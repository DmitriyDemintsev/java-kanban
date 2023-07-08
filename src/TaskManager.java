import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public interface TaskManager {

    void createTask(Task task);
    Task getTask(int taskId);
    ArrayList<Task> getAllTasks();
    void updateTask(Task task);
    void dellTask(int taskId);
    void dellAllTasks();

    void createEpic(Epic epic);
    Epic getEpic(int epicId);
    ArrayList<Epic> getAllEpics();
    void updateEpic(Epic epic);
    void dellEpic(int epicId);
    void dellAllEpics();

    void createSubtask(@NotNull Subtask subtask);
    Subtask getSubtask(int subtaskId);
    ArrayList<Subtask> getAllSubtasks();
    void updateSubtask(Subtask subtask);
    void dellSubtask(int subtaskId);
    void dellAllSubtasks();

    ArrayList<Task> getHistory();
}
