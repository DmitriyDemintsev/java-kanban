package test;

import manager.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    void setUpFirst() {
        taskManager = new InMemoryTaskManager();
        init();
    }
}
