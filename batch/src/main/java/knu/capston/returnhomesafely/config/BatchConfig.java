package knu.capston.returnhomesafely.config;

import knu.capston.returnhomesafely.config.job.CCTVStepConfig;
import knu.capston.returnhomesafely.config.job.PoliceStepConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class BatchConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job scopeJob() {
        return jobBuilderFactory.get("scopeJob")
            .start(cctvStep())
            .next(policeStep())
            .build();
    }

    @Bean
    public Step cctvStep() {
        return new CCTVStepConfig(stepBuilderFactory)
            .cctvScopeStep(/*LocalDate.now().toString()*/);
    }

    @Bean
    public Step policeStep() {
        return new PoliceStepConfig(stepBuilderFactory)
            .policeScopeStep(/*LocalDate.now().toString()*/);
    }
}
