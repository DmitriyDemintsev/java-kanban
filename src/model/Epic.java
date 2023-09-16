package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task{

    private ArrayList<Integer> subtaskIds;
    private LocalDateTime endTime;

    public Epic(String taskName, String taskDescription) {
        super(taskName, taskDescription);
        subtaskIds = new ArrayList<>();
    }

    public ArrayList<Integer> getSubtaskIds() {
        return new ArrayList<>(subtaskIds);
    }

    public void setSubtaskIds(ArrayList<Integer> subtaskIds) {
        this.subtaskIds = subtaskIds;
    }

    public void addSubtaskId(int subtaskId) {
        subtaskIds.add(subtaskId);
    }

    public void deleteSubtaskId(Integer subtaskId) {
        subtaskIds.remove(subtaskId);
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.EPIC;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "taskName='" + getName() + '\'' +
                ", taskDescription='" + getDescription() + '\'' +
                ", taskStatus='" + getTaskStatus() + '\'' +
                ", taskId=" + getId() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subtaskIds, epic.subtaskIds) && Objects.equals(endTime, epic.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtaskIds, endTime);
    }
}
