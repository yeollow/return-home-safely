package knu.capston.returnhomesafely.config;

import java.time.LocalDate;
import javax.persistence.EntityManagerFactory;
import knu.capston.returnhomesafely.domain.CCTV;
import knu.capston.returnhomesafely.processor.CCTVItemProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.kafka.KafkaItemWriter;
import org.springframework.batch.item.kafka.builder.KafkaItemWriterBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaAdmin.NewTopics;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class BatchConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;
    private final KafkaTemplate<Long, CCTV> template;

    private static final int chunkSize = 10;

    @Bean
    public Job scopeJob() {
        return jobBuilderFactory.get("scopeJob")
            .start(cctvScopeStep(LocalDate.now().toString()))
            .next(policeScopeStep(LocalDate.now().toString()))
            .build();
    }

    @Bean
    @JobScope
    public Step cctvScopeStep(@Value("#{jobParameters[requestDate]}") String requestDate) {
        return stepBuilderFactory.get("cctvScopeStep")
            .<CCTV, CCTV>chunk(chunkSize)
            .reader(cctvCsvItemReader())
//            .processor(cctvItemProcessor())
            .writer(cctvKafkaItemWriter())
//            .writer(cctvJpaItemWriter())
            .build();
    }


    @Bean
    public FlatFileItemReader cctvCsvItemReader() {
        return new FlatFileItemReaderBuilder<CCTV>()
            .name("cctvItemReader")
            .resource(new ClassPathResource("cctv.csv"))
            .delimited()
            .names("managementAgency", "loadLocation", "location", "purpose",
                "numOfCamera", "pixels", "direction", "storageDays", "installationDate", "number",
                "latitude", "longitude", "dataDate")
            .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                setTargetType(CCTV.class);
            }})
            .build();

    }

    @Bean
    public CCTVItemProcessor cctvItemProcessor() {
        return new CCTVItemProcessor();
    }

    @Bean
    public JpaItemWriter<CCTV> cctvJpaItemWriter() {
        JpaItemWriter<CCTV> jpaItemWriter = new JpaItemWriter<>();
        jpaItemWriter.setEntityManagerFactory(entityManagerFactory);

        return jpaItemWriter;
    }

    //    KafkaAdmin bean is automatically registered when you use spring boot
    @Bean
    public KafkaAdmin.NewTopics batchTopics() {
        return new NewTopics(
            TopicBuilder.name("CCTV")
                .partitions(1)
                .replicas(1)
                .build(),

            TopicBuilder.name("POLICE")
                .partitions(1)
                .replicas(1)
                .build()
        );
    }

    @Bean
    public KafkaItemWriter<Long, CCTV> cctvKafkaItemWriter() {
        template.setDefaultTopic("CCTV");

        return new KafkaItemWriterBuilder<Long, CCTV>()
            .kafkaTemplate(template)
            .itemKeyMapper(CCTV::getId)
            .build();
    }


    @Bean
    @JobScope
    public Step policeScopeStep(
        @Value("#{jobParameters[requestDate]}") String requestDate) {
        return stepBuilderFactory.get("policeScopeStep")
            .tasklet((contribution, chunkContext) -> {
//                ...
                return RepeatStatus.FINISHED;
            }).build();
    }
}
