package knu.capston.returnhomesafely.config.job;

import knu.capston.returnhomesafely.config.kafka.KafkaWriterConfig;
import knu.capston.returnhomesafely.domain.Police;
import knu.capston.returnhomesafely.processor.PoliceItemProcessor;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.kafka.KafkaItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class PoliceStepConfig {

    private final StepBuilderFactory stepBuilderFactory;

    private static final int chunkSize = 10;

    @Bean
    @JobScope
    public Step policeScopeStep(
        /*@Value("#{jobParameters[requestDate]}") String requestDate*/) {
        assert stepBuilderFactory != null;
        return stepBuilderFactory.get("policeScopeStep")
            .<Police, Police>chunk(chunkSize)
            .reader(policeItemReader())
//            .writer(policeItemWriter())
            .build();
    }

    @Bean
    public FlatFileItemReader<? extends Police> policeItemReader() {
        return new FlatFileItemReaderBuilder<Police>()
            .name("policeItemReader")
            .resource(new ClassPathResource("police.csv"))            //csv파일 내용 변경
            .delimited()
//            entity 변경
            .names("managementAgency", "loadLocation", "location", "purpose",
                "numOfCamera", "pixels", "direction", "storageDays", "installationDate", "number",
                "latitude", "longitude", "dataDate")
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
    public KafkaItemWriter<Long, Police> policeItemWriter() {
        return new KafkaWriterConfig().policeKafkaItemWriter();
    }

}
