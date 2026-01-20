//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.example;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Method;
import org.jsoup.nodes.Document;

public class Main {
    static String url = "https://jsonplaceholder.typicode.com/users";
    static String json = "{\n  \"id\": 5,\n  \"name\": \"Taras Updated\",\n  \"username\": \"zhoh_updated\",\n  \"email\": \"taras_updated@test.com\"\n}\n";

    public static void main(String[] args) throws Exception {
        printOpenTodos(1);
    }

    public static void MyPost() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        String json = "{\n  \"name\": \"Taras\",\n  \"username\": \"zhoh\",\n  \"email\": \"taras@test.com\"\n}\n";
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).header("Content-Type", "application/json").POST(BodyPublishers.ofString(json)).build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        System.out.println("Status: " + response.statusCode());
        System.out.println((String)response.body());
    }

    public static void MyUpdat() throws IOException {
        Connection.Response response = Jsoup.connect(url).method(Method.PUT).header("Content-Type", "application/json").ignoreContentType(true).ignoreHttpErrors(true).requestBody(json).execute();
        System.out.println("Status: " + response.statusCode());
        System.out.println("Response:");
        System.out.println(response.body());
    }

    public static void MyDelet() throws IOException {
        String url = "https://jsonplaceholder.typicode.com/users/5";
        Connection.Response response = Jsoup.connect(url).method(Method.DELETE).ignoreContentType(true).ignoreHttpErrors(true).execute();
        int status = response.statusCode();
        System.out.println("Status: " + status);
    }

    public static void MyGet() throws IOException {
        Document doc = Jsoup.connect(url).ignoreContentType(true).get();
        System.out.println(doc.text());
    }

    public static void MyGet(int id) throws IOException {
        String url = "https://jsonplaceholder.typicode.com/users/" + id;
        Document doc = Jsoup.connect(url).ignoreContentType(true).get();
        System.out.println(doc.text());
    }

    public static void MyGet(String username) throws IOException {
        String url = "https://jsonplaceholder.typicode.com/users?username=" + URLEncoder.encode(username, StandardCharsets.UTF_8);
        Connection.Response response = Jsoup.connect(url).method(Method.GET).ignoreContentType(true).ignoreHttpErrors(true).execute();
        System.out.println(response.statusCode());
        System.out.println(response.body());
    }

    public static void CommentsToLastPost(int id) throws IOException {
        String url = "https://jsonplaceholder.typicode.com/users/" + id + "/posts";
        Document doc = Jsoup.connect(url).ignoreContentType(true).ignoreHttpErrors(true).get();
        String postsJson = doc.text();
        int maxPostId = -1;
        Pattern pattern = Pattern.compile("\"id\"\\s*:\\s*(\\d+)");
        Matcher matcher = pattern.matcher(postsJson);

        while(matcher.find()) {
            int postId = Integer.parseInt(matcher.group(1));
            if (postId > maxPostId) {
                maxPostId = postId;
            }
        }

        if (maxPostId == -1) {
            System.out.println("У користувача " + id + " немає постів.");
        } else {
            String url2 = "https://jsonplaceholder.typicode.com/posts/" + maxPostId + "/comments";
            Document doc2 = Jsoup.connect(url2).ignoreContentType(true).ignoreHttpErrors(true).get();
            System.out.println(doc2.text());
            String fileName = "user-" + id + "-post-" + maxPostId + "-comments.json";
            Files.writeString(Path.of(fileName), doc2.text(), StandardCharsets.UTF_8);
            System.out.println("Saved to file: " + fileName);
        }
    }

    public static void printOpenTodos(int Id) throws IOException {
        String url = "https://jsonplaceholder.typicode.com/users/" + Id + "/todos";
        Document doc = Jsoup.connect(url).ignoreContentType(true).ignoreHttpErrors(true).get();
        String json = doc.text();
        String[] todos = json.split("\\},\\s*\\{");

        for(String todo : todos) {
            if (todo.contains("\"completed\": false")) {
                System.out.println(todo);
            }
        }

    }
}
