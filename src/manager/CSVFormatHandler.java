package manager;

import model.*;
import java.util.ArrayList;
import java.util.List;

public class CSVFormatHandler {

    private static final String DELIMITER = ",";

    String toString(Task task) {
        String result = task.getTaskId() + DELIMITER +
                task.getTaskType() + DELIMITER +
                task.getTaskName() + DELIMITER +
                task.getTaskStatus() + DELIMITER +
                task.getTaskDescription() + DELIMITER;
        if(task.getTaskType() == TaskType.SUBTASK) {
            result = result + ((Subtask) task).getEpicId();
        }
         return result;

    }

    Task fromString(String value) {
        String[] parts = value.split(",");
        if(parts[1].toUpperCase().equals(TaskType.TASK.name())) {
            Task task = new Task(parts[2], parts[4]);
            task.setTaskId(Integer.valueOf(parts[0]));
            task.setTaskStatus(TaskStatus.valueOf(parts[3].toUpperCase()));
            return task;
        }
        if(parts[1].toUpperCase().equals(TaskType.EPIC.name())) {
            Epic epic = new Epic(parts[2], parts[4]);
            epic.setTaskId(Integer.valueOf(parts[0]));
            epic.setTaskStatus(TaskStatus.valueOf(parts[3].toUpperCase()));
            return epic;
        }
        if(parts[1].toUpperCase().equals(TaskType.SUBTASK.name())) {
            Subtask subtask = new Subtask(parts[2], parts[4], Integer.valueOf(parts[5]));
            subtask.setTaskId(Integer.valueOf(parts[0]));
            subtask.setTaskStatus(TaskStatus.valueOf(parts[3].toUpperCase()));
            return subtask;
        }
        throw new IllegalArgumentException("Не найдены элементы указанного типа");
    }

    String historyToString(HistoryManager historyManager) {
        List<String> result = new ArrayList<>();

        for (Task task : historyManager.getHistory()) {
            result.add(String.valueOf(task.getTaskId()));
        }
        return String.join(DELIMITER, result);
    }

    List<Integer> historyFromString(String value) {
        String[] parts = value.split(",");

        List<Integer> id = new ArrayList<>();
        for (String part: parts) {
            id.add(Integer.valueOf(part));
        }
        return id;
    }

    String getHeader() {
        return "id,type,name,status,description,epic\n";
    }

}
