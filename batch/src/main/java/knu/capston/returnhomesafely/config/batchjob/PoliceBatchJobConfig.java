package knu.capston.returnhomesafely.config.batchjob;

import javax.persistence.EntityManagerFactory;
import knu.capston.returnhomesafely.config.kafka.KafkaWriterConfig;
import knu.capston.returnhomesafely.domain.Police;
import knu.capston.returnhomesafely.processor.PoliceItemProcessor;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.kafka.KafkaItemWriter;
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
    @JobScope
    public Step policeScopeStep(
        /*@Value("#{jobParameters[requestDate]}") String requestDate*/) {
        assert stepBuilderFactory != null;
        return stepBuilderFactory.get("policeScopeStep")
            .<Police, Police>chunk(chunkSize)
            .reader(policeItemReader())
//            .processor(policeItemProcessor())
//            .writer(policeJpaItemWriter())
            .writer(policeItemWriter())
            .build();
    }

    @Bean
    public FlatFileItemReader<? extends Police> policeItemReader() {
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


//    XXXConfig에 해당하는 Bean을 생성할 필요는 없다. XXXConfig내부에 있는 policeKafkaItemWriter()가 반환하는 KafkaItemWriter<> 자료형의 Spring Bean만 뽑아오면 됨.
//    XXXConfig에서 KafkaWriterConfig에서 필요한 Bean만 뽑아올 수 있음 -> KafkaWriterConfig에서 만든 Bean이 필요하다면, XXXConfig에서 private final 필드를 통해 해당 Bean을 생성자 주입으로 받아보자.
    @Bean
    public KafkaItemWriter<Long, ? super Police> policeItemWriter() {
        return new KafkaWriterConfig(template).policeKafkaItemWriter();
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
