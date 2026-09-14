# Job Recommendation Engine

A Spring Boot REST API that recommends jobs for candidates and candidates for jobs based on skills, experience, location, and salary expectations.

## Tech Stack

* Java 21
* Spring Boot 4.0.8
* Spring Web MVC
* Spring Data JPA
* PostgreSQL 15
* Maven
* JUnit
* Docker & Docker Compose

## Features

* Create and retrieve candidates
* Create and retrieve jobs
* Get all candidates and jobs
* Candidate → Job recommendations
* Job → Candidate recommendations
* Must-have skill filtering
* Nice-to-have skill scoring
* Experience-based scoring
* Location and remote scoring
* Salary compatibility scoring
* Ranked recommendations
* Configurable scoring weights
* `limit` query parameter
* Dockerized API and PostgreSQL
* Unit tests for scoring rules and edge cases

---

## Running Locally

### Requirements

* Java 21
* PostgreSQL 15 or compatible version
* Maven

Create a PostgreSQL database with:

```text
Database: job_match
Username: jobmatch_user
Password: jobmatch_pass
Port: 5432
```

Run the application:

```bash
./mvnw spring-boot:run
```

For Windows:

```bash
mvnw.cmd spring-boot:run
```

API:

```text
http://localhost:8080/api
```

---

## Running with Docker

### Requirement

* Docker Desktop installed and running

From the project root:

```bash
docker compose up --build
```

This starts:

* Spring Boot API on port `8080`
* PostgreSQL 15 on port `5432`

API:

```text
http://localhost:8080/api
```

Stop the containers:

```bash
docker compose down
```

PostgreSQL data is stored in a Docker volume.

---

# API Endpoints

All endpoints use the `/api` context path.

## Candidates

### Create Candidate

```http
POST /api/candidates
```

Example:

```json
{
  "name": "Ayush Sharma",
  "skills": [
    "Java",
    "Spring Boot",
    "PostgreSQL",
    "React"
  ],
  "yearsOfExperience": 2.5,
  "location": "Bangalore",
  "expectedSalary": 800000
}
```

### Get Candidate

```http
GET /api/candidates/{id}
```

### Get All Candidates

```http
GET /api/candidates
```

---

## Jobs

### Create Job

```http
POST /api/jobs
```

Example:

```json
{
  "title": "Backend Developer",
  "requiredSkills": [
    {
      "skill": "Java",
      "mustHave": true
    },
    {
      "skill": "Spring Boot",
      "mustHave": true
    },
    {
      "skill": "PostgreSQL",
      "mustHave": false
    }
  ],
  "minYearsExperience": 2.0,
  "location": "Bangalore",
  "salaryMin": 700000,
  "salaryMax": 1200000,
  "remoteAllowed": true
}
```

### Get Job

```http
GET /api/jobs/{id}
```

### Get All Jobs

```http
GET /api/jobs
```

---

# Recommendation APIs

## Candidate → Jobs

```http
GET /api/candidates/{candidateId}/recommendations
```

Limit results:

```http
GET /api/candidates/{candidateId}/recommendations?limit=3
```

Custom weights:

```http
GET /api/candidates/{candidateId}/recommendations?skillWeight=70&experienceWeight=10&locationWeight=10&salaryWeight=10
```

## Job → Candidates

```http
GET /api/jobs/{jobId}/recommendations
```

Limit results:

```http
GET /api/jobs/{jobId}/recommendations?limit=3
```

Custom weights:

```http
GET /api/jobs/{jobId}/recommendations?skillWeight=70&experienceWeight=10&locationWeight=10&salaryWeight=10
```

---

# Scoring Logic

The default scoring weights are:

| Factor     |  Weight |
| ---------- | ------: |
| Skills     |      50 |
| Experience |      20 |
| Location   |      15 |
| Salary     |      15 |
| **Total**  | **100** |

Final score:

```text
Total Score =
Skill Score
+ Experience Score
+ Location Score
+ Salary Score
```

The recommendation response contains the total score and individual score breakdown.

## Skills

### Must-have

Missing any must-have skill is a **hard filter**. The candidate/job pair is excluded.

### Nice-to-have

Nice-to-have skills improve the score but do not filter the candidate/job.

The skill weight is divided approximately:

```text
70% → Must-have skills
30% → Nice-to-have skills
```

This ensures that a candidate who has the required skills receives a meaningful skill score even without nice-to-have skills.

## Experience

Meeting or exceeding the minimum experience gives the full experience score.

Candidates below the required experience are **not excluded**. Their score is reduced proportionally:

```text
Experience Score =
Candidate Experience / Required Experience
× Experience Weight
```

## Location

```text
Exact location match → Full score
Different location + remote allowed → Partial score
Different location + remote not allowed → 0
```

With the default location weight:

```text
Exact match → 15
Remote mismatch → 9
Non-remote mismatch → 0
```

## Salary

* Expected salary above the job's maximum → salary score `0`
* Expected salary within the job range → score based on salary overlap
* Better salary compatibility → higher score

---

# Configurable Weights

The recommendation APIs support:

```text
skillWeight
experienceWeight
locationWeight
salaryWeight
```

Example:

```http
GET /api/candidates/{candidateId}/recommendations?skillWeight=70&experienceWeight=10&locationWeight=10&salaryWeight=10
```

The weights do not have to total 100. The total score is the sum of the supplied component weights.

---

# Testing

JUnit tests cover important scoring rules and edge cases, including:

* Missing must-have skill
* Experience penalty
* Strong/perfect match
* Salary mismatch
* Remote location
* Custom weights
* Default weights
* Zero skill weight

Run the tests:

```bash
./mvnw test
```

For Windows:

```bash
mvnw.cmd test
```

---

# Design Decisions

A rule-based scoring system was used because the assignment provides explicit business rules and does not require training data.

The scoring logic is separated into `ScoringService`, while recommendation filtering, ranking, and limiting are handled by `RecommendationService`.

This keeps the business logic easy to understand, test, and modify.

---

# Assumptions

* Skills are compared case-insensitively.
* Must-have skills are mandatory.
* Nice-to-have skills only affect ranking.
* Candidates below the experience requirement are penalized rather than excluded.
* Exact location matches receive the highest location score.
* Remote jobs receive partial location credit when locations differ.
* Expected salary above the job maximum receives a salary score of `0`.
* Custom scoring weights can be supplied through query parameters.
* Default weights are `50/20/15/15`.
* PostgreSQL is used for persistence.
* Authentication and authorization are outside the scope of this assignment.

---

# Future Improvements

* Pagination
* Improved validation and error handling
* OpenAPI/Swagger documentation
* Integration tests using Testcontainers
* External configuration for scoring weights
* Recommendation caching
* Frontend application
* ML-based ranking using historical recommendation data
* Authentication and authorization

---

# AI Usage

AI tools were used as a development assistant for:

* Discussing implementation approaches
* Reviewing scoring logic
* Identifying edge cases
* Creating test scenarios
* Docker configuration
* Documentation

The implementation was reviewed and tested during development.

---

# Out of Scope

* Authentication
* Authorization
* Frontend/UI
* Machine-learning recommendation models
* Job application management
* Notifications

---

# Quick Start with Docker

The easiest way to run the complete application:

```bash
docker compose up --build
```

Then access:

```text
http://localhost:8080/api
```

Stop the application:

```bash
docker compose down
```
