# Multi Tenancy with Spring Boot, Hibernate & Liquibase

## Overview

Multi Tenancy usually plays an important role in the business case for
SAAS solutions. Spring Data and Hibernate provide out-of-the-box support
for different Multi-tenancy strategies. Configuration however becomes more
complicated, and the available examples are few.

This project complements my blog series on Multi Tenancy, and contains
working examples of different Multi Tenant strategies implemented with
Spring Boot, Hibernate and Liquibase, complete with support for database
migrations as well as dynamically set up new tenants on the fly.

## How to start a Dockerized postgres database

All the examples require a postgres database running at localhost:5432. Run the following command 
to use the provided `docker-compose.yml` configuration to start a dockerized postgres
container:

```
docker-compose up -d
```

Close it down with the following command when done, or if you need to recreate the database:

```
docker-compose down
```

