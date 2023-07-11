package manager;
import model.Task;


import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    static final int LIST_LENGTH = 10;
    private final LinkedList<Task> historyStorage = new LinkedList<>();

    @Override
    public void add(Task task) {
        if (historyStorage.size() == LIST_LENGTH) {
            historyStorage.removeFirst();
        }
        historyStorage.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return new LinkedList<>(historyStorage);
    }
}
