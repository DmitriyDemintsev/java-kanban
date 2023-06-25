import java.util.ArrayList;
import java.util.HashMap;


public class TaskManager {
    public HashMap<Integer, Epic> epicStorage = new HashMap<>();
    public HashMap<Integer, Subtask> subtaskStorage = new HashMap<>();
    public HashMap<Integer, Task> taskStorage = new HashMap<>();
    int idGenerator = 0;

    public int generateId() {
        idGenerator += 1;
        return idGenerator;
    }

    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        for (Task task : taskStorage.values()) {
            tasks.add(task);
        }
        return tasks;
    }

    public void dellAllTasks() {
        taskStorage.clear();
    }

    public Task getTask(int taskId) {
        return taskStorage.get(taskId);
    }

    public void createTask(Task task) {
        taskStorage.put(task.taskId, task);
    }

    public void updateTask(Task task) {
        taskStorage.put(task.taskId, task);
    }

    public void dellTask(int taskId) {
        taskStorage.remove(taskId);
    }

    public ArrayList<Epic> getAllEpics() {
        ArrayList<Epic> epics = new ArrayList<>();
        for (Epic epic : epicStorage.values()) {
            epics.add(epic);
        }
        return epics;
    }

    public void dellAllEpics() {
        dellAllSubtasks();
        epicStorage.clear();
    }

    public Epic getEpic(int epicId) {
        return epicStorage.get(epicId);
    }

    public void createEpic(Epic epic) {
        epicStorage.put(epic.taskId, epic);
    }

    public void updateEpic(Epic epic) {
        epicStorage.put(epic.taskId, epic);
    }

    public void dellEpic(int epicId) {
        Epic epic = getEpic(epicId);
        for (int subtaskId : epic.subtaskIds) {
            subtaskStorage.remove(subtaskId);
        }
        epicStorage.remove(epicId);
    }

    public ArrayList<Subtask> getAllSubtasks() {
        ArrayList<Subtask> subtasks = new ArrayList<>();
        for (Subtask subtask : subtaskStorage.values()) {
            subtasks.add(subtask);
        }
        return subtasks;
    }

    public void dellAllSubtasks() {
        subtaskStorage.clear();
    }

    public Subtask getSubtask(int subtaskId) {
        return subtaskStorage.get(subtaskId);
    }

    public void createSubtask(Subtask subtask) {
        subtaskStorage.put(subtask.taskId, subtask);
        Epic epic = getEpic(subtask.epicId);
        epic.subtaskIds.add(subtask.taskId);
        updateEpicStatus(subtask.epicId);
    }

    public void updateSubtask(Subtask subtask) {
        subtaskStorage.put(subtask.taskId, subtask);
        updateEpicStatus(subtask.epicId);
    }

    public void dellSubtask(int subtaskId) {
        Subtask subtask = getSubtask(subtaskId);
        subtaskStorage.remove(subtaskId);
        Epic epic = getEpic(subtask.epicId);
        epic.subtaskIds.remove(subtaskId);
        updateEpicStatus(subtaskId);
    }

    public ArrayList<Subtask> getSubtasks(int epicId) {
        Epic epic = getEpic(epicId);
        ArrayList<Subtask> subtasks = new ArrayList<>();
        for (int subtaskId : epic.subtaskIds) {
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
            if (!subtask.taskStatus.equals("NEW")) {
                isAllNew = false;
            }
            if (!subtask.taskStatus.equals("DONE")) {
                isAllDone = false;
            }
        }
        if (subtasks.isEmpty() || isAllNew) {
            epic.taskStatus = "NEW";
        } else if (isAllDone) {
            epic.taskStatus = "DONE";
        } else {
            epic.taskStatus = "IN_PROGRESS";
        }
    }
}
