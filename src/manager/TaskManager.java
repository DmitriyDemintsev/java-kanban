package manager;
import model.Task;
import model.Epic;
import model.Subtask;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface TaskManager {

    List<Task> getPrioritizedTasks();

    void createTask(model.Task task);
    Task getTask(int taskId);
    List<Task> getAllTasks();
    void updateTask(Task task);
    void dellTask(int taskId);
    void dellAllTasks();

    void createEpic(Epic epic);
    Epic getEpic(int epicId);
    List<Epic> getAllEpics();
    void updateEpic(Epic epic);
    void dellEpic(int epicId);
    void dellAllEpics();

    void createSubtask(@NotNull Subtask subtask);
    Subtask getSubtask(int subtaskId);
    List<Subtask> getAllSubtasks();
    void updateSubtask(Subtask subtask);
    void dellSubtask(int subtaskId);
    void dellAllSubtasks();

    List<Task> getHistory();
}
