package model;

import java.util.ArrayList;

public class Epic extends Task{

    private ArrayList<Integer> subtaskIds;

    public Epic(String taskName, String taskDescription) {
        super(taskName, taskDescription);
        subtaskIds = new ArrayList<>();
    }
    public ArrayList<Integer> getSubtaskIds() {
        return subtaskIds;
    }
    public void setSubtaskIds(ArrayList<Integer> subtaskIds) {
        this.subtaskIds = subtaskIds;
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
