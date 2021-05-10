package knu.capston.returnhomesafely;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableBatchProcessing      //must declare to use Spring Batch functionalities
@SpringBootApplication
public class BatchApplication {

    public static void main(String[] args) {
//        System.exit(SpringApplication.exit(SpringApplication.run(BatchApplication.class,args)));
        SpringApplication.run(BatchApplication.class, args);
    }
}
/*

* BatchApplication Process
* 1. springApplication
*   - push data to kafka broker topics with spring-kafka

       Todo List :
            kafka broker and zookeeper installation and kafka cluster construction
            create topics and allocate partitions

    - after pulling the topic data, read the data using fluentd in hadoop and save it as .txt in HDFS
*
*
* 2. sparkApplication
    -  indexing GPS information(longitude, latitude)data in .txt files on HDFS through sparkApplication in YARN cluster and saving it in MariaDB

        Todo List:
            spark installation and allocate master / worker Node
            installation HDFS nameNode, YARN resourceManager on spark master Node
            installation HDFS dataNode, YARN nodeManager on spark worker Node
            installation HDFS and YARN on spark client and run sparkApplication through yarn cluster on hadoop

    * sparkApplication and springApplication are independent applications and are managed through Jenkins jobs.
        - the jobs run periodically
* */