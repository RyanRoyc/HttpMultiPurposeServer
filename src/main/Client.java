package main;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        HttpClient client = HttpClient.newHttpClient();

        while (true) {
            System.out.println("Choose an action: [1] Chat [2] Upload file");
            String choice = scanner.nextLine();

            if ("1".equals(choice)) {
                // Chat functionality
                System.out.print("Enter message: ");
                String message = scanner.nextLine();
                String encodedMessage = URLEncoder.encode(message, StandardCharsets.UTF_8);

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:8000/chat"))
                        .header("Content-Type", "application/x-www-form-urlencoded")
                        .POST(HttpRequest.BodyPublishers.ofString("message=" + encodedMessage))
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                System.out.println("Server response: " + response.body());

            } else if ("2".equals(choice)) {
                // File upload functionality
                System.out.print("Enter file path: ");
                String filePath = scanner.nextLine();

                Path path = Paths.get(filePath);
                if (Files.exists(path)) {
                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create("http://localhost:8000/upload"))
                            .header("Content-Type", "application/octet-stream")
                            .header("File-Name", path.getFileName().toString())
                            .POST(HttpRequest.BodyPublishers.ofFile(path))
                            .build();

                    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                    System.out.println("Server response: " + response.body());
                } else {
                    System.out.println("File not found. Please enter a valid file path.");
                }
            } else {
                System.out.println("Invalid choice. Please enter 1 or 2.");
            }
        }
    }
}
