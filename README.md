# JWT Spring Boot Security Config

This project demonstrates a simple API secured with **JWT authentication**.
It includes:

* User authentication via JWT tokens.
* Role-based authorization with roles stored in the database.
* Support for role hierarchy (`ADMIN` > `USER` > `GUEST`). 

---

## Running the service locally

```bash
./gradlew bootJar
docker-compose up -d
```

Once started, the service exposes the OpenAPI definition at:
👉 [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

---

## Obtaining a token

To obtain a JWT bearer token, send a request to the `/auth/token` endpoint using **Basic Authentication**:

```
Authorization: Basic <Base64(username:password)>
```

Example:

```bash
curl -X GET "http://localhost:8080/auth/token" \
  -H "Authorization: Basic $(echo -n 'admin:Admin123' | base64)"

```

---

## Test data

On the first startup, the `DbInitializer` creates:

* An `admin` user (username: `admin`, password: `Admin123`)
* 10 additional test users

