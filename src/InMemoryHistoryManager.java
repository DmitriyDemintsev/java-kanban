import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {

    private final ArrayList<Task> historyStorage = new ArrayList<>();

    @Override
    public void add(Task Task) {
        if (historyStorage.size() < 10) {
            historyStorage.add(Task);
        } else if (historyStorage.size() == 10) {
            historyStorage.remove(0);
            historyStorage.add(Task);
        }
    }

    @Override
    public ArrayList<Task> getHistory() {
        return historyStorage;
    }
}
