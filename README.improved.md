Insurance Management System

REST API untuk mengelola data asuransi (nasabah, polis, klaim, pembayaran).

Tech Stack

- Java 17
- Spring Boot 3.2 (Spring Boot starter parent)
- Spring Security, Spring Data JPA
- H2 (in-memory) for development
- jjwt 0.12.3 for JWT authentication

Prerequisites

- Java 17 (JDK)
- Git
- Internet connection for Maven dependencies

Quick start (development)

1. Run the application:

```powershell
./mvnw.cmd spring-boot:run
```

2. Open the API at:

http://localhost:8080

Build & package

- Create a packaged JAR:

```powershell
./mvnw.cmd clean package
```

- Install to local Maven repository:

```powershell
./mvnw.cmd clean install
```

Publish to GitHub Packages (CI)

This repository contains a GitHub Actions workflow at `.github/workflows/publish.yml` that builds and deploys to GitHub Packages using the default `GITHUB_TOKEN`.

To enable publishing from your fork or local branch:

1. Push the repository/branch to GitHub (example):

```powershell
git remote add origin https://github.com/<your-user>/insurance-management-system.git
git push -u origin main
```

2. From the repository on GitHub, open the Actions tab and run the `Publish to GitHub Packages` workflow (or wait for a push to `main`).

Direct deploy from this machine (alternative)

You can also deploy directly with `mvn deploy` to GitHub Packages by configuring a `settings.xml` with a server entry named `github` using a Personal Access Token with `repo` scope.

Example `mvn` command (uses altDeploymentRepository):

```powershell
./mvnw.cmd -B clean deploy -DskipTests=true -DaltDeploymentRepository=github::default::https://maven.pkg.github.com/<owner>/<repo>
```

Replace `<owner>` and `<repo>` with your values and provide credentials in `~/.m2/settings.xml`.

Useful commands

- Run tests:

```powershell
./mvnw.cmd test
```

- Run with a specific profile (example `dev`):

```powershell
./mvnw.cmd -Dspring.profiles.active=dev spring-boot:run
```

Contributing

- Open issues or PRs on GitHub.
- Add unit/integration tests for new features.

License

- (Add license information here)

Notes

- The project currently targets Java 17 in `pom.xml`. If you upgrade Java, update the `pom.xml` `<java.version>` property and verify the Maven wrapper if needed.

Contact

- Repository: https://github.com/habiutomo/insurance-management-system
