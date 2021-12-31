# Spring REST API Starter


## What is this repository

This repository is a starting point for a Spring REST API project. It contains all necessary configurations to get a
working REST API up and running.

## Dependencies
<details>
<summary>Click to expand!</summary>
   
| **Stack**           | **Version** | **Description**                                         |
|---------------------|:-----------:|---------------------------------------------------------|
| Spring Boot         |    2.4.5    | Config Spring-app with Java Annotation                  |
| Spring Web MVC      |    2.4.5    | Build REST API endpoint                                 |
| Spring Security     |    2.4.5    | Securing application                                    |
| Spring Data JPA     |    2.4.5    | Connect database and manipulate data                    |
| Spring Mail         |    2.4.5    | Send email                                              |
| Spring WebSocket    |    2.4.5    | Build WebSocket server                                  |
| Spring Messaging    |    2.4.5    | Send message over WebSocket                             |
| Spring Cache        |    2.4.5    | Caching data                                            |
| Spring AOP          |    2.4.5    | Support Aspect-Oriented Programming                     |
| Caffeine            |    2.9.0    | Memcached library for Java, use for control cached data |
| MySQL               |     8.x     | Database for application                                |
| Lombok              |   1.18.22   | Generate common method to reduce boilerplate code       |
| Java Validation API |    2.0.1    | Validate parameter                                      |
| JJWT                |    0.9.1    | Creating and verifying JSON Web Token in Java           |
| Apache Common Text  |     1.9     | Contains several useful methods to work with String     |
| Bucket4j            |     7.0     | For implement API Rate Limit feature                    |

</details>
   
## Features

* Language level and runtime JDK is Java 8
* Spring EcoSystem 2.4.5
* Persistence:

    * MySQL 8.0
    * Hibernate 5.4.30
    * Example Entity:
        * An `Account` entity to store basic information of user

* REST API with Spring Web MVC

    * An `AuthController` to demonstrate how to authenticate user with JWT
    * An `ExampleController` to demonstrate how to use `@PreAuthorize` to restrict access to endpoint and
      use `Spring Cache` to cache data

* Logging

    * Logging with `Slf4j` from `Lombok`
    * AOP profiling of all Spring beans method calls (e.g. method parameters, return values, errors)

* Security

    * Authentication with JSON Web Token
    * Block too many requests from the same IP Address in a duration of time (API Rate Limit)
    * CORS configuration
    * Annotations to restrict endpoints to certain roles
    * Custom `CurrentUser` to get current user information from JWT token

* Exception Handling

    * Custom `HttpError` to represent error response
    * Automatic exception handling with `GlobalExceptionHandler` class

* Several utility classes to support common task

    * `MyStringUtils` to support common methods to work with String (e.g. escape HTML, generate slug, etc.)
    * `MyMailSender` to support common methods to work with Email
    * `AssertUtils` to support common methods in validating parameter
    * `SecurityUtils` to support common methods to work with Spring Security

* Dockerfile to build a production-ready image

## How to use this repository

### Prerequisites

* Globally install Java 8
* Install your database engine (e.g. MySQL 8.0, MariaDB, etc.) and edit `application-private.yml` depend on your need (
  see _[Installation](#installation)_ )
* Install Docker (optional)

### Installation

1. Use this repository as a template and create your GitHub repository
2. Search and replace "spring-rest-api-starter", "example" with your project name
3. Edit the properties in `application.yml` and `application-private.yml` based on your need (e.g. database, email,
   etc.)
4. Install maven dependencies with `./mvnw clean install`

If no error, you are good to go. Now, start coding and run it with `./mvnw spring-boot:run`.

> To build Docker image, you need to install Docker and simply run `docker build -t <YOUR_DOCKER_HUB_USERNAME>/<YOUR_PROJECT_NAME>:latest .`

## Contact

If you want to ask anything, create an Issue, or contact me [@dvt](https://www.facebook.com/trinh.dvt/)

## Appendix

Project structure
<details>
<summary>Click to expand!</summary>
   
```
├── Dockerfile -> Dockerfile to build Docker image
├── README.md
├── mvnw
├── mvnw.cmd
├── pom.xml -> Dependency management
└── src -> Source code
    └── main
        ├── java
        │ └── com
        │     └── example
        │         ├── ExampleApplication.java -> Entry point of application
        │         ├── ServletInitializer.java
        │         │
        │         ├── annotation -> Custom annotation
        │         │ └── validator -> Custom anntation's processor
        │         │
        │         ├── config -> Application basic configuration (e.g. web, cache, etc.)
        │         │ ├── CacheConfig.java
        │         │ ├── WebConfig.java
        │         │ └── ...
        │         │
        │         ├── controller -> REST API Endpoint Definition
        │         │ └── ExampleController.java
        │         │
        │         ├── event
        │         │ └── listener -> Handle event
        │         │     └── AccountEntityListener.java
        │         │
        │         ├── exception
        │         │ ├── GlobalExceptionHandler.java -> Exception handler
        │         │ ├── HttpError.java -> Custom error response
        │         │ └── ...
        │         │
        │         ├── model -> Domain model, DTO, etc.
        │         │ ├── constraints
        │         │ ├── dto
        │         │ │ ├── request
        │         │ │ └── response
        │         │ └── entity
        │         │
        │         ├── repository -> Data Access Layer
        │         │ └── AccountRepository.java
        │         │ 
        │         ├── security -> Security configuration
        │         │ ├── SecurityConfig.java
        │         │ ├── ...
        │         │ ├── filter
        │         │ ├── ratelimit
        │         │ └── services -> Security services (seperate from Bussiness serivce)
        │         │     ├── TokenServices.java
        │         │     └── ...
        │         │
        │         ├── services -> Business logic
        │         │ ├── IAuthServices.java
        │         │ └── impl
        │         │     └── AuthServicesImpl.java
        │         │
        │         └── utils -> Utility class
        │             ├── AssertUtils.java
        │             └── ...
        │
        └── resources -> Application resources (e.g. spring config file, static files, etc.)
            ├── application-private.yml -> Sensitive configuration
            ├── application.yml -> Common configuration

```
</details>
