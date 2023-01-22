# SalesService

SalesService is a service that is the solution to the test from AnyMind Group. This service allows you to create a sale and get aggregated results of those sales. More information regarding the api can be found in the graphql schema documentation or the test pdf document.

I had fun experience implementing this and was impressed by the graphql technology. The following documentation helped me:
- https://docs.spring.io/spring-graphql/docs/current/reference/html/#overview
- https://github.com/eugenp/tutorials/tree/master/spring-boot-modules/spring-boot-graphql
- https://www.baeldung.com/spring-graphql
- https://www.baeldung.com/spring-graphql-error-handling

Let me know if you have any questions.
## Run

To run the service and test it, there are 2 ways.

Using gradle *quick start*:

```bash
./gradlew bootRun
```
Note: The command above requires you to have port 8080 and 5432 unused and docker compose installed and running.

Using docker-compose *slow start*:

```bash
docker-compose up
```
Note: The reason why it is slow is due to the building image process and gradle daemon starting up the first time. Although having gradle,java installed and ports free are not required.

## Usage
There is a gui available to test the functionality of the project. All data created is temporary due to the volume of postgres database is in-memory.
```http
http://localhost:8080/graphiql?path=/graphql

```
## Tests
To run tests, please run test gradle task.
```bash
./gradlew test

```
I've tested the required functionality and also tested for invalid data. If there are any questions, please do not hesitate to contact me. Both unit tests and integration tests were used.
