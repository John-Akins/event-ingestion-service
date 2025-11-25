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

## Code Style and Linting

This project uses the [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html). To ensure consistency, we use Checkstyle for automated linting, which displays style violations directly in your IDE. Once configured, Checkstyle runs automatically during development, highlighting issues in the editor for immediate correction.

### Setting up in your Editor

Here are the instructions for setting up linting in popular editors.

#### IntelliJ IDEA

1.  **Install Plugins:** Go to `File -> Settings -> Plugins`. Search for and install "Checkstyle-IDEA" and "google-java-format" (by Google) from the Marketplace.

2.  **Configure Code Style (Formatting):** After installing google-java-format plugin, restart IDEA. The plugin will automatically handle formatting to Google Java Style.

3.  **Configure Checkstyle:**
    *   Go to `File -> Settings -> Tools -> Checkstyle`.
    *   In the "Configuration File" panel, click the `+` icon to add a new configuration.
    *   Set the "Description" to "Google Java Style".
    *   Click "Next" and "Finish".

4.  **Activate Profile:** Make sure the "Google Java Style" profile is checked in the list of configuration files.

5.  **View Errors:** You can now run Checkstyle by opening the "Checkstyle" tool window (`View -> Tool Windows -> Checkstyle`) and running a scan. Errors will also be highlighted in the editor.

#### Visual Studio Code

1.  **Install Extension:** Install the [Checkstyle for Java](https://marketplace.visualstudio.com/items?itemName=shengchen.vscode-checkstyle) extension from the Visual Studio Marketplace.

2.  **Configure Checkstyle:** Open your VS Code settings (`settings.json`). You can do this by pressing `Ctrl+Shift+P` (or `Cmd+Shift+P` on Mac) and typing "Open User Settings (JSON)". Add the following configuration:

    ```json
    "java.checkstyle.configuration": "google_checks.xml",
    "java.checkstyle.version": "10.12.0" // Use a recent version of Checkstyle
    ```
    *Note: You may need to adjust the Checkstyle version. The extension will prompt you if a different version is needed.*

3.  **Configure Code Formatting:** To enable automatic code formatting to Google Java Style, add the following to your VS Code settings (`settings.json`):

    ```json
    "java.format.settings.url": "https://raw.githubusercontent.com/google/styleguide/gh-pages/intellij-java-google-style.xml",
    "java.format.settings.profile": "GoogleStyle"
    ```

4.  **Enable Checkstyle and Formatting:** After configuring, you might need to reload VS Code. Checkstyle will now run automatically on your Java files, and formatting will be applied when you save files or format on demand.
