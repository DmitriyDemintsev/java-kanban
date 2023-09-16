package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import manager.Managers;
import manager.TaskManager;
import model.Epic;
import model.Subtask;
import model.Task;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

public class HttpTaskServer {

    public static final int PORT = 8080;
    private final HttpServer server;
    private final TaskManager taskManager;
    private final Gson gson = new Gson();

    public HttpTaskServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/tasks", this::handler);
        this.taskManager = Managers.getDefault(); // объекты InMemoryTaskManager
    }

    private void handler(HttpExchange httpExchange) {
        try {
            System.out.println("Processing /task end-point " + httpExchange.getRequestURI());
            String path = httpExchange.getRequestURI().getPath().replaceFirst("/tasks", ""); // получаем путь, по которому отправлен запрос
            String query = httpExchange.getRequestURI().getQuery();
            String method = httpExchange.getRequestMethod(); //получаем метод http-запроса
            String [] splitStrings  = path.split("/");

            if (splitStrings.length == 1) {
                switch (method) {
                    case "GET": {
                        String response = gson.toJson(taskManager.getPrioritizedTasks());
                        sendText(httpExchange, response);
                        break;
                    }
                }
            }

            if (splitStrings.length == 2 && query == null) {
                switch (method) {
                    case "DELETE": {
                        switch (splitStrings[1]) {
                            case "task": {
                                taskManager.dellAllTasks();
                                System.out.println("Все задачи успешно удалены");
                                httpExchange.sendResponseHeaders(200, 0);
                                break;
                            }
                            case "subtask": {
                                taskManager.dellAllSubtasks();
                                System.out.println("Все задачи успешно удалены");
                                httpExchange.sendResponseHeaders(200, 0);
                                break;
                            }
                            case "epic": {
                                taskManager.dellAllEpics();
                                System.out.println("Все задачи успешно удалены");
                                httpExchange.sendResponseHeaders(200, 0);
                                break;
                            }
                        }
                        break;
                    }
                    case "GET": {
                        switch (splitStrings[1]) {
                            case "history": {
                                String response = gson.toJson(taskManager.getHistory());
                                sendText(httpExchange, response);
                                break;
                            }
                            case "task": {
                                String response = gson.toJson(taskManager.getAllTasks());
                                sendText(httpExchange, response);
                                break;
                            }
                            case "subtask": {
                                String response = gson.toJson(taskManager.getAllSubtasks());
                                sendText(httpExchange, response);
                                break;
                            }
                            case "epic": {
                                String response = gson.toJson(taskManager.getAllEpics());
                                sendText(httpExchange, response);
                                break;
                            }
                        }
                        break;
                    }
                    case "POST": {
                        switch (splitStrings[1]) {
                            case "task": {
                                InputStream inputStream = httpExchange.getRequestBody();
                                String body = new String(inputStream.readAllBytes());
                                Task task = gson.fromJson(body, Task.class);
                                int taskId = task.getId();
                                Task control = taskManager.getTask(taskId);

                                if (control != null) {
                                    taskManager.updateTask(task);
                                    String response = gson.toJson(taskManager.getTask(taskId));
                                    sendText(httpExchange, response);
                                    System.out.println("Task с id " + taskId + " успешно обновлена");
                                } else {
                                    taskManager.createTask(task);
                                    String response = gson.toJson(taskManager.getTask(task.getId()));
                                    sendText(httpExchange, response);
                                    System.out.println("Task с id " + taskId + " успешно создана");
                                }
                                break;
                            }
                            case "subtask": {
                                InputStream inputStream = httpExchange.getRequestBody();
                                String body = new String(inputStream.readAllBytes());
                                Subtask subtask = gson.fromJson(body, Subtask.class);
                                int subtaskId = subtask.getId();
                                Subtask control = taskManager.getSubtask(subtaskId);
                                if (control != null) {
                                    taskManager.updateSubtask(subtask);
                                    String response = gson.toJson(taskManager.getSubtask(subtaskId));
                                    sendText(httpExchange, response);
                                    System.out.println("Subtask с id " + subtask.getId() + " успешно обновлена");
                                } else {
                                    taskManager.createSubtask(subtask);
                                    String response = gson.toJson(taskManager.getSubtask(subtask.getId()));
                                    sendText(httpExchange, response);
                                    System.out.println("Subtask с id " + subtask.getId() + " успешно создана");
                                }
                                break;
                            }
                            case "epic": {
                                InputStream inputStream = httpExchange.getRequestBody();
                                String body = new String(inputStream.readAllBytes());
                                Epic epic = gson.fromJson(body, Epic.class);
                                int epicId = epic.getId();
                                Epic control = taskManager.getEpic(epicId);
                                if (control != null) {
                                    taskManager.updateEpic(taskManager.getEpic(epicId));
                                    String response = gson.toJson(taskManager.getEpic(epicId));
                                    sendText(httpExchange, response);
                                    System.out.println("Epic с id " + epic.getId() + " успешно обновлен");
                                } else {
                                    taskManager.createEpic(epic);
                                    String response = gson.toJson(taskManager.getEpic(epic.getId()));
                                    sendText(httpExchange, response);
                                    System.out.println("Epic с id " + epic.getId() + " успешно создан");
                                }
                                break;
                            }
                        }
                        break;
                    }
                }
            }

            if (splitStrings.length == 2 && query != null) {
                int id = parsePathId(query);
                switch (method) {
                    case "GET": {
                        switch (splitStrings[1]) {
                            case "task": {
                                if (id != -1) {
                                    String response = gson.toJson(taskManager.getTask(id));
                                    sendText(httpExchange, response);
                                }
                                break;
                            }
                            case "epic": {
                                if (id != -1) {
                                    String response = gson.toJson(taskManager.getEpic(id));
                                    sendText(httpExchange, response);
                                }
                                break;
                            }
                            case "subtask": {
                                if (id != -1) {
                                    String response = gson.toJson(taskManager.getSubtask(id));
                                    sendText(httpExchange, response);
                                }
                                break;
                            }
                        }
                        break;
                    }
                    case "DELETE": {
                        switch (splitStrings[1]) {
                            case "task": {
                                if (id != -1) {
                                    taskManager.dellTask(id);
                                    System.out.println("Задача с id " + id + " успешно удалена");
                                    httpExchange.sendResponseHeaders(200, 0);
                                }
                                break;
                            }
                            case "subtask": {
                                if (id != -1) {
                                    taskManager.dellSubtask(id);
                                    System.out.println("Подзадача с id " + id + " успешно удалена");
                                    httpExchange.sendResponseHeaders(200, 0);
                                }
                                break;
                            }
                            case "epic": {
                                if (id != -1) {
                                    taskManager.dellEpic(id);
                                    System.out.println("Epic с id " + id + " успешно удален");
                                    httpExchange.sendResponseHeaders(200, 0);
                                }
                                break;

                            }
                        }
                        break;
                    }
                }
            }

            if (splitStrings.length == 3 && query != null) {
                int id = parsePathId(query);
                if (id != -1) {
                    String response = gson.toJson(taskManager.getSubtasks(id));
                    sendText(httpExchange, response);
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } finally {
            httpExchange.close();
        }
    }

    private int parsePathId(String query) {
        String [] stringId = query.split("=");
        String id = stringId[1];
        try {
            return Integer.parseInt(id);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    private void sendText(HttpExchange httpExchange, String response) throws IOException {
        byte[] bytes = response.getBytes(Charset.defaultCharset());
        httpExchange.sendResponseHeaders(200, bytes.length);
        httpExchange.getResponseHeaders().add("Content-Type", "application/json");


        httpExchange.getResponseBody().write(bytes);
    }

    public static void main(String[] args) throws IOException {
        final HttpTaskServer server = new HttpTaskServer();
        server.start();
        server.stop();
    }

    public void start() {
        System.out.println("Запущен HttpTaskServer на порту " + PORT);
        server.start();
    }
    public void stop() {
        System.out.println("HttpTaskServer остановлен");
        server.stop(0);
    }
}
