# Development

This document provides instructions for setting up and running the application in a local development environment.

## Running the Application with Docker

To run the application locally using Docker, you will need to have Docker and Docker Compose installed.

### Building the Docker Image

First, build the Docker image using the following command from the root of the project:

```bash
docker build -t event-ingestion-service .
```

### Running with Docker Compose

Once the image is built, you can run the application using Docker Compose. The `docker-compose.yml` file is configured to use environment variables for the database connection.

1.  **Set Environment Variables**:
    You will need to set the following environment variables. You can either export them in your shell or create a `.env` file in the root of the project.

    ```bash
    export DOCKER_IMAGE=event-ingestion-service
    export SPRING_PROFILES_ACTIVE=dev
    export DB_HOSTNAME=<your_database_host>
    export DB_PORT=<your_database_port>
    export DB_NAME=<your_database_name>
    export DB_USERNAME=<your_database_username>
    export DB_PASSWORD=<your_database_password>
    ```

2.  **Run Docker Compose**:
    With the environment variables set, you can now start the application:

    ```bash
    docker-compose up
    ```

The application will be accessible at `http://localhost:8085`.

**Note:** Database tables will be automatically created upon application startup due to the Flyway migrations configured in the project.

## Testing the API

To test the event ingestion endpoint, you can use the sample request data provided in `src/main/resources/data/sample_events.json`. This file contains examples of valid event payloads that can be sent to the `/ingest` endpoint.

### Using `sample_events.json` with Postman

1.  **Open Postman**: Launch the Postman application.
2.  **Create a New Request**: Click on `+` to create a new request.
3.  **Set Request Method**: Change the method to `POST`.
4.  **Set Request URL**: Enter the endpoint URL, e.g., `http://localhost:8085/ingest`.
5.  **Set Headers**: Add a header: `Content-Type: application/json`.
6.  **Set Request Body**:
    *   Select the `Body` tab.
    *   Choose the `raw` radio button and select `JSON` from the dropdown.
    *   Open `src/main/resources/data/sample_events.json` in your editor.
    *   Copy the content of `sample_events.json`.
    *   Paste the copied JSON content into the body section of your Postman request.
7.  **Send Request**: Click the `Send` button to make the request.

For a detailed description of the API, including all endpoints, request bodies, and response schemas, please refer to the `openapi.yml` file.
