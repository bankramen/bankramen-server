# Repository Guidelines

## Project Structure & Module Organization
This repository is a Gradle-based Spring Boot backend. Put application code under `src/main/java/org/example`, grouped by feature (`auth`, `user`, `common`, etc.) rather than by framework alone. Keep runtime configuration in `src/main/resources/application.yaml`. Place tests in `src/test/java/org/example` with the same package structure as production code.

Current dependencies indicate a MySQL + JPA + Redis + Spring Security/JWT stack, so keep persistence, cache, and security concerns separated into focused packages.

## Build, Test, and Development Commands
Use the Gradle wrapper through Bash because `gradlew` is not executable in the current repo state.

- `bash ./gradlew test` — run the JUnit 5 and Spring test suite.
- `bash ./gradlew build` — compile, test, and package the app.
- `bash ./gradlew bootRun` — start the service locally.
- `bash ./gradlew clean` — remove Gradle build outputs.

The build requires **JDK 26** (`build.gradle` toolchain setting). Export the environment variables referenced in `application.yaml` before running locally, such as `MYSQL_URL`, `REDIS_HOST`, and `JWT_SECRET`.

## Coding Style & Naming Conventions
Use 4-space indentation and keep files formatted consistently with standard Java/Spring conventions. Use lowercase package names, `PascalCase` for classes, `camelCase` for methods/fields, and `UPPER_SNAKE_CASE` for constants. Prefer constructor injection for Spring beans and keep controllers thin; business logic should live in services.

## Testing Guidelines
Use `spring-boot-starter-test` and `spring-security-test`. Name tests `*Test` and mirror the production package structure. Add focused unit tests for services/utilities and use slice or integration tests only when framework wiring matters. No coverage gate is configured yet, but new behavior should ship with regression tests.

## Commit & Pull Request Guidelines
Recent history follows a short conventional style such as `chore(#1): remove unused entity` and `init(#1): setting`. Keep that `type(#issue): summary` pattern, and include rationale plus verification details in the PR description. For API or security changes, add sample requests/responses, changed environment variables, and linked issue numbers.

## Security & Configuration Tips
Never hardcode secrets. Keep all database, Redis, and JWT values in environment variables. If you change auth, persistence, or cache settings, document the required config changes in the PR.
