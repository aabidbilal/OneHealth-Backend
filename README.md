# OneHealth — Personal Health Record (PHR) Platform

OneHealth is an AI-powered Personal Health Record (PHR) platform designed to help patients securely store, manage, and access their medical records digitally.

The backend provides REST APIs for user authentication, patient and doctor management, medical report management, and consent-based access control. It uses JWT-based authentication, MySQL for structured data, and MinIO for storing medical report files.

> **Project status:** Under development  
> **Repository:** [OneHealth-Backend](https://github.com/aabidbilal/OneHealth-Backend)

---

## Table of Contents

- [Overview](#overview)
- [Key Features](#key-features)
- [Technology Stack](#technology-stack)
- [Architecture](#architecture)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
- [Database Configuration](#database-configuration)
- [MinIO Configuration](#minio-configuration)
- [Environment Variables](#environment-variables)
- [Running the Application](#running-the-application)
- [Running Tests](#running-tests)
- [Project Structure](#project-structure)
- [API Testing](#api-testing)
- [Troubleshooting](#troubleshooting)
- [Security Considerations](#security-considerations)
- [Future Enhancements](#future-enhancements)
- [Contributing](#contributing)
- [License](#license)

---

## Overview

Healthcare records are often distributed across different hospitals, clinics, and healthcare providers. Accessing a patient's complete medical history can therefore be difficult.

OneHealth aims to provide a centralized platform where patients can manage their medical reports and control access to their health information.

The backend is responsible for:

- User registration and authentication.
- Patient profile management.
- Doctor registration and management.
- Medical report metadata and file management.
- Secure storage and retrieval of medical documents.
- Patient-controlled doctor access and access-request management.

OneHealth is being developed as a project to explore healthcare application development, backend engineering, authentication, object storage, and consent-based access control.

**Note:** OneHealth is an independent project. Integration with India's Ayushman Bharat Digital Mission (ABDM) is not implied by the current implementation.

---

## Key Features

### 1. Authentication and Authorization

- User registration and login.
- JWT-based authentication.
- Role-based application security.
- Secure handling of authenticated requests.

### 2. Patient Management

- Patient profile management.
- Patient-related data associated with registered users.
- Access to patient functionality through backend APIs.

### 3. Doctor Management

- Doctor registration and profile management.
- Doctor-related backend operations.
- Integration with the application's access-control workflow.

### 4. Medical Report Management

- Medical report management through REST APIs.
- Report metadata stored in MySQL.
- Medical report files stored using MinIO object storage.
- File upload validation and configurable upload limits.
- Report retrieval and download functionality implemented in the backend.

### 5. Consent-Based Access Control

- Management of doctor access requests.
- Tracking of access-request statuses.
- Patient-controlled approval and revocation workflows supported by the access-control implementation.

> The availability and behavior of individual features depend on the current implementation. Refer to the source code for the exact API contracts and authorization rules.

---

## Technology Stack

| Technology | Purpose |
|---|---|
| Java | Backend programming language |
| Spring Boot | Application framework |
| Spring Web | REST API development |
| Spring Security | Authentication and authorization |
| JWT | Token-based authentication |
| Spring Data JPA | Database access |
| Hibernate | Object-relational mapping |
| MySQL | Relational database |
| MinIO | Medical report file storage |
| Maven | Dependency management and build automation |
| Git and GitHub | Version control and source code hosting |
| IntelliJ IDEA | Recommended development environment |

---

## Architecture

OneHealth uses a layered backend architecture.

```text
                 Client Application
                         |
                         | HTTP / REST API
                         v
                Spring Boot Backend
                         |
             +-----------+-----------+
             |           |           |
             v           v           v
        Controllers   Security    Validation
             |
             v
           Services
             |
             +-----------------------+
             |                       |
             v                       v
       Repositories            File Storage
             |                       |
             v                       v
           MySQL                   MinIO
       Structured Data         Medical Files
```

### Responsibilities

**Controllers**
- Handle incoming HTTP requests.
- Validate request data and return API responses.

**Services**
- Implement application and business logic.
- Coordinate authentication, patient management, report management, and access-control operations.

**Repositories**
- Interact with the database through Spring Data JPA.

**MySQL**
- Stores structured application data, including user, patient, doctor, report, and access-control information as defined by the entity mappings.

**MinIO**
- Stores medical report files separately from relational database records.

**Spring Security and JWT**
- Protect application endpoints according to the configured authentication and authorization rules.

---

## Prerequisites

Install the following software before running the project.

| Requirement | Purpose |
|---|---|
| Java JDK 24 | Runs and compiles the application |
| Git | Clones the repository |
| MySQL Server | Stores structured application data |
| Docker Desktop | Recommended way to run MinIO locally |
| IntelliJ IDEA or another Java IDE | Optional development environment |
| Postman or equivalent HTTP client | Optional API testing |

The project includes a Maven Wrapper (`mvnw` and `mvnw.cmd`), so a separate Maven installation is generally unnecessary.

### Verify your installations

Check Java:

```bash
java -version
```

Check Git:

```bash
git --version
```

Check Docker:

```bash
docker --version
```

The Java version should be compatible with the project's `pom.xml`. Java 24 is the development version used for this setup.

---

## Getting Started

Follow these steps to run the backend on your computer.

### Step 1: Clone the repository

```bash
git clone https://github.com/aabidbilal/OneHealth-Backend.git
```

Move into the project directory:

```bash
cd OneHealth-Backend
```

If the repository is private, you must have access to clone it.

### Step 2: Configure MySQL

Make sure your MySQL server is running.

Open your MySQL client and execute:

```sql
CREATE DATABASE OneHealth_db;
```

Create or choose a dedicated MySQL user for the application and grant it the necessary permissions on `OneHealth_db`.

Keep the database username and password private.

The backend reads its database configuration from environment variables, which are described below.

## MinIO Setup

OneHealth uses **SILO (MinIO-compatible object storage)** to store medical report files.

### 1. Start the storage service

Ensure your SILO container is running and exposes these ports:

- **API:** `http://localhost:9000`
- **Console:** `http://localhost:9001`

Open the console and log in using the credentials configured for your container.

### 2. Create a bucket

Create a bucket named:

```text
onehealth-bucket
```

### 3. Configure environment variables

Set the following variables to match your SILO configuration:

```env
MINIO_ENDPOINT=http://localhost:9000
MINIO_ACCESS_KEY=your_access_key
MINIO_SECRET_KEY=your_secret_key
MINIO_BUCKET_NAME=onehealth-bucket
```

Replace the example credentials with your actual local credentials.

**Note:** The storage service must be running before you start the backend.

### Step 4: Configure environment variables

The application reads its configuration from environment variables rather than hardcoding credentials in `application.properties`.

The required variables are:

```text
DB_URL
DB_USERNAME
DB_PASSWORD
JWT_SECRET
MINIO_ENDPOINT
MINIO_ACCESS_KEY
MINIO_SECRET_KEY
MINIO_BUCKET_NAME
```

For their expected values, see the [Environment Variables](#environment-variables) section.

Do not commit real credentials to GitHub.

### Step 5: Run the application

**Windows — Command Prompt:**

```bat
mvnw.cmd spring-boot:run
```

**Windows — Git Bash, Linux, or macOS:**

```bash
./mvnw spring-boot:run
```

If your environment variables are configured correctly and MySQL and MinIO are accessible, Spring Boot should start.

Watch the console for startup errors. A successful startup message is a useful first indication that the application is running, but API testing is still required.

The application port is determined by the Spring Boot configuration. Unless overridden, Spring Boot uses port `8080`.

---

## Database Configuration

The current `application.properties` uses environment-variable placeholders:

```properties
spring.application.name=demo

spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

jwt.secret=${JWT_SECRET}

minio.endpoint=${MINIO_ENDPOINT}
minio.access-key=${MINIO_ACCESS_KEY}
minio.secret-key=${MINIO_SECRET_KEY}
minio.bucket-name=${MINIO_BUCKET_NAME}

spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=12MB
```

### Database URL

For a local MySQL instance using the database created above:

```text
jdbc:mysql://localhost:3306/OneHealth_db
```

If your MySQL server uses a different host, port, or database name, update the URL accordingly.

### Schema management

The current configuration uses:

```properties
spring.jpa.hibernate.ddl-auto=update
```

Hibernate may update the database schema based on the application's entity mappings.

This is convenient for local development, but it should not be treated as a production database migration strategy. Production deployments should use controlled schema migrations and backups.

---

## MinIO Configuration

MinIO stores the actual medical report files, while MySQL stores structured application data and associated report metadata.

The application expects the following configuration:

| Variable | Example local value |
|---|---|
| `MINIO_ENDPOINT` | `http://localhost:9000` |
| `MINIO_ACCESS_KEY` | `onehealthadmin` |
| `MINIO_SECRET_KEY` | Your configured MinIO password |
| `MINIO_BUCKET_NAME` | `onehealth-bucket` |

The access key and secret key must match the credentials accepted by your MinIO server.

If the application cannot connect to MinIO, verify that:

1. Docker is running.
2. The MinIO container is running.
3. Port `9000` is accessible.
4. The endpoint and credentials are correct.
5. The required bucket exists or is created by the application.

---

## Environment Variables

Configure the environment variables before running the backend.

The following table shows example values for local development. Replace the example database credentials and JWT secret with your own values.

| Variable | Example value | Description |
|---|---|---|
| `DB_URL` | `jdbc:mysql://localhost:3306/OneHealth_db` | MySQL connection URL |
| `DB_USERNAME` | `onehealth_user` | MySQL username |
| `DB_PASSWORD` | Your local database password | MySQL password |
| `JWT_SECRET` | Your generated secret | Secret used for JWT signing |
| `MINIO_ENDPOINT` | `http://localhost:9000` | MinIO API endpoint |
| `MINIO_ACCESS_KEY` | `onehealthadmin` | MinIO access key |
| `MINIO_SECRET_KEY` | Your configured MinIO password | MinIO secret key |
| `MINIO_BUCKET_NAME` | `onehealth-bucket` | Storage bucket name |

### Option A: IntelliJ IDEA

1. Open the project in IntelliJ IDEA.
2. Open **Run → Edit Configurations**.
3. Select the Spring Boot application configuration.
4. Find the environment variables field.
5. Add all required environment variables.
6. Save the configuration and run the application.

Use the same environment-variable names as the table above.

### Option B: Windows PowerShell

You can set variables temporarily in a PowerShell session:

```powershell
$env:DB_URL="jdbc:mysql://localhost:3306/OneHealth_db"
$env:DB_USERNAME="onehealth_user"
$env:DB_PASSWORD="YOUR_LOCAL_DB_PASSWORD"
$env:JWT_SECRET="YOUR_GENERATED_JWT_SECRET"

$env:MINIO_ENDPOINT="http://localhost:9000"
$env:MINIO_ACCESS_KEY="onehealthadmin"
$env:MINIO_SECRET_KEY="YOUR_MINIO_PASSWORD"
$env:MINIO_BUCKET_NAME="onehealth-bucket"
```

Replace the example values with your actual local configuration.

Then run:

```powershell
.\mvnw.cmd spring-boot:run
```

These PowerShell environment variables apply to the current session and its child processes. You must configure them again in a new session.

**Security note:** Never commit your real `.env` files, passwords, JWT secrets, or production credentials.

---

## Running Tests

Run the automated tests using the Maven Wrapper.

### Run the test suite

**Windows:**

```bat
mvnw.cmd test
```

**Linux or macOS:**

```bash
./mvnw test
```

### Compile without running tests

```bash
./mvnw clean compile
```

On Windows Command Prompt, use:

```bat
mvnw.cmd clean compile
```

A successful compilation verifies that the main Java source code compiles. It does not prove that all application features work correctly.

Some integration tests may require MySQL, MinIO, or additional environment variables. Review the test configuration if a test fails because an external service is unavailable.

---

## Project Structure

The backend uses package organization based on application features.

```text
OneHealth-Backend/
│
├── .mvn/
│   └── wrapper/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── in/onehealth/demo/
│   │   │       ├── accessrequest/
│   │   │       │   ├── entity/
│   │   │       │   ├── repository/
│   │   │       │   └── service/
│   │   │       │
│   │   │       ├── auth/
│   │   │       │   ├── controller/
│   │   │       │   ├── dto/
│   │   │       │   └── service/
│   │   │       │
│   │   │       ├── config/
│   │   │       ├── doctor/
│   │   │       ├── dto/
│   │   │       ├── entity/
│   │   │       ├── exception/
│   │   │       ├── patient/
│   │   │       ├── report/
│   │   │       │   ├── controller/
│   │   │       │   ├── dto/
│   │   │       │   ├── entity/
│   │   │       │   ├── repository/
│   │   │       │   ├── service/
│   │   │       │   └── storage/
│   │   │       ├── repository/
│   │   │       ├── security/
│   │   │       └── DemoApplication.java
│   │   │
│   │   └── resources/
│   │       └── application.properties
│   │
│   └── test/
│       └── java/
│
├── .gitattributes
├── .gitignore
├── mvnw
├── mvnw.cmd
├── pom.xml
└── README.md
```

### Package responsibilities

- `accessrequest`: Doctor access requests and access-control entities.
- `auth`: Registration and login APIs, request/response DTOs, and authentication services.
- `config`: Application, security, JWT, and MinIO configuration.
- `doctor`: Doctor-related controllers, services, repositories, DTOs, and entities.
- `patient`: Patient-related controllers, services, repositories, DTOs, and entities.
- `report`: Medical report APIs, report metadata, file validation, and storage integration.
- `security`: User-details and authentication-related security components.
- `exception`: Application exception handling.
- `repository`: Shared repository components.

---

## API Testing

You can use Postman or another HTTP client to test the backend.

Once the application is running, determine the actual API endpoints from the controller mappings in the source code.

A typical testing sequence is:

1. Register a user.
2. Log in using the registered credentials.
3. Obtain the JWT token returned by the login API.
4. Use the token in the authorization header for protected requests.
5. Test the patient and doctor operations.
6. Test report upload and retrieval.
7. Test access-request approval and revocation.
8. Verify that unauthorized requests are rejected.

For protected endpoints, the authorization header generally follows this format when supported by the application's security configuration:

```http
Authorization: Bearer YOUR_JWT_TOKEN
```

Use the request bodies, endpoint paths, and response formats defined by the actual controllers and DTOs. Do not assume that every endpoint follows a generic example.

---

## Troubleshooting

### 1. Database connection refused

**Possible causes:**
- MySQL is not running.
- The database URL or port is incorrect.
- The database does not exist.
- The configured credentials are invalid.

**What to check:**
- Start MySQL.
- Verify `DB_URL`.
- Verify `DB_USERNAME` and `DB_PASSWORD`.
- Confirm that `OneHealth_db` exists.

### 2. MinIO connection refused

**Possible cause:** The backend cannot reach MinIO at the configured endpoint.

**What to check:**

```bash
docker ps
```

If the container exists but is stopped:

```bash
docker start onehealth-minio
```

Verify that the API endpoint is:

```text
http://localhost:9000
```

Also check the MinIO credentials and bucket configuration.

### 3. JWT configuration error

Check that `JWT_SECRET` is set and satisfies the requirements of the JWT library and the application's JWT configuration.

Use a strong, randomly generated secret appropriate for the configured signing algorithm. Do not use a public example secret in a real deployment.

### 4. Missing environment variables

If Spring Boot reports that a property cannot be resolved, verify that the corresponding environment variable exists in the environment used to launch the application.

Variables configured in IntelliJ may not automatically be available in a separate terminal session.

### 5. Maven build failure

Run:

```bash
./mvnw clean compile
```

On Windows Command Prompt:

```bat
mvnw.cmd clean compile
```

Check the first meaningful compilation or dependency-resolution error. Confirm that your Java version is compatible with the project configuration.

### 6. Port already in use

If the application cannot bind to its configured port, another process may already be using it.

Stop the conflicting process or configure a different application port.

---

## Security Considerations

OneHealth handles potentially sensitive healthcare information. Security is therefore an important part of its development.

- Never commit passwords, API keys, or JWT secrets to GitHub.
- Use environment variables or a suitable secrets manager for credentials.
- Restrict access to patient records and medical report files.
- Validate file uploads and enforce reasonable file-size limits.
- Apply authorization checks on every protected operation.
- Use HTTPS and appropriate access controls when deploying the application.
- Back up the database and object storage.
- Avoid using real patient information during development and demonstrations.
- Review logging behavior to avoid exposing sensitive medical or authentication data.

**Important:** This project is under development and should not be assumed to be production-ready or compliant with healthcare regulations without an appropriate security, privacy, and compliance review.

---

## Future Enhancements

Potential future improvements include:

- AI-assisted medical report summarization and explanation.
- Healthcare interoperability using appropriate standards such as HL7 FHIR.
- Integration with India's Ayushman Bharat Digital Mission, subject to the required specifications, eligibility, approvals, and implementation.
- Improved audit logging for medical-record access.
- Comprehensive automated unit and integration testing.
- Production-oriented database migrations.
- Deployment automation and monitoring.

These are potential enhancements, not claims that the features are already implemented.

---

## Contributing

Contributions and suggestions are welcome.

To work on your own copy:

1. Fork or clone the repository, depending on your access.
2. Create a branch for your changes.
3. Implement and test the changes.
4. Commit your work with a descriptive message.
5. Open a pull request if you have permission to contribute to the main repository.

Example:

```bash
git checkout -b feature/your-feature
```

After making changes:

```bash
git add .
git commit -m "Describe your changes"
git push -u origin feature/your-feature
```

---

## License

No license has been specified in this repository yet.

Unless a license is added, others should not assume that they have permission to redistribute or modify the project simply because the repository is publicly accessible.

---

**OneHealth — Building a more connected personal health record experience.**
