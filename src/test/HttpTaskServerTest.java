package test;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.HttpTaskServer;
import server.KVServer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import static model.TaskStatus.IN_PROGRESS;
import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskServerTest {
    Gson gson = new Gson();
    HttpTaskServer taskServer;
    HttpClient client;
    private KVServer kvServer;

    @BeforeEach
    public void setUp() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        client = HttpClient.newHttpClient();
        taskServer = new HttpTaskServer();
        taskServer.start();
        String url = "http://localhost:8078/";
    }

    @AfterEach
    public void stop() {
        kvServer.stop();
        taskServer.stop();
    }

    @Test
    void checkCreateTask() throws IOException, InterruptedException {
        Task task = new Task("Тест создание Task", "Тест добавить новое описание Task");

        URI url = URI.create("http://localhost:8080/tasks/task/");
        Gson gson = new Gson();
        String json = gson.toJson(task);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Task actualTask = gson.fromJson(response.body(), Task.class);

        task.setId(actualTask.getId());
        assertEquals(task, actualTask);
    }

    @Test
    void checkGetTask() throws IOException, InterruptedException {
        Task task = new Task("Тест получение Task", "Тест описание для получения Task");
        task.setId(gson.fromJson(
                client.send(HttpRequest.newBuilder()
                                .uri(URI.create("http://localhost:8080/tasks/task/"))
                                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task))).build(),
                        HttpResponse.BodyHandlers.ofString()).body(),
                Task.class
        ).getId());

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/?id=" + task.getId());
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Task actualTask = gson.fromJson(response.body(), Task.class);

        assertEquals(task, actualTask);
    }

    @Test
    void checkGetAllTask() throws IOException, InterruptedException {
        Task task_1 = new Task("Тест получение AllTask", "Тест описание для получения AllTask");
        Task task_2 = new Task("Тест получение AllTask", "Тест описание для получения AllTask");
        task_1.setId(gson.fromJson(
                client.send(HttpRequest.newBuilder()
                                .uri(URI.create("http://localhost:8080/tasks/task/"))
                                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task_1))).build(),
                        HttpResponse.BodyHandlers.ofString()).body(),
                Task.class
        ).getId());

        task_2.setId(gson.fromJson(
                client.send(HttpRequest.newBuilder()
                                .uri(URI.create("http://localhost:8080/tasks/task/"))
                                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task_2))).build(),
                        HttpResponse.BodyHandlers.ofString()).body(),
                Task.class
        ).getId());

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Type taskType = new TypeToken<ArrayList<Task>>() {
        }.getType();

        ArrayList<Task> loadTask = gson.fromJson(response.body(), taskType);

        assertEquals(loadTask, List.of(task_1, task_2));
    }

    @Test
    void checkUpdateTaskStatusInProgress() throws IOException, InterruptedException {
        Task task = new Task("Тест обновление Task для IN_PROGRESS", "Тест описание для" +
                "обновления Task для IN_PROGRESS Task");
        task.setId(gson.fromJson(
                client.send(HttpRequest.newBuilder()
                                .uri(URI.create("http://localhost:8080/tasks/task/"))
                                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task))).build(),
                        HttpResponse.BodyHandlers.ofString()).body(),
                Task.class
        ).getId());

        task.setStatus(IN_PROGRESS);

        URI url = URI.create("http://localhost:8080/tasks/task/");
        Gson gson = new Gson();
        String json = gson.toJson(task);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Task actualTask = gson.fromJson(response.body(), Task.class);

        assertEquals(task, actualTask);
    }


    @Test
    void checkDellTask() throws IOException, InterruptedException {
        Task task = new Task("Тест удаление Task", "Описание Task");
        task.setId(gson.fromJson(
                client.send(HttpRequest.newBuilder()
                                .uri(URI.create("http://localhost:8080/tasks/task/"))
                                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task))).build(),
                        HttpResponse.BodyHandlers.ofString()).body(),
                Task.class
        ).getId());

        URI url = URI.create("http://localhost:8080/tasks/task/?id=" + task.getId());
        Gson gson = new Gson();
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.statusCode(), 200);

        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Task actualTask = gson.fromJson(response.body(), Task.class);

        assertNull(actualTask);
    }

    @Test
    void checkDellAllTask() throws IOException, InterruptedException {
        Task task_1 = new Task("Тест удаление Task", "Описание Task");
        Task task_2 = new Task("Тест удаление Task", "Описание Task");
        task_1.setId(gson.fromJson(
                client.send(HttpRequest.newBuilder()
                                .uri(URI.create("http://localhost:8080/tasks/task/"))
                                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task_1))).build(),
                        HttpResponse.BodyHandlers.ofString()).body(),
                Task.class
        ).getId());

        task_2.setId(gson.fromJson(
                client.send(HttpRequest.newBuilder()
                                .uri(URI.create("http://localhost:8080/tasks/task/"))
                                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task_2))).build(),
                        HttpResponse.BodyHandlers.ofString()).body(),
                Task.class
        ).getId());

        URI url = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.statusCode(), 200);

        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Type taskType = new TypeToken<ArrayList<Task>>() {
        }.getType();
        ArrayList<Task> loadTask = gson.fromJson(response.body(), taskType);

        assertTrue(loadTask.isEmpty());
    }

    @Test
    void checkCreateEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Тест создание Epic", "Тест добавить новое описание Epic");

        URI url = URI.create("http://localhost:8080/tasks/epic/");
        Gson gson = new Gson();
        String json = gson.toJson(epic);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Epic actualEpic = gson.fromJson(response.body(), Epic.class);

        epic.setId(actualEpic.getId());
        assertEquals(epic, actualEpic);
    }


    @Test
    void checkGetEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Тест получение Epic", "Тест описание для получения Epic");
        epic.setId(gson.fromJson(
                client.send(HttpRequest.newBuilder()
                                .uri(URI.create("http://localhost:8080/tasks/epic/"))
                                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic))).build(),
                        HttpResponse.BodyHandlers.ofString()).body(),
                Epic.class
        ).getId());

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/?id=" + epic.getId());
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Epic actualEpic = gson.fromJson(response.body(), Epic.class);

        assertEquals(epic, actualEpic);
    }

    @Test
    void checkGetAllEpic() throws IOException, InterruptedException {
        Epic epic_1 = new Epic("Тест получить Epic", "Тест получения Epic");
        Epic epic_2 = new Epic("Тест получить Epic", "Тест получения Epic");
        epic_1.setId(gson.fromJson(
                client.send(HttpRequest.newBuilder()
                                .uri(URI.create("http://localhost:8080/tasks/epic/"))
                                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic_1))).build(),
                        HttpResponse.BodyHandlers.ofString()).body(),
                Task.class
        ).getId());

        epic_2.setId(gson.fromJson(
                client.send(HttpRequest.newBuilder()
                                .uri(URI.create("http://localhost:8080/tasks/epic/"))
                                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic_2))).build(),
                        HttpResponse.BodyHandlers.ofString()).body(),
                Task.class
        ).getId());

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Type epicType = new TypeToken<ArrayList<Epic>>() {
        }.getType();

        ArrayList<Epic> loadEpic = gson.fromJson(response.body(), epicType);

        assertEquals(loadEpic, List.of(epic_1, epic_2));
    }

    @Test
    void checkDellEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Тест удаление Epic", "Описание Epic");
        epic.setId(gson.fromJson(
                client.send(HttpRequest.newBuilder()
                                .uri(URI.create("http://localhost:8080/tasks/task/"))
                                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic))).build(),
                        HttpResponse.BodyHandlers.ofString()).body(),
                Task.class
        ).getId());

        URI url = URI.create("http://localhost:8080/tasks/epic/?id=" + epic.getId());
        Gson gson = new Gson();
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.statusCode(), 200);

        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Epic actualEpic = gson.fromJson(response.body(), Epic.class);

        assertNull(actualEpic);
    }

    @Test
    void checkDellAllEpic() throws IOException, InterruptedException {
        Epic epic_1 = new Epic("Тест удаление Epic_1", "Описание Epic_1");
        Epic epic_2 = new Epic("Тест удаление Epic_2", "Описание Epic_2");
        epic_1.setId(gson.fromJson(
                client.send(HttpRequest.newBuilder()
                                .uri(URI.create("http://localhost:8080/tasks/epic/"))
                                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic_1))).build(),
                        HttpResponse.BodyHandlers.ofString()).body(),
                Task.class
        ).getId());

        epic_2.setId(gson.fromJson(
                client.send(HttpRequest.newBuilder()
                                .uri(URI.create("http://localhost:8080/tasks/epic/"))
                                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic_2))).build(),
                        HttpResponse.BodyHandlers.ofString()).body(),
                Task.class
        ).getId());

        URI url = URI.create("http://localhost:8080/tasks/epic/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.statusCode(), 200);

        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Type epicType = new TypeToken<ArrayList<Epic>>() {
        }.getType();
        ArrayList<Epic> loadEpic = gson.fromJson(response.body(), epicType);

        assertTrue(loadEpic.isEmpty());
    }

    @Test
    void checkCreateSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic("Тест Epic для создания Subtask",
                "Тест описание Epic для создания Subtask");
        epic.setId(gson.fromJson(
                client.send(HttpRequest.newBuilder()
                                .uri(URI.create("http://localhost:8080/tasks/epic/"))
                                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic))).build(),
                        HttpResponse.BodyHandlers.ofString()).body(),
                Epic.class
        ).getId());

        Subtask subtask = new Subtask("Тест создание Subtask", "Тест описание Subtask",
                epic.getId());
        URI url = URI.create("http://localhost:8080/tasks/subtask/");
        Gson gson = new Gson();
        String json = gson.toJson(subtask);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Subtask actualSubtask= gson.fromJson(response.body(), Subtask.class);

        subtask.setId(actualSubtask.getId());
        assertEquals(subtask, actualSubtask);
    }

    @Test
    void checkGetSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic("Тест Epic для создания Subtask",
                "Тест описание Epic для создания Subtask");
        epic.setId(gson.fromJson(
                client.send(HttpRequest.newBuilder()
                                .uri(URI.create("http://localhost:8080/tasks/epic/"))
                                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic))).build(),
                        HttpResponse.BodyHandlers.ofString()).body(),
                Epic.class
        ).getId());

        Subtask subtask = new Subtask("Тест получение Subtask", "Тест описание для получения " +
                "Subtask", epic.getId());
        subtask.setId(gson.fromJson(
                client.send(HttpRequest.newBuilder()
                                .uri(URI.create("http://localhost:8080/tasks/subtask/"))
                                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subtask))).build(),
                        HttpResponse.BodyHandlers.ofString()).body(),
                Subtask.class
        ).getId());

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/?id=" + subtask.getId());
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Subtask actualSubtask = gson.fromJson(response.body(), Subtask.class);

        assertEquals(subtask, actualSubtask);
    }

    @Test
    void checkGetAllSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic("Тест Epic для создания Subtask",
                "Тест описание Epic для создания Subtask");
        epic.setId(gson.fromJson(
                client.send(HttpRequest.newBuilder()
                                .uri(URI.create("http://localhost:8080/tasks/epic/"))
                                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic))).build(),
                        HttpResponse.BodyHandlers.ofString()).body(),
                Epic.class
        ).getId());

        Subtask subtask_1 = new Subtask("Тест получение AllSubtask", "Тест описание для " +
                "получения AllSubtask", epic.getId());
        Subtask subtask_2 = new Subtask("Тест получение AllSubtask", "Тест описание для " +
                "получения AllSubtask", epic.getId());
        subtask_1.setId(gson.fromJson(
                client.send(HttpRequest.newBuilder()
                                .uri(URI.create("http://localhost:8080/tasks/subtask/"))
                                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subtask_1))).build(),
                        HttpResponse.BodyHandlers.ofString()).body(),
                Subtask.class
        ).getId());
        subtask_2.setId(gson.fromJson(
                client.send(HttpRequest.newBuilder()
                                .uri(URI.create("http://localhost:8080/tasks/subtask/"))
                                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subtask_2))).build(),
                        HttpResponse.BodyHandlers.ofString()).body(),
                Subtask.class
        ).getId());

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Type subtaskType = new TypeToken<ArrayList<Subtask>>() {
        }.getType();

        ArrayList<Subtask> loadSubtask = gson.fromJson(response.body(), subtaskType);

        assertEquals(loadSubtask, List.of(subtask_1, subtask_2));
    }

    @Test
    void checkUpdateSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic("Тест Epic для создания Subtask",
                "Тест описание Epic для создания Subtask");
        epic.setId(gson.fromJson(
                client.send(HttpRequest.newBuilder()
                                .uri(URI.create("http://localhost:8080/tasks/epic/"))
                                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic))).build(),
                        HttpResponse.BodyHandlers.ofString()).body(),
                Epic.class
        ).getId());

        Subtask subtask = new Subtask("Тест обновление Subtask", "Тест описание для обновления " +
                "Subtask", epic.getId());

        subtask.setId(gson.fromJson(
                client.send(HttpRequest.newBuilder()
                                .uri(URI.create("http://localhost:8080/tasks/subtask/"))
                                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subtask))).build(),
                        HttpResponse.BodyHandlers.ofString()).body(),
                Subtask.class
        ).getId());

        subtask.setStatus(IN_PROGRESS);

        URI url = URI.create("http://localhost:8080/tasks/subtask/");
        Gson gson = new Gson();
        String json = gson.toJson(subtask);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Subtask actualSubtask = gson.fromJson(response.body(), Subtask.class);

        assertEquals(subtask, actualSubtask);
    }

    @Test
    void checkDellSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic("Тест удаление AllSubtask", "Описание Epic");
        epic.setId(gson.fromJson(
                client.send(HttpRequest.newBuilder()
                                .uri(URI.create("http://localhost:8080/tasks/epic/"))
                                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic))).build(),
                        HttpResponse.BodyHandlers.ofString()).body(),
                Epic.class
        ).getId());

        Subtask subtask = new Subtask("Тест удаление Subtask", "Описание Subtask",
                epic.getId());
        subtask.setId(gson.fromJson(
                client.send(HttpRequest.newBuilder()
                                .uri(URI.create("http://localhost:8080/tasks/subtask/"))
                                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subtask))).build(),
                        HttpResponse.BodyHandlers.ofString()).body(),
                Subtask.class
        ).getId());

        URI url = URI.create("http://localhost:8080/tasks/subtask/?id=" + subtask.getId());
        Gson gson = new Gson();
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.statusCode(), 200);

        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Subtask actualSubtask = gson.fromJson(response.body(), Subtask.class);

        assertNull(actualSubtask);


    }

    @Test
    void checkDellAllSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic("Тест удаление AllSubtask", "Описание Epic");
        epic.setId(gson.fromJson(
                client.send(HttpRequest.newBuilder()
                                .uri(URI.create("http://localhost:8080/tasks/epic/"))
                                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic))).build(),
                        HttpResponse.BodyHandlers.ofString()).body(),
                Epic.class
        ).getId());

        Subtask subtask_1 = new Subtask("Тест удаление AllSubtask", "Описание Subtask_1",
                epic.getId());
        Subtask subtask_2 = new Subtask("Тест удаление AllSubtask", "Описание Subtask_2",
                epic.getId());
        subtask_1.setId(gson.fromJson(
                client.send(HttpRequest.newBuilder()
                                .uri(URI.create("http://localhost:8080/tasks/subtask/"))
                                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subtask_1))).build(),
                        HttpResponse.BodyHandlers.ofString()).body(),
                Subtask.class
        ).getId());
        subtask_2.setId(gson.fromJson(
                client.send(HttpRequest.newBuilder()
                                .uri(URI.create("http://localhost:8080/tasks/subtask/"))
                                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subtask_2))).build(),
                        HttpResponse.BodyHandlers.ofString()).body(),
                Subtask.class
        ).getId());

        URI url = URI.create("http://localhost:8080/tasks/subtask/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.statusCode(), 200);

        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Type epicSubtask = new TypeToken<ArrayList<Subtask>>() {
        }.getType();
        ArrayList<Subtask> loadSubtask = gson.fromJson(response.body(), epicSubtask);

        assertTrue(loadSubtask.isEmpty());
    }
}
