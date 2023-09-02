package manager;

import model.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CSVFormatHandler {

    private static final String DELIMITER = ",";

    String toString(Task task) {
        String result = task.getId() + DELIMITER +
                task.getTaskType() + DELIMITER +
                task.getName() + DELIMITER +
                task.getTaskStatus() + DELIMITER +
                task.getDescription() + DELIMITER +
                task.getStartTime() + DELIMITER +
                task.getDuration() + DELIMITER;
        if(task.getTaskType() == TaskType.SUBTASK) {
            result = result + ((Subtask) task).getEpicId();
        }
         return result;

    }

    Task fromString(String value) {
        String[] parts = value.split(",");
        TaskType type = TaskType.valueOf(parts[1].toUpperCase());
        if(type == TaskType.TASK) {
            Task task = new Task(parts[2], parts[4]);
            task.setId(Integer.valueOf(parts[0]));
            task.setStatus(TaskStatus.valueOf(parts[3].toUpperCase()));
            task.setStartTime(LocalDateTime.parse(parts[5]));
            task.setDuration(Integer.valueOf(parts[6]));
            return task;
        }
        if(type == TaskType.EPIC) {
            Epic epic = new Epic(parts[2], parts[4]);
            epic.setId(Integer.valueOf(parts[0]));
            epic.setStatus(TaskStatus.valueOf(parts[3].toUpperCase()));
            epic.setStartTime(LocalDateTime.parse(parts[5]));
            epic.setDuration(Integer.valueOf(parts[6]));
            return epic;
        }
        if(type == TaskType.SUBTASK) {
            Subtask subtask = new Subtask(parts[2], parts[4], Integer.valueOf(parts[7]));
            subtask.setId(Integer.valueOf(parts[0]));
            subtask.setStatus(TaskStatus.valueOf(parts[3].toUpperCase()));
            subtask.setStartTime(LocalDateTime.parse(parts[5]));
            subtask.setDuration(Integer.valueOf(parts[6]));
            return subtask;
        }

        throw new IllegalArgumentException("Не найдены элементы указанного типа");
    }

    String historyToString(HistoryManager historyManager) {
        List<String> result = new ArrayList<>();

        for (Task task : historyManager.getHistory()) {
            result.add(String.valueOf(task.getId()));
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
        return "id,type,name,status,description,startTime,duration,epic\n";
    }

}
