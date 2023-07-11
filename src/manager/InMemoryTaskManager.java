package manager;
import model.Task;
import model.Epic;
import model.Subtask;
import model.TaskStatus;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {

    private final HashMap<Integer, Task> taskStorage = new HashMap<>();
    private final HashMap<Integer, Epic> epicStorage = new HashMap<>();
    private final HashMap<Integer, Subtask> subtaskStorage = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    private int idGenerator = 0;

    private int generateId() {
        return ++idGenerator;
    }

    @Override
    public void createTask(Task task) {
        task.setTaskId(generateId());
        taskStorage.put(task.getTaskId(), task);
    }

    @Override
    public Task getTask(int taskId) {
        if (!taskStorage.containsKey(taskId)) {
            return null;
        } else {
            historyManager.add(taskStorage.get(taskId));
            return taskStorage.get(taskId);
        }
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(taskStorage.values());
    }

    @Override
    public void updateTask(Task task) {
        Task updated = taskStorage.get(task.getTaskId());
        if (updated == null) {
            return;
        }
        taskStorage.put(task.getTaskId(), task);
    }

    @Override
    public void dellTask(int taskId) {
        taskStorage.remove(taskId);
    }

    @Override
    public void dellAllTasks() {
        taskStorage.clear();
    }



    @Override
    public void createEpic(Epic epic) {
        epic.setTaskId(generateId());
        epicStorage.put(epic.getTaskId(), epic);
    }

    @Override
    public Epic getEpic(int epicId) {
        historyManager.add(epicStorage.get(epicId));
        return epicStorage.get(epicId);
    }

    @Override
    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epicStorage.values());
    }

    @Override
    public void updateEpic(Epic epic) {
        Epic updated = epicStorage.get(epic.getTaskId());
        if (updated == null) {
            return;
        }
        updated.setTaskName(epic.getTaskName());
        updated.setTaskDescription(epic.getTaskDescription());
    }

    @Override
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

    @Override
    public void dellAllEpics() {
        dellAllSubtasks();
        epicStorage.clear();
    }

    @Override
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

    @Override
    public Subtask getSubtask(int subtaskId) {
        historyManager.add(subtaskStorage.get(subtaskId));
        return subtaskStorage.get(subtaskId);
    }

    @Override
    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtaskStorage.values());
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        Subtask updated = subtaskStorage.get(subtask.getTaskId());
        if (updated == null) {
            return;
        }
        subtaskStorage.put(subtask.getTaskId(), subtask);
    }

    @Override
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

    @Override
    public void dellAllSubtasks() {
        subtaskStorage.clear();
        for (Epic epic : epicStorage.values()) {
            epic.getSubtaskIds().clear();
            updateEpicStatus(epic.getTaskId());
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    public List<Subtask> getSubtasks(int epicId) {
        Epic epic = getEpic(epicId);
        ArrayList<Subtask> subtasks = new ArrayList<>();
        for (int subtaskId : epic.getSubtaskIds()) {
            subtasks.add(getSubtask(subtaskId));
        }
        return subtasks;
    }

    private void updateEpicStatus(int epicId) {
        Epic epic = getEpic(epicId);
        List<Subtask> subtasks = getSubtasks(epicId);
        boolean isAllNew = true;
        boolean isAllDone = true;
        for (Subtask subtask : subtasks) {
            if (!subtask.getTaskStatus().equals(TaskStatus.NEW)) {
                isAllNew = false;
            }
            if (!subtask.getTaskStatus().equals(TaskStatus.DONE)) {
                isAllDone = false;
            }
        }
        if (subtasks.isEmpty() || isAllNew) {
            epic.setTaskStatus(TaskStatus.NEW);
        } else if (isAllDone) {
            epic.setTaskStatus(TaskStatus.DONE);
        } else {
            epic.setTaskStatus(TaskStatus.IN_PROGRESS);
        }
    }
}