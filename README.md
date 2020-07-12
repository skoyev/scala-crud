# Play REST/ElasticSearch API

## Appendix

### Introduction

This application consists of REST APIs to perform CRUD operations using PlayFramework, Scala and Elasticsearch(v6.x) as database. 

REST APIs are documented using Swagger.
#### Tools and Technology
* [Play Framework](https://www.playframework.com/documentation/2.4.x/Home) - 2.4.11
* [Scala Library](https://github.com/scala/scala/tree/v2.11.6) - 2.11.6
* [Elasticsearch](https://www.elastic.co/guide/en/elasticsearch/reference/6.2/index.html) - 6.2.3
* [Elastic4s](https://github.com/sksamuel/elastic4s/tree/v6.2.9) - 6.2.9
* [Swagger](https://github.com/swagger-api/swagger-play) - 1.5.0
* [Accord](http://wix.github.io/accord/) - 0.7.2
* [Java Faker](https://github.com/DiUS/java-faker) - 0.15

### Running

#### Prerequisite
* [sbt](https://www.scala-sbt.org/)
* [Docker](https://www.docker.com/)
* [docker-compose](https://docs.docker.com/compose/)

Once you have above software installed. Download/checkout the project and jump to project directory. Then run below commands to run the application:

```bash
docker-compose up
```
This will start elasticsearch in docker. You can check the status of elasticsearch from your browser by hitting the URL: <http://localhost:9200/>

Once your elasticsearch is up and running. It's time to start the application.

Run below command to start the application:

```bash
sbt run
```

Play will start up on the HTTP port at <http://localhost:9000/>.   You don't need to deploy or reload anything -- changing any source code while the server is running will automatically recompile and hot-reload the application on the next HTTP request.

#### Testing 
* To execute tests:
```
sbt test
```

#### Swagger
* Swagger documentation of REST APIs can be found at <http://localhost:9000/docs/>

#### Author
 **Sergiy Koyev** 
* [GitHub](https://github.com/skoyev)