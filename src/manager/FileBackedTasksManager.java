package manager;

import model.*;
import org.jetbrains.annotations.NotNull;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FileBackedTasksManager extends InMemoryTaskManager {

    private String fileName;

    private static CSVFormatHandler handler = new CSVFormatHandler();

    public FileBackedTasksManager(String fileName) {
        this.fileName = fileName;
    }

    private void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(handler.getHeader());
            for (Task task: taskStorage.values()) {
                writer.write(handler.toString(task));
                writer.newLine();
            }
            for (Epic epic: epicStorage.values()) {
                writer.write(handler.toString(epic));
                writer.newLine();
            }
            for (Subtask subtask: subtaskStorage.values()) {
                writer.write(handler.toString(subtask));
                writer.newLine();
            }
            writer.newLine();
            writer.write(handler.historyToString(historyManager));

        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка во время записи файла.");
        }
    }

    static FileBackedTasksManager loadFromFile(String fileName) throws IOException {
        FileBackedTasksManager manager = new FileBackedTasksManager(fileName);

        try {
            List<String> lines = Files.readAllLines(Path.of(fileName));
            if (lines.isEmpty()) {
                return manager;
            }
            Map<Integer, Task> idToTask = new HashMap<>();
            int i;
            for (i = 1; i < lines.size(); i++) {
                if (lines.get(i).isEmpty()) {
                    break;
                }
                Task task = handler.fromString(lines.get(i));
                idToTask.put(task.getTaskId(), task);
                if (task.getTaskType() == TaskType.TASK) {
                    manager.taskStorage.put(task.getTaskId(), task);
                }
                if (task.getTaskType() == TaskType.EPIC) {
                    manager.epicStorage.put(task.getTaskId(), (Epic) task);
                }
                if (task.getTaskType() == TaskType.SUBTASK) {
                    manager.subtaskStorage.put(task.getTaskId(), (Subtask) task);
                }
            }
            for (Subtask subtask: manager.getAllSubtasks()) {
                manager.getEpic(subtask.getEpicId()).addSubtaskId(subtask);
            }

            String historyRow = lines.get(++i);
            List<Integer> history = handler.historyFromString(historyRow);
            for (Integer id : history) {
                manager.historyManager.add(idToTask.get(id));
            }
            return manager;
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка во время чтения файла.");
        }
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public Task getTask(int taskId) {
        Task task = super.getTask(taskId);
        save();
        return task;
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> task = super.getAllTasks();
        save();
        return task;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void dellTask(int taskId) {
        super.dellTask(taskId);
        save();
    }

    @Override
    public void dellAllTasks() {
        super.dellAllTasks();
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public Epic getEpic(int epicId) {
        Epic epic = super.getEpic(epicId);
        save();
        return epic;
    }

    @Override
    public ArrayList<Epic> getAllEpics() {
        ArrayList<Epic> epic = super.getAllEpics();
        save();
        return epic;
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void dellEpic(int epicId) {
        super.dellEpic(epicId);
        save();
    }

    @Override
    public void dellAllEpics() {
        super.dellAllSubtasks();
        save();
    }

    @Override
    public void createSubtask(@NotNull Subtask subtask) {
        super.createSubtask(subtask);
        save();
    }

    @Override
    public Subtask getSubtask(int subtaskId) {
        Subtask subtask = super.getSubtask(subtaskId);
        save();
        return subtask;
    }

    @Override
    public ArrayList<Subtask> getAllSubtasks() {
        ArrayList<Subtask> subtasks = super.getAllSubtasks();
        save();
        return subtasks;
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void dellSubtask(int subtaskId) {
        super.dellSubtask(subtaskId);
        save();
    }

    @Override
    public void dellAllSubtasks() {
        super.dellAllSubtasks();
        save();
    }

    @Override
    public List<Task> getHistory() {
        return super.getHistory();
    }


    public static void main(String[] args) throws IOException {
        TaskManager taskManager = new FileBackedTasksManager("fileName.txt");

        Task task_1 = new Task("Задача №1", "Это описание задачи №1");
        taskManager.createTask(task_1);


        Epic epic_1 = new Epic("Эпик_1", "Здесь описание для эпика_1");
        taskManager.createEpic(epic_1);

        Subtask subtask_1_1 = new Subtask("Подзадача_1_1",
                "Здесь напишем что хотим в подзадаче_1_1",
                epic_1.getTaskId());
        Subtask subtask_1_3 = new Subtask("Подзадача_1_3",
                "Здесь напишем что хотим в подзадаче_1_3",
                epic_1.getTaskId());

        taskManager.createSubtask(subtask_1_1);
        taskManager.createSubtask(subtask_1_3);

        taskManager.getTask(task_1.getTaskId());
        taskManager.getEpic(epic_1.getTaskId());
        taskManager.getSubtask(subtask_1_3.getTaskId());

        System.out.println(taskManager.getHistory());

        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubtasks());


        System.out.println();

        TaskManager taskManager2 = loadFromFile("fileName.txt");
        System.out.println(taskManager2.getHistory());
        System.out.println(taskManager2.getAllTasks());
        System.out.println(taskManager2.getAllEpics());
        System.out.println(taskManager2.getAllSubtasks());
    }
}
