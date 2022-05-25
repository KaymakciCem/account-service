# account-service

___
### Spring Boot Application

---
This project helps us to create account for existing customers.

#### Requirements
- The API exposes two endpoints. 
- First one helps us to create account for users who are already registered to the system.
- Second one helps us to retrieve the user information and transactions.
- If initialCredit is not 0, transaction microservice is called and the related transaction is created.

The application has 2 apis
* AccountAPI
* CustomerAPI

Opening an account
```curl
curl --location --request POST 'http://localhost:8081/account/open' \
--header 'Content-Type: application/json' \
--data-raw '{
"customerId": "2",
"initialCredit": 100
}'
```

Retrieving user information
```curl
curl --location --request GET 'http://localhost:8081/customer/info/{customerId}' \
--header 'Content-Type: application/json'
```

### Tech Stack

---
- Java 11
- Spring Boot
- Spring Data JPA
- Restful API
- H2 In memory database
- Docker
- K8s files
- JUnit 5

### Prerequisites

---
- Maven

### Run & Build

first go to the terminal and open up the project directory. "~/account-service/"

### Run the tests

mvn test

### Run project

mvn spring-boot:run

### Docker
to create the docker image execute the following commands
mvn clean package
mvn docker:build
