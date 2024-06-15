## Introduction
This project is an agent designed to store chat information from Telegram group channels. It captures user accounts, chat history, and message URLs. When a Telegram bot webhook receives messages from Telegram groups, the agent dynamically creates schemas and stores the messages and user data in a PostgreSQL database.

## Features
- **Dynamic schema creation** in PostgreSQL based on Telegram group or channel data.
- **Automatic storage** of messages and user data.
- **Webhook integration** with the Telegram API.
- Local development and testing using **ngrok**.

## Prerequisites
- Java JDK 17
- Maven
- PostgreSQL
- ngrok (for local testing)

## How to Run

1. **Clone the repository**
    ```bash
    git clone https://github.com/jwei26/ChatHistory.git
    ```

2. **Set up your PostgreSQL database**
    - Set up your PostgreSQL database
    - Create the `central.groups` table manually:
        ```sql
        CREATE TABLE central.groups (
            group_id BIGINT PRIMARY KEY,
            group_name VARCHAR(255),
            data_schema VARCHAR(255)
        );
        ```

3. **Configure application properties**
    - Update your `application.properties` file with your PostgreSQL details:
        ```properties
        spring.datasource.url=jdbc:postgresql://localhost:5432/yourdatabase
        spring.datasource.username=user
        spring.datasource.password=password
        ```

4. **Set up ngrok to expose your local server**
    ```bash
    ngrok http http://localhost:8080
    ```

5. **Set the webhook URL with Telegram API**
    ```bash
    curl -F "url=https://ngrokURL/webhook" https://api.telegram.org/bot<BotToken>/setWebhook
    ```
    
6. **Running the Application**
    - Start the Spring Boot application


## Using the Application
Once the application is running and the webhook is set, any messages sent to your Telegram bot will be captured and stored dynamically into the database. New schemas will be created automatically when you add the bot to new groups.
