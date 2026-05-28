# Sludgate Brass Band — Band Resource Management System

A full-stack web application for managing a community brass band's members, 
instrument loans, music library, and performances. Built as a 6-person 
university team project (Team 20).

## Tech Stack

- **Backend:** Java, Spring Boot, Spring Security, Spring Data JPA
- **Database:** MySQL
- **Build:** Gradle
- **Testing:** JUnit
- **Frontend:** Thymeleaf, HTML/CSS, JavaScript

## Features

- Role-based access control across five user roles (director, committee 
  member, member, parent, child) using Spring Security
- Member and band management (senior and training bands)
- Instrument and miscellaneous item loan tracking
- Music library and performance management
- Secure authentication with BCrypt password hashing

## My Contribution

This was a 6-person team project. My contribution focused on the 
item-management subsystem:

- Built the controllers and service classes handling instrument and 
  miscellaneous loan items
- Modified the `User` and `Item` domain models to support item relationships
- Refactored the codebase by removing superseded controller, service, and 
  repository classes, and added documentation across model and controller classes

## Running Locally

Prerequisites: Java, Gradle, and a MySQL instance.

1. Configure your database connection in `application.properties` 
   (use your own MySQL credentials).
2. Start the server:
```bash
   gradle bootRun
```
3. Open the application at http://localhost:8080/login

## Running Tests

```bash
gradle clean test -info
```

Test results are generated in `build/reports/tests/test` — open `index.html` to view them.

## Authors

Team 20:
- Michael Udo
- Edward Beavis
- Juin Kai Lim
- Kingsley Sherwood
- Roshan Virdee
- Sau Yip
