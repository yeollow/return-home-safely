package knu.capston.returnhomesafely.config.batchjob;

import javax.persistence.EntityManagerFactory;
import knu.capston.returnhomesafely.domain.CCTV;
import knu.capston.returnhomesafely.processor.CCTVItemProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.kafka.KafkaItemWriter;
import org.springframework.batch.item.kafka.builder.KafkaItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
@RequiredArgsConstructor
public class CCTVBatchJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;

    private final KafkaTemplate<Long, CCTV> template;
    private static final int chunkSize = 10;

    @Bean
    public Job cctvJob() {
        return jobBuilderFactory.get("cctvJob")
            .start(cctvScopeStep())
            .build();
    }

    @Bean
//    @JobScope
    public Step cctvScopeStep(
        /*@Value("#{jobParameters[requestDate]}") String requestDate*/) {
        assert stepBuilderFactory != null;
        return stepBuilderFactory.get("cctvScopeStep")
            .<CCTV, CCTV>chunk(chunkSize)
            .reader(cctvItemReader())
//            .processor(cctvItemProcessor())
            .writer(cctvKafkaItemWriter())
//            .writer(cctvJpaItemWriter())
            .build();
    }

    @Bean
    public ItemReader<? extends CCTV> cctvItemReader() {
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
    public KafkaItemWriter<Long, ? super CCTV> cctvKafkaItemWriter() {
        template.setDefaultTopic("CCTV");

        return new KafkaItemWriterBuilder<Long, CCTV>()
            .kafkaTemplate(template)
            .itemKeyMapper(CCTV::getId)
            .build();
    }

    /*
    @Bean
    public JpaItemWriter<CCTV> cctvJpaItemWriter() {
        JpaItemWriter<CCTV> jpaItemWriter = new JpaItemWriter<>();
        jpaItemWriter.setEntityManagerFactory(entityManagerFactory);

        return jpaItemWriter;
    }
    */
}
