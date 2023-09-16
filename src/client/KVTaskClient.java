package client;

import manager.exception.RequestFailedException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {

    private String url;
    private String apiToken;
    private HttpClient client;

    public KVTaskClient(String url) {
        this.url = url;
        client = HttpClient.newHttpClient();
        apiToken = register();
    }

    private String register() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url + "register"))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return response.body();
            } else {
                throw new RequestFailedException("Не удается выполнить запрос на регистрацию, status code "
                        + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            throw new RequestFailedException("Невозможно выполнить запрос", e);
        }
    }

    public void save(String key, String value) { // key = {task, subtask, epic, history}
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url + "save/" + key + "?API_TOKEN=" + apiToken))
                    .POST(HttpRequest.BodyPublishers.ofString(value))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new RequestFailedException("Не удается выполнить запрос на регистрацию, status code "
                        + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            throw new RequestFailedException("Невозможно выполнить запрос", e);
        }
    }

    public String load(String key) { // возвращает состояние менеджера задач через запрос GET
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url + "load/" + key + "?API_TOKEN=" + apiToken))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return response.body();
            } else {
                throw new RequestFailedException("Не удается выполнить запрос на загрузку, status code "
                        + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            throw new RequestFailedException("Невозможно выполнить запрос", e);
        }
    }
}
