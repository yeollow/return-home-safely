package knu.capston.returnhomesafely.config.kafka;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin.NewTopics;

@Configuration
public class KafkaTopicsConfig {

    @Bean
    public NewTopics topicSet() {
        return new NewTopics(
            TopicBuilder.name("CCTV")
                .replicas(1)
                .partitions(1)
                .build(),

            TopicBuilder.name("POLICE")
                .replicas(1)
                .partitions(1)
                .build()
        );
    }
}
