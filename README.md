# Library Management (Spring Boot)

## Stack
- Java 17
- Spring Boot 3.x
- Thymeleaf + Bootstrap 5 (CDN)
- MySQL

## Setup
1) Create database
```
CREATE DATABASE library_management;
```

2) Update DB credentials in `src/main/resources/application.yml` if needed:
```
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/library_management?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
    username: root
    password: root
```

3) Run
```
./mvnw spring-boot:run
```
Or with Maven:
```
mvn spring-boot:run
```

4) Open
```
http://localhost:8080
```

## Demo Accounts
- ADMIN: admin / Admin@123
- LIBRARIAN: librarian / Librarian@123
- MEMBER: member / Member@123

## Roles
- ADMIN: user management + dashboard + books + loans
- LIBRARIAN: books + copies + loans
- MEMBER: profile, catalog, history

## Seed Data
- 2 categories
- 5 books, each 2 copies
- 2 loans (1 active, 1 returned)

## Notes
- Session login (no JWT)
- Basic security: JPA, validation, Thymeleaf escaping