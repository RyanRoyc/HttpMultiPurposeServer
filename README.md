# Multi-Purpose HTTP Client-Server Program

This Java program consists of a simple HTTP client and server. The server can handle two types of requests:
1. **Chat Messages** - The client can send text messages to the server.
2. **File Uploads** - The client can upload files to the server.

## Components

### Client

The `Client` class provides a command-line interface for users to interact with the server. It offers two main functionalities:
1. **Chat** - Allows users to send messages to the server.
2. **Upload File** - Allows users to upload files to the server.

### Server

The `Server` class sets up an HTTP server that listens for requests on port 8000. It supports:
1. **ChatHandler** - Handles incoming chat messages.
2. **UploadHandler** - Handles file uploads.

## Requirements

- Java Development Kit (JDK) 11 or higher

## Setup

1. Clone the repository or download the source files.

2. Compile the Java files:

    ```sh
    javac Client.java Server.java
    ```

3. Run the server:

    ```sh
    java Server
    ```

4. In a separate terminal, run the client:

    ```sh
    java Client
    ```
