# Event Ingestion Service

This repository contains the Event Ingestion Service, a Spring Boot application designed to ingest and process events into an analytics platform.

## Features

*   **Event Ingestion**: Receives event data via a REST API endpoint.
*   **Data Validation**: Ensures incoming event data adheres to defined schemas.
*   **Data Persistence**: Stores event data in a database.
*   **SQS Integration (Planned)**: The service is designed to integrate with AWS SQS for asynchronous event processing, but this functionality is not yet implemented.

## Getting Started

To set up and run the application in a local development environment, please refer to the [DEVELOPMENT.md](DEVELOPMENT.md) file for detailed instructions.

## API Documentation

For a detailed description of the API, including all endpoints, request bodies, and response schemas, please refer to the [openapi.yml](openapi.yml) file.

## Configuration

The application uses environment variables for configuration (e.g., database connection details, AWS credentials). A `.env` file can be used for local development to manage these variables.
