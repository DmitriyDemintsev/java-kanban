package test;

import manager.FileBackedTasksManager;
import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static model.TaskStatus.DONE;
import static model.TaskStatus.IN_PROGRESS;
import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    @BeforeEach
    public void setUp() {
        String fileName = "/dev/null";
        taskManager = new FileBackedTasksManager(fileName);
        init();
    }

    @Test
    public void loadFromFile() {
        taskManager = FileBackedTasksManager.loadFromFile("src/test/test.csv");

        Task expectedTask = new Task("Task example", "Task description");
        expectedTask.setId(1);
        expectedTask.setStartTime(LocalDateTime.parse("2023-09-02T12:10:45.941608"));
        expectedTask.setDuration(10);
        Task actualTask = taskManager.getTask(1);
        assertNotNull(actualTask);
        assertEquals(expectedTask.getId(), actualTask.getId());
        assertEquals(expectedTask.getName(), actualTask.getName());
        assertEquals(expectedTask.getDescription(), actualTask.getDescription());
        assertEquals(expectedTask.getTaskStatus(), actualTask.getTaskStatus());
        assertEquals(expectedTask.getStartTime(), actualTask.getStartTime());
        assertEquals(expectedTask.getDuration(), actualTask.getDuration());

        Epic expectedEpic = new Epic("Epic example", "Epic description");
        expectedEpic.setId(2);
        expectedEpic.setStartTime(LocalDateTime.parse("2023-09-02T12:10:50.941608"));
        expectedEpic.setDuration(20);
        expectedEpic.setStatus(IN_PROGRESS);
        expectedEpic.addSubtaskId(3);
        expectedEpic.addSubtaskId(4);
        Epic actualEpic = taskManager.getEpic(2);
        assertNotNull(actualEpic);
        assertEquals(expectedEpic.getId(), actualEpic.getId());
        assertEquals(expectedEpic.getName(), actualEpic.getName());
        assertEquals(expectedEpic.getDescription(), actualEpic.getDescription());
        assertEquals(expectedEpic.getTaskStatus(), actualEpic.getTaskStatus());
        assertEquals(expectedEpic.getSubtaskIds(), actualEpic.getSubtaskIds());
        assertEquals(expectedEpic.getStartTime(), actualEpic.getStartTime());
        assertEquals(expectedEpic.getDuration(), actualEpic.getDuration());


        Subtask expectedSubtask_1 = new Subtask("Subtask_1 example",
                "Subtask_1 description", 2);
        expectedSubtask_1.setId(3);
        expectedSubtask_1.setStartTime(LocalDateTime.parse("2023-09-02T12:55:45.941608"));
        expectedSubtask_1.setDuration(40);
        Subtask actualSubtask_1 = taskManager.getSubtask(3);
        assertNotNull(actualSubtask_1);
        assertEquals(expectedSubtask_1.getId(), actualSubtask_1.getId());
        assertEquals(expectedSubtask_1.getName(), actualSubtask_1.getName());
        assertEquals(expectedSubtask_1.getDescription(), actualSubtask_1.getDescription());
        assertEquals(expectedSubtask_1.getTaskStatus(), actualSubtask_1.getTaskStatus());
        assertEquals(expectedSubtask_1.getEpicId(), actualSubtask_1.getEpicId());
        assertEquals(expectedSubtask_1.getStartTime(), actualSubtask_1.getStartTime());
        assertEquals(expectedSubtask_1.getDuration(), actualSubtask_1.getDuration());


        Subtask expectedSubtask_2 = new Subtask("Subtask_2 example",
                "Subtask_2 description", 2);
        expectedSubtask_2.setId(4);
        expectedSubtask_2.setStartTime(LocalDateTime.parse("2023-09-02T12:56:45.941608"));
        expectedSubtask_2.setDuration(50);
        expectedSubtask_2.setStatus(DONE);
        Subtask actualSubtask_2 = taskManager.getSubtask(4);
        assertNotNull(actualSubtask_2);
        assertEquals(expectedSubtask_2.getId(), actualSubtask_2.getId());
        assertEquals(expectedSubtask_2.getName(), actualSubtask_2.getName());
        assertEquals(expectedSubtask_2.getDescription(), actualSubtask_2.getDescription());
        assertEquals(expectedSubtask_2.getTaskStatus(), actualSubtask_2.getTaskStatus());
        assertEquals(expectedSubtask_2.getEpicId(), actualSubtask_2.getEpicId());
        assertEquals(expectedSubtask_2.getStartTime(), actualSubtask_2.getStartTime());
        assertEquals(expectedSubtask_2.getDuration(), actualSubtask_2.getDuration());


        Epic expectedEpicEmpty = new Epic("Epic_Empty example",
                "Epic_Empty description");
        expectedEpicEmpty.setId(5);
        expectedEpicEmpty.setStartTime(LocalDateTime.parse("2023-09-02T12:51:45.941608"));
        expectedEpicEmpty.setDuration(50);

        Epic actualEpicEmpty = taskManager.getEpic(5);
        assertNotNull(actualEpicEmpty);
        assertEquals(expectedEpicEmpty.getId(), actualEpicEmpty.getId());
        assertEquals(expectedEpicEmpty.getName(), actualEpicEmpty.getName());
        assertEquals(expectedEpicEmpty.getDescription(), actualEpicEmpty.getDescription());
        assertEquals(expectedEpicEmpty.getTaskStatus(), actualEpicEmpty.getTaskStatus());
        assertEquals(expectedEpicEmpty.getSubtaskIds(), actualEpicEmpty.getSubtaskIds());
        assertEquals(expectedEpic.getStartTime(), actualEpic.getStartTime());
        assertEquals(expectedEpic.getDuration(), actualEpic.getDuration());

        assertTrue(expectedEpicEmpty.getSubtaskIds().isEmpty(), "В Epic_Empty есть подзадачи");

        assertEquals(taskManager.getHistory(), List.of(actualTask, actualEpic, actualSubtask_1,
                actualSubtask_2, actualEpicEmpty));
    }

    @Test
    public void checkEmptyHistory() {
        taskManager = FileBackedTasksManager.loadFromFile("src/test/testEmptyHistory.csv");

        assertTrue(taskManager.getHistory().isEmpty());
    }

    @Test
    public void checkEmptyFile() {
        taskManager = FileBackedTasksManager.loadFromFile("src/test/testEmptyFile.csv");

        assertTrue(taskManager.getAllTasks().isEmpty());
        assertTrue(taskManager.getAllEpics().isEmpty());
        assertTrue(taskManager.getAllSubtasks().isEmpty());
    }
}
