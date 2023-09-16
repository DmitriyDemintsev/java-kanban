package test;

import client.KVTaskClient;
import manager.FileBackedTasksManager;
import manager.http.HttpTaskManager;
import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.KVServer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static model.TaskStatus.DONE;
import static model.TaskStatus.IN_PROGRESS;
import static org.junit.jupiter.api.Assertions.*;

class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {

    KVTaskClient client;

    private KVServer server;

    @BeforeEach
    public void setUp() throws IOException {
        server = new KVServer();
        server.start();
        client = new KVTaskClient("http://localhost:8078/");
        String url = "http://localhost:8078/";
        taskManager = new HttpTaskManager(url);
        init();
    }

    @AfterEach
    public void stop() {
        server.stop();
    }

    @Test
    public void loadFromServer() {
        client.save(HttpTaskManager.TASK_KEY, "[{\n" +
                "\t\"taskName\": \"Task example\",\n" +
                "\t\"taskDescription\": \"Task description\",\n" +
                "\t\"taskId\": 1,\n" +
                "\t\"taskStatus\": \"NEW\"\n" +
                "\t}\n]");

        client.save(HttpTaskManager.EPIC_KEY, "[{\n" +
                "\t\"taskName\": \"Epic example\",\n" +
                "\t\"taskDescription\": \"Epic description\",\n" +
                "\t\"taskId\": 2,\n" +
                "\t\"taskStatus\": \"IN_PROGRESS\"\n" +
                "\t},\n" +
                "\n" +
                "{\n" +
                "\t\"taskName\": \"Epic_Empty example\",\n" +
                "\t\"taskDescription\": \"Epic_Empty description\",\n" +
                "\t\"taskId\": 5,\n" +
                "\t\"taskStatus\": \"NEW\"\n" +
                "\t}\n]");

        client.save(HttpTaskManager.SUBTASK_KEY, "[{\n" +
                "\t\"taskName\": \"Subtask_1 example\",\n" +
                "\t\"taskDescription\": \"Subtask_1 description\",\n" +
                "\t\"taskId\": 3,\n" +
                "\t\"taskStatus\": \"NEW\",\n" +
                "\t\"epicId\": 2\n" +
                "\t},\n" +
                "\n" +
                "{\n" +
                "\t\"taskName\": \"Subtask_2 example\",\n" +
                "\t\"taskDescription\": \"Subtask_2 description\",\n" +
                "\t\"taskId\": 4,\n" +
                "\t\"taskStatus\": \"DONE\",\n" +
                "\t\"epicId\": 2\n" +
                "\t}\n]");

        client.save(HttpTaskManager.HISTORY_KEY, "[1,2,3,4,5]");


        taskManager = HttpTaskManager.loadFromServer("http://localhost:8078/");

        Task expectedTask = new Task("Task example", "Task description");
        expectedTask.setId(1);
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
        client.save(HttpTaskManager.TASK_KEY, "[]");
        client.save(HttpTaskManager.EPIC_KEY, "[]");
        client.save(HttpTaskManager.SUBTASK_KEY, "[]");
        client.save(HttpTaskManager.HISTORY_KEY, "[]");

        taskManager = HttpTaskManager.loadFromServer("http://localhost:8078/");

        assertTrue(taskManager.getHistory().isEmpty());
    }

    @Test
    public void checkEmptyFile() {
        client.save(HttpTaskManager.TASK_KEY, "[]");
        client.save(HttpTaskManager.EPIC_KEY, "[]");
        client.save(HttpTaskManager.SUBTASK_KEY, "[]");
        client.save(HttpTaskManager.HISTORY_KEY, "[]");

        taskManager = HttpTaskManager.loadFromServer("http://localhost:8078/");

        assertTrue(taskManager.getAllTasks().isEmpty());
        assertTrue(taskManager.getAllEpics().isEmpty());
        assertTrue(taskManager.getAllSubtasks().isEmpty());
    }
}
