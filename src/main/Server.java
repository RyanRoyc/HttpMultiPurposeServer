package main;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import com.sun.net.httpserver.*;

public class Server {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/chat", new ChatHandler());
        server.createContext("/upload", new UploadHandler());
        server.setExecutor(null);
        server.start();
    }

    static class ChatHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String clientAddress = exchange.getRemoteAddress().toString();
            if ("POST".equals(exchange.getRequestMethod())) {
                // Read and decode the message from the request body
                String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                String decodedMessage = URLDecoder.decode(requestBody.substring("message=".length()), StandardCharsets.UTF_8.name());

                System.out.println("Client at " + clientAddress + " says: " + decodedMessage);

                // Respond to the client
                String response = "Server received: " + decodedMessage;
                exchange.sendResponseHeaders(200, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } else {
                // Handle other HTTP Methods e.g. GET, if needed
                String clientMessage = exchange.getRequestURI().getQuery();
                if (clientMessage != null) {
                    clientMessage = URLDecoder.decode(clientMessage, StandardCharsets.UTF_8.name());
                }

                System.out.println("Client at " + clientAddress + " says: " + clientMessage);

                String response = "Server received: " + clientMessage;
                exchange.sendResponseHeaders(200, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        }
    }

    static class UploadHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String clientAddress = exchange.getRemoteAddress().toString();
            String fileName = exchange.getRequestHeaders().getFirst("File-Name");

            System.out.println("Client at " + clientAddress + " sent file: " + fileName);

            InputStream requestBody = exchange.getRequestBody();

            // Determine if the file is text or binary
            boolean isTextFile = isTextFile(fileName);

            byte[] fileBytes = readUploadedFile(requestBody);
            String fileContent = isTextFile ? new String(fileBytes) : convertBytesToString(fileBytes);

            saveFile(fileBytes, fileName);

            String response = "Server received and processed the uploaded file from client at " + clientAddress +
                    "\nFile name: " + fileName + "\nFile content:\n" + fileContent;
            exchange.sendResponseHeaders(200, response.length());
            exchange.getResponseBody().write(response.getBytes());
            exchange.getResponseBody().close();
        }

        private boolean isTextFile(String fileName) {
            // Determine if the file is a text file based on its extension
            return fileName.toLowerCase().endsWith(".txt") || fileName.toLowerCase().endsWith(".html")
                    || fileName.toLowerCase().endsWith(".xml") || fileName.toLowerCase().endsWith(".java");
        }

        private byte[] readUploadedFile(InputStream inputStream) throws IOException {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }
            return byteArrayOutputStream.toByteArray();
        }

        private String convertBytesToString(byte[] fileBytes) {
            return java.util.Base64.getEncoder().encodeToString(fileBytes);
        }

        private void saveFile(byte[] fileBytes, String fileName) throws IOException {
            String filePath = "C:\\Java Tutorial\\RyanProject\\HttpMultiPurposeServer\\StoredFiles\\" + fileName;
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                fos.write(fileBytes);
            }
        }
    }
}
