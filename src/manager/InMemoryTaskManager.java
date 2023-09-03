package manager;

import manager.exception.TaskValidationException;
import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    protected final HashMap<Integer, Task> taskStorage = new HashMap<>();
    protected final HashMap<Integer, Epic> epicStorage = new HashMap<>();
    protected final HashMap<Integer, Subtask> subtaskStorage = new HashMap<>();
    protected final HistoryManager historyManager = Managers.getDefaultHistory();
    protected Set<Task> prioritizedTask = new TreeSet<>(Comparator.comparing(
            Task::getStartTime,
            Comparator.nullsLast(Comparator.naturalOrder())
    ).thenComparing(Task::getId));

    private int idGenerator = 0;

    private int generateId() {
        return ++idGenerator;
    }

    @Override
    public List<Task> getPrioritizedTasks() {

        return new ArrayList<>(prioritizedTask);
    }

    private void validate(Task task) {
        List<Task> list = getPrioritizedTasks();
        //if()
        for (int i = 0; i < list.size() - 1; i++) {
            if ((task.getStartTime() != null) && (list.get(i).getStartTime() != null)
                    && (task.getEndTime() != null) && (list.get(i).getEndTime() != null)) {
                if (task.getStartTime().isAfter(list.get(i).getEndTime()) ||
                        task.getEndTime().isBefore(list.get(i).getStartTime())) {
                    continue;
                }
                throw new TaskValidationException("Задачи пересекаются");
            }
        }
    }

    @Override
    public void createTask(Task task) {
        task.setId(generateId());
        taskStorage.put(task.getId(), task);
        prioritizedTask.add(task);
        try {
            validate(task);
        } catch (TaskValidationException e) {
            dellTask(task.getId());
            throw e;
        }
    }

    @Override
    public Task getTask(int taskId) {
        if (!taskStorage.containsKey(taskId)) {
            return null;
        }
        historyManager.add(taskStorage.get(taskId));
        return taskStorage.get(taskId);
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(taskStorage.values());
    }

    @Override
    public void updateTask(Task task) {
        Task updated = taskStorage.get(task.getId());
        if (updated == null) {
            return;
        }
        taskStorage.put(task.getId(), task);
    }

    @Override
    public void dellTask(int taskId) {
        prioritizedTask.remove(getTask(taskId));
        taskStorage.remove(taskId);
        historyManager.remove(taskId);
    }

    @Override
    public void dellAllTasks() {
        prioritizedTask.removeAll(getAllTasks());
        for (int taskId : taskStorage.keySet()) {
            historyManager.remove(taskId);
        }
        taskStorage.clear();
    }

    @Override
    public void createEpic(Epic epic) {
        epic.setId(generateId());
        epicStorage.put(epic.getId(), epic);
        //prioritizedTask.add(epic);
        try {
            validate(epic);
        } catch (TaskValidationException e) {
            dellEpic(epic.getId());
            throw e;
        }
    }

    @Override
    public Epic getEpic(int epicId) {
        if (!epicStorage.containsKey(epicId)) {
            return null;
        }

        historyManager.add(epicStorage.get(epicId));
        return epicStorage.get(epicId);
    }

    @Override
    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epicStorage.values());
    }

    @Override
    public void updateEpic(Epic epic) {
        Epic updated = epicStorage.get(epic.getId());
        if (updated == null) {
            return;
        }
        updated.setName(epic.getName());
        updated.setDescription(epic.getDescription());
    }

    @Override
    public void dellEpic(int epicId) {
        Epic epic = getEpic(epicId);
        if (epic == null) {
            return;
        }
        for (int subtaskId : epic.getSubtaskIds()) {
            prioritizedTask.remove(getSubtask(subtaskId));
            dellSubtask(subtaskId);
        }
        prioritizedTask.remove(epic);
        epicStorage.remove(epicId);
        historyManager.remove(epicId);
    }

    @Override
    public void dellAllEpics() {
        dellAllSubtasks();
        prioritizedTask.removeAll(getAllEpics());
        for (int epicId : epicStorage.keySet()) {
            historyManager.remove(epicId);
        }
        epicStorage.clear();
    }

    @Override
    public void createSubtask(@NotNull Subtask subtask) {
        subtask.setId(generateId());
        if (!epicStorage.containsKey(subtask.getEpicId())) {
            return;
        }

        subtaskStorage.put(subtask.getId(), subtask);
        Epic epic = getEpic(subtask.getEpicId());
        epic.addSubtaskId(subtask.getId());
        updateEpicData(subtask.getEpicId());
        prioritizedTask.add(subtask);

        try {
            validate(subtask);
        } catch (TaskValidationException e) {
            dellSubtask(subtask.getId());
            throw e;
        }
    }

    @Override
    public Subtask getSubtask(int subtaskId) {
        if (!subtaskStorage.containsKey(subtaskId)) {
            return null;
        }

        historyManager.add(subtaskStorage.get(subtaskId));
        return subtaskStorage.get(subtaskId);
    }

    @Override
    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtaskStorage.values());
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        Subtask updated = subtaskStorage.get(subtask.getId());
        if (updated == null) {
            return;
        }
        subtaskStorage.put(subtask.getId(), subtask);
        updateEpicData(subtask.getEpicId());
    }

    @Override
    public void dellSubtask(int subtaskId) {
        Subtask subtask = getSubtask(subtaskId);
        if (subtask == null) {
            return;
        }
        prioritizedTask.remove(subtask);
        Epic epic = getEpic(subtask.getEpicId());
        epic.deleteSubtaskId(subtaskId);
        updateEpicData(subtask.getEpicId());
        historyManager.remove(subtaskId);
        subtaskStorage.remove(subtaskId);
    }

    @Override
    public void dellAllSubtasks() {
        prioritizedTask.removeAll(getAllSubtasks());
        for (Epic epic : epicStorage.values()) {
            epic.getSubtaskIds().clear();
            updateEpicData(epic.getId());
        }
        for (int subtaskId : subtaskStorage.keySet()) {
            historyManager.remove(subtaskId);
        }
        subtaskStorage.clear();
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

    private void updateEpicData(int epicId) {
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
            epic.setStatus(TaskStatus.NEW);
        } else if (isAllDone) {
            epic.setStatus(TaskStatus.DONE);
        } else {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        }

        LocalDateTime startTime = null;
        LocalDateTime endTime = null;
        int duration = 0;
        for (Subtask subtask : subtasks) {
            if (subtask.getStartTime() != null) {
                if (startTime == null || subtask.getStartTime().isBefore(startTime)) {
                    startTime = subtask.getStartTime();
                }
                if (endTime == null || subtask.getEndTime().isAfter(endTime)) {
                    endTime = subtask.getEndTime();
                }
            }
            duration += subtask.getDuration();
        }
        epic.setStartTime(startTime);
        epic.setEndTime(endTime);
        epic.setDuration(duration);
    }

}
