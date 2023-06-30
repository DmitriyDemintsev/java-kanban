import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {

    private HashMap<Integer, Task> taskStorage = new HashMap<>();
    private HashMap<Integer, Epic> epicStorage = new HashMap<>();
    private HashMap<Integer, Subtask> subtaskStorage = new HashMap<>();

     private int idGenerator = 0;

    private int generateId() {
        return ++idGenerator;
    }

    public void createTask(Task task) {
        task.setTaskId(generateId());
        taskStorage.put(task.getTaskId(), task);
    }

    public Task getTaskById(int taskId) {
        return taskStorage.get(taskId);
    }

    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(taskStorage.values());
    }

    public void dellAllTasks() {
        taskStorage.clear();
    }

    public void updateTask(Task task) {
        Task updated = taskStorage.get(task.getTaskId());
        if (updated == null) {
            return;
        }
        taskStorage.put(task.getTaskId(), task);
    }

    public void dellTask(int taskId) {
        taskStorage.remove(taskId);
    }

    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epicStorage.values());
    }

    public void dellAllEpics() {
        dellAllSubtasks();
        epicStorage.clear();
    }

    public Epic getEpic(int epicId) {
        return epicStorage.get(epicId);
    }

    public void createEpic(Epic epic) {
        epic.setTaskId(generateId());
        epicStorage.put(epic.getTaskId(), epic);
    }

    public void updateEpic(Epic epic) {
        Epic updated = epicStorage.get(epic.getTaskId());
        if (updated == null) {
            return;
        }
        updated.setTaskName(epic.getTaskName());
        updated.setTaskDescription(epic.getTaskDescription());
    }

    public void dellEpic(int epicId) {
        Epic epic = getEpic(epicId);
        if (epic == null) {
            return;
        }
        for (int subtaskId : epic.getSubtaskIds()) {
            subtaskStorage.remove(subtaskId);
        }
        epicStorage.remove(epicId);
    }

    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtaskStorage.values());
    }

    public void dellAllSubtasks() {
        subtaskStorage.clear();
        for (Epic epic : epicStorage.values()) {
            epic.getSubtaskIds().clear();
            String status = "NEW";
            epic.setTaskStatus(status);
        }
    }

    public Subtask getSubtask(int subtaskId) {
        return subtaskStorage.get(subtaskId);
    }

    public void createSubtask(@NotNull Subtask subtask) {
        subtask.setTaskId(generateId());
        subtaskStorage.put(subtask.getTaskId(), subtask);
        if (!epicStorage.containsKey(subtask.getTaskId())) {
            return;
        }
        Epic epic = getEpic(subtask.getEpicId());
        epic.getSubtaskIds().add(subtask.getTaskId());
        updateEpicStatus(subtask.getEpicId());
    }

    public void updateSubtask(Subtask subtask) {
        Subtask updated = subtaskStorage.get(subtask.getTaskId());
        if (updated == null) {
            return;
        }
        subtaskStorage.put(subtask.getTaskId(), subtask);
    }

    public void dellSubtask(int subtaskId) {
        Subtask subtask = getSubtask(subtaskId);
        if(subtask == null) {
            return;
        }
        subtaskStorage.remove(subtaskId);
        Epic epic = getEpic(subtask.getEpicId());
        epic.getSubtaskIds().remove(subtaskId);
        updateEpicStatus(subtaskId);
    }

    public ArrayList<Subtask> getSubtasks(int epicId) {
        Epic epic = getEpic(epicId);
        ArrayList<Subtask> subtasks = new ArrayList<>();
        for (int subtaskId : epic.getSubtaskIds()) {
            subtasks.add(getSubtask(subtaskId));
        }
        return subtasks;
    }

    private void updateEpicStatus(int epicId) {
        Epic epic = getEpic(epicId);
        ArrayList<Subtask> subtasks = getSubtasks(epicId);
        boolean isAllNew = true;
        boolean isAllDone = true;
        for (Subtask subtask : subtasks) {
            if (!subtask.getTaskStatus().equals("NEW")) {
                isAllNew = false;
            }
            if (!subtask.getTaskStatus().equals("DONE")) {
                isAllDone = false;
            }
        }
        if (subtasks.isEmpty() || isAllNew) {
            epic.setTaskStatus("NEW");
        } else if (isAllDone) {
            epic.setTaskStatus("DONE");
        } else {
            epic.setTaskStatus("IN_PROGRESS");
        }
    }
}
