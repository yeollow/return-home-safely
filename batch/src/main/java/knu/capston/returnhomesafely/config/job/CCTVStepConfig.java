package knu.capston.returnhomesafely.config.job;

import knu.capston.returnhomesafely.config.kafka.KafkaWriterConfig;
import knu.capston.returnhomesafely.domain.CCTV;
import knu.capston.returnhomesafely.processor.CCTVItemProcessor;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.kafka.KafkaItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class CCTVStepConfig {

    private final StepBuilderFactory stepBuilderFactory;
    private static final int chunkSize = 10;

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
    public KafkaItemWriter<Long, ? super CCTV> cctvItemWriter() {
        return new KafkaWriterConfig().cctvKafkaItemWriter();
    }
}
