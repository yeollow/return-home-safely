package knu.capston.returnhomesafely.config.batchjob;

import javax.persistence.EntityManagerFactory;
import knu.capston.returnhomesafely.config.kafka.KafkaWriterConfig;
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

//    이렇게 되면 Job실행 순서가 어떻게 되는거지?
    @Bean
    public Job cctvJob() {
        return jobBuilderFactory.get("cctvJob")
            .start(cctvScopeStep())
            .build();
    }

    @Bean
    @JobScope
    public Step cctvScopeStep(
        /*@Value("#{jobParameters[requestDate]}") String requestDate*/) {
        assert stepBuilderFactory != null;
        return stepBuilderFactory.get("cctvScopeStep")
            .<CCTV, CCTV>chunk(chunkSize)
            .reader(cctvItemReader())
//            .processor(cctvItemProcessor())
            .writer(cctvItemWriter())
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

//    XXXConfig에 해당하는 Bean을 생성할 필요는 없다. XXXConfig내부에 있는 policeKafkaItemWriter()가 반환하는 KafkaItemWriter<> 자료형의 Spring Bean만 뽑아오면 됨.
//    XXXConfig에서 KafkaWriterConfig에서 필요한 Bean만 뽑아올 수 있음 -> KafkaWriterConfig에서 만든 Bean이 필요하다면, XXXConfig에서 private final 필드를 통해 해당 Bean을 생성자 주입으로 받아보자.
    @Bean
    public KafkaItemWriter<Long, ? super CCTV> cctvItemWriter() {
        return new KafkaWriterConfig(template).cctvKafkaItemWriter();
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
