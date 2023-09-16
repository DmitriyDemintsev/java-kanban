package manager;

import manager.http.HttpTaskManager;

public final class Managers {

    /*public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }*/
    public static TaskManager getDefault() {
        return new HttpTaskManager("http://localhost:8078/");
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
