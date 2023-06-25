public class Subtask extends Task{
    int epicId;

    public Subtask(String taskName, String taskDescription, String taskStatus, int taskId, int epicId) {
        super(taskName, taskDescription, taskStatus, taskId);
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "taskName='" + taskName + '\'' +
                ", taskDescription='" + taskDescription + '\'' +
                ", taskStatus='" + taskStatus + '\'' +
                ", taskId=" + taskId +
                '}';
    }
}
