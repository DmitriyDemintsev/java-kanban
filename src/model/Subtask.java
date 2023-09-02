package model;

import java.time.LocalDateTime;

public class Subtask extends Task {

    private int epicId;

    public Subtask(String taskName, String taskDescription, int epicId) {
        super(taskName, taskDescription);
        this.epicId = epicId;
    }

    public Subtask(String taskName, String taskDescription, int epicId, LocalDateTime startTime, int duration) {
        super(taskName, taskDescription, startTime, duration);
        this.epicId = epicId;
    }


    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.SUBTASK;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "taskName='" + getName() + '\'' +
                ", taskDescription='" + getDescription() + '\'' +
                ", taskStatus='" + getTaskStatus() + '\'' +
                ", taskId=" + getId() +
                '}';
    }
}
