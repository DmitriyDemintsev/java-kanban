import java.util.ArrayList;

public class Epic extends Task{
    public ArrayList<Integer> subtaskIds;

    public Epic(String taskName, String taskDescription, String taskStatus, int taskId) {
        super(taskName, taskDescription, taskStatus, taskId);
        this.subtaskIds = new ArrayList<>();
    }
    @Override
    public String toString() {
        return "Epic{" +
                "taskName='" + taskName + '\'' +
                ", taskDescription='" + taskDescription + '\'' +
                ", taskStatus='" + taskStatus + '\'' +
                ", taskId=" + taskId +
                '}';
    }

}
