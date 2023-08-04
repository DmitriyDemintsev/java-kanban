package model;

import java.util.ArrayList;

public class Epic extends Task{

    private ArrayList<Integer> subtaskIds;

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

    public void addSubtaskId(Subtask subtask) {
        subtaskIds.add(subtask.getTaskId());
    }

    public void deleteSubtaskId (Integer subtaskId) {
        subtaskIds.remove(subtaskId);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "taskName='" + getTaskName() + '\'' +
                ", taskDescription='" + getTaskDescription() + '\'' +
                ", taskStatus='" + getTaskStatus() + '\'' +
                ", taskId=" + getTaskId() +
                '}';
    }
}
