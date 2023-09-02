package test;

import manager.TaskManager;
import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static model.TaskStatus.DONE;
import static model.TaskStatus.IN_PROGRESS;
import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {

    protected T taskManager;

    protected void init() {

    }

    @Test
    void checkCreateTask() {
        Task task = new Task("Тест создание Task", "Тест добавить новое описание Task");
        taskManager.createTask(task);

        assertNotNull(task, "Task пустая null");

    }

    @Test
    void checkGetTask() {
        Task task = new Task("Тест получение Task", "Тест описание для получения Task");
        taskManager.createTask(task);

        taskManager.getTask(task.getId());

        assertEquals(task, taskManager.getTask(task.getId()), "История пустая null");
        assertNull(taskManager.getTask(-1), "Неверный TaskId");

    }

    @Test
    void checkGetAllTask() {
        Task task_1 = new Task("Тест получение AllTask", "Тест описание для получения AllTask");
        Task task_2 = new Task("Тест получение AllTask", "Тест описание для получения AllTask");
        taskManager.createTask(task_1);
        taskManager.createTask(task_2);

        List<Task> tasks = taskManager.getAllTasks();
        assertEquals(tasks, List.of(task_1, task_2), "История пустая null");
    }

    @Test
    void checkUpdateTaskStatusInProgress() {
        Task task = new Task("Тест обновление Task для IN_PROGRESS", "Тест описание для" +
                "обновления Task для IN_PROGRESS Task");
        taskManager.createTask(task);

        task.setStartTime(LocalDateTime.now());
        LocalDateTime expectedStartTime = task.getStartTime();
        task.setDuration(10);
        task.setStatus(IN_PROGRESS);

        taskManager.updateTask(task);
        assertEquals(task.getTaskStatus(), IN_PROGRESS, "Статус на IN_PROGRESS не обновлен");
        assertEquals(task.getStartTime(), expectedStartTime, "Ожидаемое и реальное время старта не совпадают");
        assertEquals(task.getDuration(), 10, "Ожидаемая и реальная продолжительность не совпадают");
        assertEquals(task.getEndTime(), task.getStartTime().plusMinutes(10),
                "Ожидаемая и реальная продолжительность не совпадают");

    }

    @Test
    void checkUpdateTaskStatusDone() {
        Task task = new Task("Тест обновление Task для Done", "Тест описание для обновления" +
                "Task для Done");
        taskManager.createTask(task);

        task.setStatus(DONE);

        taskManager.updateTask(task);
        assertEquals(task.getTaskStatus(), DONE, "Статус на DONE не обновлен");
    }

    @Test
    void checkDellTask() {
        Task task = new Task("Тест удаление Task", "Описание Task");
        taskManager.createTask(task);

        taskManager.dellTask(task.getId());
        assertNull(taskManager.getTask(task.getId()), "Не удалось удалить Task");
    }

    @Test
    void checkDellAllTask() {
        Task task_1 = new Task("Тест удаление Task", "Описание Task");
        taskManager.createTask(task_1);
        Task task_2 = new Task("Тест удаление Task", "Описание Task");
        taskManager.createTask(task_2);

        taskManager.dellAllTasks();
        assertTrue(taskManager.getAllTasks().isEmpty(), "Не удалось удалить Tasks");
    }

    @Test
    void checkCreateEpic() {
        Epic epic = new Epic("Тест создание Epic", "Тест добавить новое описание Task");
        taskManager.createEpic(epic);

        assertNotNull(epic, "Epic пуст null");
    }

    @Test
    void checkGetEpic() {
        Epic epic = new Epic("Тест получение Epic", "Тест описание для получения Epic");
        taskManager.createEpic(epic);

        taskManager.getEpic(epic.getId());
        assertEquals(epic, taskManager.getEpic(epic.getId()), "История пустая null");
        assertNull(taskManager.getEpic(-1), "Неверный EpicId");

    }

    @Test
    void checkGetAllEpic() {
        Epic epic_1 = new Epic("Тест получить Epic", "Тест получения Epic");
        Epic epic_2 = new Epic("Тест получить Epic", "Тест получения Epic");
        taskManager.createEpic(epic_1);
        taskManager.createEpic(epic_2);

        List<Epic> epics = taskManager.getAllEpics();
        assertEquals(epics, List.of(epic_1, epic_2), "История пустая null");
    }

    @Test
    void checkDellEpic() {
        Epic epic = new Epic("Тест удаление Epic", "Описание Epic");
        taskManager.createEpic(epic);

        taskManager.dellEpic(epic.getId());
        assertNull(taskManager.getEpic(epic.getId()), "Не удалось удалить Epic");
    }

    @Test
    void checkDellAllEpic() {
        Epic epic_1 = new Epic("Тест удаление Epic_1", "Описание Epic_1");
        taskManager.createEpic(epic_1);
        Epic epic_2 = new Epic("Тест удаление Epic_2", "Описание Epic_2");
        taskManager.createEpic(epic_2);

        taskManager.dellAllEpics();
        assertTrue(taskManager.getAllEpics().isEmpty(), "Не удалось удалить Tasks");
    }

    @Test
    void checkCreateSubtask() {
        Epic epic = new Epic("Тест Epic для создания Subtask",
                "Тест описание Epic для создания Subtask");
        taskManager.createEpic(epic);
        Subtask subtask = new Subtask("Тест создание Subtask", "Тест описание Subtask",
                epic.getId());
        taskManager.createSubtask(subtask);

        assertNotNull(subtask, "Subtask пустая null");
    }

    @Test
    void checkGetSubtask() {
        Epic epic = new Epic("Тест Epic для создания Subtask",
                "Тест описание Epic для создания Subtask");
        taskManager.createEpic(epic);
        Subtask subtask = new Subtask("Тест получение Subtask", "Тест описание для получения " +
                "Subtask", epic.getId());
        taskManager.createSubtask(subtask);

        taskManager.getSubtask(subtask.getId());
        assertEquals(subtask, taskManager.getSubtask(subtask.getId()), "История пустая null");
        assertNull(taskManager.getSubtask(-1), "Неверный SubtaskId");

    }

    @Test
    void checkGetAllSubtask() {
        Epic epic = new Epic("Тест Epic для создания Subtask",
                "Тест описание Epic для создания Subtask");
        taskManager.createEpic(epic);
        Subtask subtask_1 = new Subtask("Тест получение AllSubtask", "Тест описание для " +
                "получения AllSubtask", epic.getId());
        Subtask subtask_2 = new Subtask("Тест получение AllSubtask", "Тест описание для " +
                "получения AllSubtask", epic.getId());
        taskManager.createSubtask(subtask_1);
        taskManager.createSubtask(subtask_2);

        List<Subtask> subtasks = taskManager.getAllSubtasks();
        assertEquals(subtasks, List.of(subtask_1, subtask_2), "История пустая null");
    }

    @Test
    void checkUpdateSubtask() {
        Epic epic = new Epic("Тест Epic для создания Subtask",
                "Тест описание Epic для создания Subtask");
        taskManager.createEpic(epic);

        Subtask subtask = new Subtask("Тест обновление Subtask", "Тест описание для обновления " +
                "Subtask", epic.getId());
        taskManager.createSubtask(subtask);

        subtask.setStartTime(LocalDateTime.now());
        LocalDateTime expectedStartTime = subtask.getStartTime();
        subtask.setDuration(10);
        subtask.setStatus(IN_PROGRESS);
        taskManager.updateSubtask(subtask);

        assertEquals(subtask.getTaskStatus(), IN_PROGRESS, "Статус на IN_PROGRESS не обновлен");
        assertEquals(subtask.getStartTime(), expectedStartTime, "Ожидаемое и реальное время старта не совпадают");
        assertEquals(subtask.getDuration(), 10, "Ожидаемая и реальная продолжительность не совпадают");
        assertEquals(subtask.getEndTime(), subtask.getStartTime().plusMinutes(10),
                "Ожидаемая и реальная продолжительность не совпадают");

    }

    @Test
    void checkDellSubtask() {
        Epic epic_1 = new Epic("Тест удаление AllSubtask", "Описание Epic");
        taskManager.createEpic(epic_1);

        Subtask subtask = new Subtask("Тест удаление Subtask", "Описание Subtask",
                epic_1.getId());
        taskManager.createSubtask(subtask);

        taskManager.dellSubtask(subtask.getId());
        assertNull(taskManager.getSubtask(subtask.getId()), "Не удалось удалить Subtask");
    }

    @Test
    void checkDellAllSubtask() {
        Epic epic_1 = new Epic("Тест удаление AllSubtask", "Описание Epic");
        taskManager.createEpic(epic_1);
        Subtask subtask_1 = new Subtask("Тест удаление AllSubtask", "Описание Subtask_1",
                epic_1.getId());
        taskManager.createSubtask(subtask_1);
        Subtask subtask_2 = new Subtask("Тест удаление AllSubtask", "Описание Subtask_2",
                epic_1.getId());
        taskManager.createSubtask(subtask_2);

        taskManager.dellAllSubtasks();
        assertTrue(taskManager.getAllSubtasks().isEmpty(), "Не удалось удалить AllSubtask");
    }
}
