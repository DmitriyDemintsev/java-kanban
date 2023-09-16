package model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Task {

    private String taskName;
    private String taskDescription;
    private int taskId;
    private TaskStatus taskStatus;
    private LocalDateTime startTime;
    private int duration;


    public Task(String taskName, String taskDescription) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskStatus = TaskStatus.NEW;
    }

    public Task(String taskName, String taskDescription, LocalDateTime startTime, int duration) { //new
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskStatus = TaskStatus.NEW;
        this.startTime = startTime;
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        if (startTime != null) {
            return startTime.plusMinutes(duration);
        } else {
            return null;
        }
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getName() {
        return taskName;
    }

    public void setName(String taskName) {
        this.taskName = taskName;
    }

    public void setStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getDescription() {
        return taskDescription;
    }

    public void setDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public int getId() {
        return taskId;
    }

    public int setId(int taskId) {
        this.taskId = taskId;
        return taskId;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public TaskType getTaskType() {
        return TaskType.TASK;
    }


    @Override
    public String toString() {
        return "Task{" +
                "taskName='" + taskName + '\'' +
                ", taskDescription='" + taskDescription + '\'' +
                ", taskStatus='" + taskStatus + '\'' +
                ", taskId=" + taskId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return taskId == task.taskId && duration == task.duration && Objects.equals(taskName, task.taskName) && Objects.equals(taskDescription, task.taskDescription) && taskStatus == task.taskStatus && Objects.equals(startTime, task.startTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskName, taskDescription, taskId, taskStatus, startTime, duration);
    }
}
