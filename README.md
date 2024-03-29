# reactive-talk-202012-kotlin

#### Kotlin version of my demo code for Reactive Spring Talk 12/2020.

![CI build](https://github.com/ksbrwsk/reactive-talk-202012-kotlin/workflows/CI%20build/badge.svg)

**Prerequisites:**

* [Java 17](https://adoptopenjdk.net/)
* [Apache Maven](https:http://maven.apache.org/)
* [Docker](https://www.docker.com/)
* Optional [Postman](https://www.postman.com/)

If you have Docker installed, grab a PostgreSQL image from Docker Hub and run the image.
```bash
docker pull bitnami/postgresql:12
docker run --name postgresql -e POSTGRESQL_PASSWORD=password123 -e POSTGRES_DB=spring -p 5432:5432 bitnami/postgresql:12
```
Or else, grab the app image via 
```bash
docker pull ksabrwsk/reactive-talk-202012:1.0.0
```

The PostgreSQL flyway database schemas are located under
```bash
reactive-talk-202012/src/main/resources/db/migration
```

Application properties can be configured in

```bash
reactive-talk-202012/src/main/resources/application.properties
```

#### How to build and run

Type

```bash
mvn package
mvn spring-boot:run
```

to build and run the application on your local environment.

To run the app as a Docker Container type the following commands to build
an run the image:
```bash
docker build ./ -t reactive-talk-202012-kotlin
docker-compose up --force-recreate
```

Use curl, httpie etc. to call the HTTP Endpoints, or use Postman, a collection file is located under
```bash
reactive-talk-202012.postman_collection.json
```

**Themes:**

* Spring Webflux
* Router Functions/Handler Functions
* R2DBC Database Connectivity
* Unit Testing
* Integration Testing with Testcontainers
* Docker build/compose
