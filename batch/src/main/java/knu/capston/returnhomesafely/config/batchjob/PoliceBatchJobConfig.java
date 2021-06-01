package knu.capston.returnhomesafely.config.batchjob;

import javax.persistence.EntityManagerFactory;
import knu.capston.returnhomesafely.domain.Police;
import knu.capston.returnhomesafely.processor.PoliceItemProcessor;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
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
public class PoliceBatchJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;

    private final KafkaTemplate<Long, Police> template;
    private static final int chunkSize = 10;

        @Bean
        public Job policeJob() {
            return jobBuilderFactory.get("policeJob")
                .start(policeScopeStep())
                .build();
        }

    @Bean
    //    @JobScope
    public Step policeScopeStep() {
        assert stepBuilderFactory != null;
        return stepBuilderFactory.get("policeScopeStep")
            .<Police, Police>chunk(chunkSize)
            .reader(policeItemReader())
//            .processor(policeItemProcessor())
//            .writer(policeJpaItemWriter())
            .writer(policeKafkaItemWriter())
            .build();
    }

    @Bean
    public ItemReader<? extends Police> policeItemReader() {
        return new FlatFileItemReaderBuilder<Police>()
            .name("policeItemReader")
            .resource(new ClassPathResource("police.csv"))
            .delimited()
            .names("managementAgency", "policeOffice", "longitude", "latitude", "location")
            .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                setTargetType(Police.class);
            }})
            .build();
    }

    @Bean
    public PoliceItemProcessor policeItemProcessor() {
        return new PoliceItemProcessor();
    }

    @Bean
    public KafkaItemWriter<Long, ? super Police> policeKafkaItemWriter() {
        template.setDefaultTopic("POLICE");

        return new KafkaItemWriterBuilder<Long, Police>()
            .kafkaTemplate(template)
            .itemKeyMapper(Police::getId)
            .build();
    }

/*
    @Bean
    public JpaItemWriter<Police> policeJpaItemWriter() {
        JpaItemWriter<Police> jpaItemWriter = new JpaItemWriter<>();
        jpaItemWriter.setEntityManagerFactory(entityManagerFactory);

        return jpaItemWriter;
    }
*/


}
