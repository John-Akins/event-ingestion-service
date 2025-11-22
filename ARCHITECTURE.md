# Architecture and Infrastructure

This document provides an overview of the architecture and infrastructure for the Event Ingestion Service.

## Application Architecture

The Event Ingestion Service is a **Java-based application** built using the **Spring Boot** framework. It is designed to receive and process events through a REST API.

- **Framework**: Spring Boot 3.5.7
- **Language**: Java 21
- **Database**: The application uses **PostgreSQL** as its primary database for storing event data and relies on an in-memory **H2** database for testing purposes. Database migrations are managed using **Liquibase**.
- **API**: The application exposes **RESTful endpoints** for ingesting events. It includes validation to ensure the integrity of the incoming data.

## Infrastructure

The infrastructure for the Event Ingestion Service is hosted on **Amazon Web Services (AWS)** and is managed using **Terraform**, which allows for an Infrastructure as Code (IaC) approach.

- **Compute**: The application is deployed on a single **Amazon EC2 instance**.
- **Database**: A **PostgreSQL** database is provisioned using **Amazon RDS (Relational Database Service)**.
- **Networking**: An **Application Load Balancer (ALB)** is used to distribute incoming traffic to the EC2 instance. The infrastructure is also configured to work with **Cloudflare**, which provides services such as SSL termination and content delivery (CDN).
- **State Management**: Terraform's state is stored in an **Amazon S3 bucket**, ensuring a persistent and shared state for the infrastructure.

## CI/CD Pipeline

The CI/CD pipeline is managed using **GitHub Actions**, which automates the build, test, and security scanning processes.

- **Continuous Integration**: The `ci.yml` workflow is triggered on every pull request and push to the `main` branch. It performs the following steps:
  - Checks out the code.
  - Sets up the Java environment.
  - Builds the application using Maven.
  - Runs unit tests.
  - Uploads test reports as artifacts if the tests fail.

- **Security Scanning**: The `security.yml` workflow is also triggered on pull requests and runs on a weekly schedule. It performs the following security checks:
  - Runs the **OWASP Dependency-Check** Maven plugin to scan for known vulnerabilities in the project's dependencies.
  - Uploads the security scan report as an artifact for review.

- **Production Deployment**: The `deploy-production.yml` workflow is triggered on every push to the `main` branch. It automates the deployment of the application to the production environment and consists of the following jobs:
  - **Build & Push Image**: This job builds the application and pushes a Docker image to Docker Hub.
  - **Provision Infrastructure**: This job uses Terraform to provision the necessary infrastructure on AWS, including the EC2 instance and RDS database.
  - **Deploy Application**: This job deploys the application to the EC2 instance by running a deployment script. It uses environment variables to configure the database connection and other settings.
