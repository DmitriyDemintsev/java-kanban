package test;

import manager.HistoryManager;
import manager.Managers;
import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    private HistoryManager historyManager;
    private Task task;

    @BeforeEach
    void setUp() {
        historyManager = Managers.getDefaultHistory();
        task = new Task("Task 1", "Description 1");
    }

    @Test
    void checkRemoveTask() {
        Task task1 = new Task("Task 1", "Description 1");
        Task task2 = new Task("Task 2", "Description 2");
        Task task3 = new Task("Task 3", "Description 3");
        Task task4 = new Task("Task 3", "Description 3");

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.add(task4);

        historyManager.remove(task2.getId());
        assertFalse(historyManager.getHistory().contains(task2));
        historyManager.remove(task1.getId());
        assertFalse(historyManager.getHistory().contains(task1));
        historyManager.remove(task4.getId());
        assertFalse(historyManager.getHistory().contains(task4));
        historyManager.remove(task3.getId());
        assertFalse(historyManager.getHistory().contains(task3));

    }

    @Test
    void checkAddTask() {
        Task task = new Task("Тест добавление add Task", "Тест описание для добавления " +
                "add Task");
        historyManager.add(task);

        assertTrue(historyManager.getHistory().contains(task), "Task Is null");
    }

    @Test
    void checkGetHistory() {
        Task task = new Task("Тест Task получения historyManager", "Тест описание для " +
                "получения historyManager");
        task.setId(1);
        Epic epic = new Epic("Тест Epic получения historyManager", "Тест описание для " +
                "получения historyManager");
        epic.setId(2);
        Subtask subtask = new Subtask("Тест Subtask получения historyManager", "Тест описание " +
                "для получения historyManager", epic.getId());
        subtask.setId(3);

        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subtask);

        assertEquals(historyManager.getHistory(), List.of(task, epic, subtask));
    }

    @Test
    void checkThatHistoryIsEmpty() {
        List<Task> history = historyManager.getHistory();

        assertNotNull(history, "History Is null");
        assertTrue(history.isEmpty(), "History not null");
    }

    @Test
    void checkThatHistoryIsOne() {
        historyManager.add(task);
        historyManager.add(task);

        List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая");
        assertEquals(1, history.size(), "История не соответствует размеру");
    }

}
