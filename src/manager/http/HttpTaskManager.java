package manager.http;

import client.KVTaskClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import manager.FileBackedTasksManager;
import model.Epic;
import model.Subtask;
import model.Task;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpTaskManager extends FileBackedTasksManager {

    public static final String TASK_KEY = "tasks";
    public static final String SUBTASK_KEY = "subtask";
    public static final String EPIC_KEY = "epic";
    public static final String HISTORY_KEY = "history";
    private KVTaskClient client;
    private static Gson gson = new Gson();

    public HttpTaskManager(String url) {
        super(null);
        this.client = new KVTaskClient(url);
    }

    @Override
    public void save() {
        String jsonStringTask = gson.toJson(taskStorage.values());
        client.save(TASK_KEY, jsonStringTask);

        String jsonSubtask = gson.toJson(subtaskStorage.values());
        client.save(SUBTASK_KEY, jsonSubtask);

        String jsonEpic = gson.toJson(epicStorage.values());
        client.save(EPIC_KEY, jsonEpic);

        List<Integer> historyIds = historyManager.getHistory().stream()
                .map(Task::getId)
                .collect(Collectors.toList());
        String historyJson = gson.toJson(historyIds);
        client.save(HISTORY_KEY, historyJson);
    }

    public static HttpTaskManager loadFromServer(String url) {
        HttpTaskManager manager = new HttpTaskManager(url);
        KVTaskClient client = new KVTaskClient(url);
        Map<Integer, Task> idToTask = new HashMap<>();
        Type taskType = new TypeToken<ArrayList<Task>>() {
        }.getType();
        ArrayList<Task> loadTask = gson.fromJson(client.load(TASK_KEY), taskType);
        for (Task task : loadTask) {
            int id = task.getId();
            task.setId(id);
            manager.taskStorage.put(id, task);
            manager.prioritizedTask.add(task);
            idToTask.put(id, task);
        }

        Type epicType = new TypeToken<ArrayList<Epic>>() {
        }.getType();
        ArrayList<Epic> loadEpic = gson.fromJson(client.load(EPIC_KEY), epicType);
        for (Epic epic : loadEpic) {
            int id = epic.getId();
            epic.setId(id);
            epic.setSubtaskIds(new ArrayList<>());
            manager.epicStorage.put(id, epic);
            idToTask.put(id, epic);
        }

        Type subtaskType = new TypeToken<ArrayList<Subtask>>() {
        }.getType();
        ArrayList<Subtask> loadSubtask = gson.fromJson(client.load(SUBTASK_KEY), subtaskType);
        for (Subtask subtask : loadSubtask) {
            int id = subtask.getId();
            subtask.setId(id);
            manager.subtaskStorage.put(id, subtask);
            manager.prioritizedTask.add(subtask);
            idToTask.put(id, subtask);
            manager.epicStorage.get(subtask.getEpicId()).addSubtaskId(id);
        }

        for (int id: idToTask.keySet()) {
            if (id > manager.idGenerator) {
                manager.idGenerator = id;
            }
        }
        Type historyType = new TypeToken<ArrayList<Integer>>() {
        }.getType();
        List<Integer> historyIds = gson.fromJson(client.load(HISTORY_KEY), historyType);
        if (!historyIds.isEmpty()) {
            for (Integer id : historyIds) {
                manager.historyManager.add(idToTask.get(id));
            }
        }
        return manager;
    }
}
