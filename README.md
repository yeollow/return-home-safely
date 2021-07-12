## Bigdata pipelining / Spring Batch

우선 **Bigdata pipelining**과정은 아래와 같으며, _Spring Batch_ 의 실행으로부터 일어난다.  
_Spring Batch_ 과정은 `job` 을 기본단위로 실행하며 각 `job` 은 아래와 같은 구조를 가지는 하나의 `tasklet` 즉, `step` 을 단위로 실행된다.
- ItemReader : `FlatFileItemReaderBuilder<>()` 를 통해 공공데이터 성격 별 `.csv` file을 읽어들인다.
- ItemProcessor : 읽어들인 `.csv` file을 원하는 형식으로 전처리 한다.
- ItemWriter : `KafkaItemWriter<>()` 를 통해 읽어들인 성격 별 data를 `Spring-kafka` 를 통해 생성해놓은 _kafka broker_ 의 _topic_ 으로 전송한다.

<br>
<img src="https://user-images.githubusercontent.com/47915704/125220801-37c48d80-e302-11eb-88c5-7360ab49adfd.png" width="550" height="250">

이후 **_kafka_** 와 **_hadoop_** 과 연동된 _fluentd plugin_ 을 통해 _hadoop HDFS_ 에 _kafka topic data_ 를 저장한 뒤, **_Spark_** 를 통해 MR작업을 거쳐 원하는 data schema형태의
dataFrame을 얻어 **MySQL** 에 table형태로 저장하는 과정을 거친다.

<br>
<img src="https://user-images.githubusercontent.com/47915704/125220015-eff13680-e300-11eb-9c1d-f8fbd2b1c285.png" width="550" height="250">

## Spring API Server
위와 같이 _Spring Batch_ 및 _Bigdata pipelining_ 과정을 거쳐 **MySQL** 에 table형태로 저장된 성격 별 공공데이터를 통해 사용자의 요청에 따라 조회 후 반환하며 기본적으로 _Spring MVC pattern_ 을 따른다.
- Controller : **_HTTP_** 를 통해 들어오는 요청을 받고, 실행한 business logic에 대한 결과물을 application/json data형식으로 다시 반환한다.
- Service :  geoHash계산, CRUD작업 위임 등의 business logic을 처리한다.
- Repository : 연동된 **MySQL** 에서 Service에서 위임되었던 CRUD작업을 진행한다.

<br>
<img src="https://user-images.githubusercontent.com/47915704/125222442-f4b7e980-e304-11eb-9e6e-b0acccc453ed.png" width="550" height="250">

또한 Nginx WS를 구축하여 HTTP 기본 포트인 80을 reverse proxy한다.

**GeoHash** : geographic location을 문자와 숫자로 이루어진 짧은 String형태로 반환하여 공간을 사각형으로 분할하는 계층적인 공간 데이터 구조로 나눈다. [GeoHash내용 참조](https://scvgoe.github.io/2018-12-11-Geohash/)


**프로젝트 보완 사항**
> 결과적으로 geoHash관련 부분은 Redis가 제공한다고 하니, Spring Redis연동으로 구현해본다.
> 해당 Application을 monolothic architecture가 아닌, docker / kubernetes를 활용한 cloud native 환경에 걸맞는 MSA구조로의 변환을 염두해본다.
