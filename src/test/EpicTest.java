package test;

import manager.InMemoryTaskManager;
import model.Epic;
import model.Subtask;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static model.TaskStatus.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class EpicTest {
    InMemoryTaskManager taskManager = new InMemoryTaskManager();

    @Test
    void checkCreateNewEpic() {
        Epic epic = new Epic("Тест создай новую задачу", "Тест добавить описание задачи");
        taskManager.createEpic(epic);

        assertEquals(epic, taskManager.getEpic(epic.getId()), "Задачи не совпадают.");

        final List<Epic> tasks = taskManager.getAllEpics();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(epic, tasks.get(0), "Задачи не совпадают.");
        assertEquals(epic.getTaskStatus(), NEW);
    }

    @Test
    void checkTaskAndSubtaskWitchStatusNew() {
        Epic epic_1 = new Epic("Тест задача для allNew", "Тест добавить новое описание задачи");
        taskManager.createEpic(epic_1);
        Subtask subtask_1 = new Subtask("Тестовая Subtask_1", "Описание тестовой Subtask_1", epic_1.getId());
        taskManager.createSubtask(subtask_1);
        Subtask subtask_2 = new Subtask("Тестовая Subtask_2", "Описание тестовой Subtask_2", epic_1.getId());
        taskManager.createSubtask(subtask_2);

        assertEquals(epic_1.getTaskStatus(), NEW);
        assertEquals(epic_1.getTaskStatus(), subtask_1.getTaskStatus(), "epic_1 и subtask_1 не совпадают");
        assertEquals(epic_1.getTaskStatus(), subtask_2.getTaskStatus(), "epic_1 и subtask_2 не совпадают");
    }

    @Test
    void checkTaskAndSubtaskWitchStatusInProgress() {
        Epic epic_1 = new Epic("Тест задача для allInProgress", "Тест добавить новое описание задачи");
        taskManager.createEpic(epic_1);
        Subtask subtask_1 = new Subtask("Тестовая Subtask_1", "Описание тестовой Subtask_1", epic_1.getId());
        taskManager.createSubtask(subtask_1);
        Subtask subtask_2 = new Subtask("Тестовая Subtask_2", "Описание тестовой Subtask_2", epic_1.getId());
        taskManager.createSubtask(subtask_2);
        subtask_1.setStatus(IN_PROGRESS);
        taskManager.updateSubtask(subtask_1);
        subtask_2.setStatus(IN_PROGRESS);
        taskManager.updateSubtask(subtask_2);

        assertEquals(epic_1.getTaskStatus(), IN_PROGRESS);
        assertEquals(epic_1.getTaskStatus(), subtask_1.getTaskStatus(), "Статусы IN_PROGRESS epic_1 и subtask_1 не совпадают");
        assertEquals(epic_1.getTaskStatus(), subtask_2.getTaskStatus(), "Статусы IN_PROGRESS epic_1 и subtask_2 не совпадают");
    }

    @Test
    void checkTaskAndSubtaskWitchStatusDone() {
        Epic epic_1 = new Epic("Тест задача для allDone", "Тест добавить новое описание задачи");
        taskManager.createEpic(epic_1);

        Subtask subtask_1 = new Subtask("Тестовая Subtask_1", "Описание тестовой Subtask_1", epic_1.getId());
        taskManager.createSubtask(subtask_1);
        Subtask subtask_2 = new Subtask("Тестовая Subtask_2", "Описание тестовой Subtask_2", epic_1.getId());
        taskManager.createSubtask(subtask_2);

        subtask_1.setStatus(DONE);
        taskManager.updateSubtask(subtask_1);
        subtask_2.setStatus(DONE);
        taskManager.updateSubtask(subtask_2);

        assertEquals(epic_1.getTaskStatus(), DONE);
        assertEquals(epic_1.getTaskStatus(), subtask_1.getTaskStatus(), "Статусы DONE epic_1 и subtask_1 не совпадают");
        assertEquals(epic_1.getTaskStatus(), subtask_2.getTaskStatus(), "Статусы DONE epic_1 и subtask_2 не совпадают");
    }

    @Test
    void checkTaskAndSubtaskWitchStatusNewAndDone() {
        Epic epic_1 = new Epic("Тест задача для newAndDone",
                "Тест добавить новое описание задачи");
        taskManager.createEpic(epic_1);

        Subtask subtask_1 = new Subtask("Тестовая Subtask_1",
                "Описание тестовой Subtask_1", epic_1.getId());
        taskManager.createSubtask(subtask_1);
        Subtask subtask_2 = new Subtask("Тестовая Subtask_2",
                "Описание тестовой Subtask_2", epic_1.getId());
        taskManager.createSubtask(subtask_2);

        subtask_2.setStatus(DONE);
        taskManager.updateSubtask(subtask_2);

        assertEquals(epic_1.getTaskStatus(), IN_PROGRESS, "Не удалось изменить статус Epic");
    }

    @Test
    void checkDataForEpicEmpty() {
        Epic epic = new Epic("Тест задача для newAndDone", "Тест добавить новое описание задачи");
        taskManager.createEpic(epic);

        assertEquals(epic.getStartTime(), null, "Ожидаемое и реальное время старта не совпадают");
        assertEquals(epic.getDuration(), 0, "Ожидаемая и реальная продолжительность не совпадают");
        assertEquals(epic.getEndTime(), null,
                "Ожидаемая и реальная продолжительность не совпадают");

    }

    @Test
    void checkDataForEpicWitchSubtask() {
        Epic epic_1 = new Epic("Тест задача для newAndDone",
                "Тест добавить новое описание задачи");
        taskManager.createEpic(epic_1);

        Subtask subtask_1 = new Subtask("Тестовая Subtask_1",
                "Описание тестовой Subtask_1", epic_1.getId());
        taskManager.createSubtask(subtask_1);

        subtask_1.setStartTime(LocalDateTime.now());
        LocalDateTime expectedStartTime = subtask_1.getStartTime();
        subtask_1.setDuration(10);
        taskManager.updateSubtask(subtask_1);

        assertEquals(epic_1.getStartTime(), expectedStartTime,
                "Ожидаемое и реальное время старта не совпадают");
        assertEquals(epic_1.getDuration(), 10, "Ожидаемая и реальная продолжительность не совпадают");
        assertEquals(epic_1.getEndTime(), subtask_1.getStartTime().plusMinutes(10),
                "Ожидаемая и реальная продолжительность не совпадают");
    }

    @Test
    void checkDataForEpicWitchThreeSubtask() {
        Epic epic_1 = new Epic("Тест задача для newAndDone",
                "Тест добавить новое описание задачи");
        taskManager.createEpic(epic_1);

        Subtask subtask_1 = new Subtask("Тестовая Subtask_1",
                "Описание тестовой Subtask_1", epic_1.getId());
        Subtask subtask_2 = new Subtask("Тестовая Subtask_1",
                "Описание тестовой Subtask_1", epic_1.getId());
        Subtask subtask_3 = new Subtask("Тестовая Subtask_1",
                "Описание тестовой Subtask_1", epic_1.getId());
        taskManager.createSubtask(subtask_1);
        taskManager.createSubtask(subtask_2);
        taskManager.createSubtask(subtask_3);

        subtask_1.setStartTime(LocalDateTime.now());
        subtask_1.setDuration(10);
        taskManager.updateSubtask(subtask_1);

        subtask_2.setStartTime(null);
        subtask_2.setDuration(0);
        taskManager.updateSubtask(subtask_2);

        subtask_3.setStartTime(LocalDateTime.now());
        subtask_3.setDuration(20);
        taskManager.updateSubtask(subtask_3);

        assertEquals(epic_1.getStartTime(), subtask_1.getStartTime(),
                "Ожидаемое и реальное время старта не совпадают");
        assertEquals(epic_1.getDuration(), 30, "Ожидаемая и реальная продолжительность не совпадают");
        assertEquals(epic_1.getEndTime(), subtask_3.getEndTime(),
                "Ожидаемая и реальная продолжительность не совпадают");
    }
}
