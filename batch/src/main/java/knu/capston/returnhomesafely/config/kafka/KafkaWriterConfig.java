package knu.capston.returnhomesafely.config.kafka;

import knu.capston.returnhomesafely.domain.CCTV;
import knu.capston.returnhomesafely.domain.Police;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.kafka.KafkaItemWriter;
import org.springframework.batch.item.kafka.builder.KafkaItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaAdmin.NewTopics;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
@RequiredArgsConstructor
public class KafkaWriterConfig {

    private final KafkaTemplate<Long, ?> template;

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
// 얘네를 이미 application-local.yml에 kafka 관련 properties를 설정해놓은 다음, Application Context가 실행될 때 load를 해서 그런지, bean이 이미 creation되었다는 BeanCurrentlyIncreationException이 발생함.
/*

    @Value(value = "${spring.kafka.producer.bootstrap-servers}")
    private String bootStrapServer;

    @Value(value = "${spring.kafka.producer.key-serializer}")
    private String keySerializer;

    @Value(value = "${spring.kafka.producer.value-serializer}")
    private String valueSerializer;

    @Value(value = "${spring.kafka.producer.acks}")
    private String acks;

    @Bean
    public ProducerFactory<Long, ?> producerFactory() {
        Map<String, Object> properties = Map.ofEntries(
            Map.entry(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServer),
            Map.entry(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializer),
            Map.entry(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializer),
            Map.entry(ProducerConfig.ACKS_CONFIG, acks)
        );

        return new DefaultKafkaProducerFactory<>(properties);
    }

    @Bean
    public KafkaTemplate<Long, ?> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
*/


    @Bean
    public KafkaItemWriter<Long, ? super CCTV> cctvKafkaItemWriter() {
//        KafkaTemplate<Long, CCTV> cctvKafkaTemplate = (KafkaTemplate<Long, CCTV>) kafkaTemplate();
        template.setDefaultTopic("CCTV");

        return new KafkaItemWriterBuilder<Long, CCTV>()
            .kafkaTemplate((KafkaTemplate<Long, CCTV>) template)
            .itemKeyMapper(CCTV::getId)
            .build();
    }

    @Bean
    public KafkaItemWriter<Long, ? super Police> policeKafkaItemWriter() {
//        KafkaTemplate<Long, Police> policeKafkaTemplate = (KafkaTemplate<Long, Police>) kafkaTemplate();
        template.setDefaultTopic("POLICE");

        return new KafkaItemWriterBuilder<Long, Police>()
            .kafkaTemplate((KafkaTemplate<Long, Police>) template)
            .itemKeyMapper(Police::getId)
            .build();
    }

}
