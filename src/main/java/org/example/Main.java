package org.example;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Scanner;

public class Main {
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final String LINE_API_URL = "https://notify-api.line.me/api/notify";

    private static final String TELEGRAM_BOT_TOKEN = "";
    private static final String TELEGRAM_BOT_API = "https://api.telegram.org/bot" + TELEGRAM_BOT_TOKEN + "/sendMessage";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("========= Send Notification =========");
        System.out.println("Click 1: Send Line Notification");
        System.out.println("Click 2: Send Telegram Notification");
        System.out.println("Click 3: Send Email Notification");
        int choice = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter Message: ");
        String message = sc.nextLine(); // Accept user input

        try {
            if (choice == 1) {
                line_noti(message);
            } else if (choice == 2) {
                telegram_noti(message);
            } else if (choice == 3) {
                new Email().sendMail("A new message", """
                Dear reader,
                                
                """ + message + """
                                
                Best regards,
                myself
                """);
            } else {
                System.out.println("Invalid choice. Please select 1, 2, or 3.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void line_noti(String message) throws IOException, InterruptedException {
        String token = "";

        // LINE Notify API expects form-urlencoded content, not JSON
        String formData = "message=" + message;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(LINE_API_URL))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization", "Bearer " + token)
                .POST(HttpRequest.BodyPublishers.ofString(formData))
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        System.out.println("Status code: " + response.statusCode());
        System.out.println("Response body: " + response.body());
    }

    public static void telegram_noti(String message) throws IOException, InterruptedException {
        String group_id = "-4604108610";

        String jsonbody = "{\n" +
                "  \"chat_id\": \"" + group_id + "\",\n" +
                "  \"text\": \"" + message + "\"\n" +
                "}\n";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(TELEGRAM_BOT_API))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonbody))
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        System.out.println(response);
    }
}
