# OneHealth Backend

OneHealth is a Personal Health Record (PHR) platform that helps patients manage their medical records digitally. The backend provides REST APIs for authentication, patient and doctor management, medical report handling, and consent-based access control.

## Tech Stack

- Java and Spring Boot
- Spring Security and JWT
- MySQL, Spring Data JPA, and Hibernate
- MinIO object storage
- Maven

## 1. Prerequisites

Install the following software:

- **Git:** To clone the repository.
- **Java JDK:** Use the version configured in `pom.xml`.
- **MySQL Server:** To store application data.
- **Docker Desktop:** To run MinIO.
- **Postman (optional):** To test APIs.

Verify your installations:

```bash
java -version
git --version
docker --version
```

## 2. Clone the Repository

```bash
git clone https://github.com/aabidbilal/OneHealth-Backend.git
cd OneHealth-Backend
```

## 3. Configure MySQL

Start your MySQL server and execute:

```sql
CREATE DATABASE OneHealth_db;
```

Create or select a MySQL user with permission to access this database.

You will configure your own database credentials in Step 5. Do not use another developer's credentials.

## 4. Set Up MinIO

OneHealth uses MinIO-compatible object storage for medical report files.

Ensure Docker Desktop is installed and running.

### Start the MinIO container

Run the following command in a terminal:

```bash
docker run -d \
  --name onehealth-minio \
  -p 9000:9000 \
  -p 9001:9001 \
  -v onehealth-minio-data:/data \
  -e MINIO_ROOT_USER=onehealthadmin \
  -e MINIO_ROOT_PASSWORD=OneHealthDev2026! \
  quay.io/minio/minio server /data --console-address ":9001"
```

The credentials above are for local development only. Replace them with your own credentials if desired.

**MinIO addresses:**

- API: `http://localhost:9000`
- Web Console: `http://localhost:9001`

Open the Web Console and sign in using the credentials configured for your container.

Create a bucket named:

```text
onehealth-bucket
```

If the container already exists, start it with:

```bash
docker start onehealth-minio
```

Verify that it is running:

```bash
docker ps
```

> If you use a different SILO/MinIO-compatible container image, use that service's installation instructions and credentials. The command above uses the official MinIO container image.

## 5. Configure Environment Variables

The application requires the following environment variables:

| Variable | Example |
|---|---|
| `DB_URL` | `jdbc:mysql://localhost:3306/OneHealth_db` |
| `DB_USERNAME` | Your MySQL username |
| `DB_PASSWORD` | Your MySQL password |
| `JWT_SECRET` | Your own strong JWT signing secret |
| `MINIO_ENDPOINT` | `http://localhost:9000` |
| `MINIO_ACCESS_KEY` | `onehealthadmin` |
| `MINIO_SECRET_KEY` | The password configured for MinIO |
| `MINIO_BUCKET_NAME` | `onehealth-bucket` |

Configure these variables in **IntelliJ IDEA → Run → Edit Configurations → Environment variables**, or in your operating system's environment.

Use the credentials for your own local services. Never commit real passwords or JWT secrets to GitHub.

## 6. Run the Backend

Ensure MySQL and MinIO are running and all environment variables are configured.

**Windows Command Prompt:**

```bat
mvnw.cmd spring-boot:run
```

**Git Bash, Linux, or macOS:**

```bash
./mvnw spring-boot:run
```

The backend uses port `8080` by default unless configured otherwise.

## 7. Verify the Setup

Compile the project:

```bash
./mvnw clean compile
```

Run automated tests:

```bash
./mvnw test
```

On Windows Command Prompt, use `mvnw.cmd` instead of `./mvnw`.

A successful build confirms compilation, but you should also test the APIs to verify the application works correctly.

## Troubleshooting

- **MySQL connection error:** Verify that MySQL is running and your database credentials are correct.
- **MinIO connection error:** Check the container status, API endpoint, credentials, and bucket name.
- **Missing environment variable:** Ensure all eight required variables are configured in the environment used to launch the application.
- **Build failure:** Check your Java version and the Maven error output.

## Security

- Never commit database passwords, JWT secrets, or private environment files.
- Use test data instead of real patient records during development.
- This project is under development and is not necessarily production-ready.
