# Walleto

**Walleto** is an e-wallet application linked to a bank account that lets users make
transactions, pay bills, and receive cashbacks. It has an Angular front end backed by
Spring Boot REST services.

## Modules

This repository is a monorepo containing three Maven/Angular modules:

| Module | Description | Stack |
|--------|-------------|-------|
| `amigowalletfrontend` | Single-page web UI (wallet, bill payment, home) | Angular 7.2, Bootstrap 4, RxJS |
| `amigowalletbackend`  | Wallet REST API (transactions, bill payments, users) | Spring Boot 2.1, Spring Data JPA, MySQL |
| `edubank`             | Companion banking service (PDF statements via iText) | Spring Boot 2.1, Spring Data JPA/REST |

## Tech stack

> Versions below reflect what is currently pinned in the project manifests.

- **Frontend:** Angular 7.2, TypeScript 3.2, Bootstrap 4.3, RxJS 6.3, ngx-translate, ngx-pagination
- **Backend:** Java 8, Spring Boot 2.1.0, Spring Data JPA, JPQL, MySQL
- **Build & tooling:** Maven, Angular CLI 7
- **Testing:** JUnit, Mockito, PMD (backend); Karma, Jasmine, Protractor (frontend)
- **CI/CD:** Jenkins

## Getting started

### Prerequisites
- Java 8 (JDK 1.8) and Maven
- Node.js + npm and Angular CLI 7 (`npm install -g @angular/cli@7`)
- A running MySQL instance (configure credentials in each backend's
  `src/main/resources/application.properties`)

### Backend (Spring Boot)

```bash
cd amigowalletbackend     # or: cd edubank
./mvnw spring-boot:run
```

The API starts on its configured port (default `8080`).

### Frontend (Angular)

```bash
cd amigowalletfrontend
npm install
npm start                 # ng serve -> http://localhost:4200
```

## Project structure

```
.
├── amigowalletfrontend/   # Angular SPA (wallet, billpayment, home, shared)
├── amigowalletbackend/    # Spring Boot API (api, service, dao, entity, model, validator)
└── edubank/               # Spring Boot banking service (PDF statements)
```

## Notes

The dependency versions here target the original build environment (Java 8 / Angular 7).
Upgrading to current Angular and Spring Boot releases involves several breaking major
versions (including the Spring Boot 3 Jakarta namespace migration) and should be done
deliberately with the build re-verified at each step.
