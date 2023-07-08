public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();

        Task task_1 = new Task("Задача №1", "Это описание задачи №1");
        Task task_2 = new Task("Задача №2", "Это описание задачи №2");

        taskManager.createTask(task_1);
        taskManager.createTask(task_2);

        System.out.println(taskManager.getTask(task_1.getTaskId()));
        System.out.println(taskManager.getTask(task_2.getTaskId()));
        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getHistory());
        System.out.println();

        Epic epic_1 = new Epic("Эпик_1", "Здесь описание для эпика_1");
        Epic epic_2 = new Epic("Эпик_2", "Здесь описание для эпика_2");

        taskManager.createEpic(epic_1);
        taskManager.createEpic(epic_2);

        System.out.println(taskManager.getEpic(epic_1.getTaskId()));
        System.out.println(taskManager.getEpic(epic_2.getTaskId()));
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getHistory());
        System.out.println();

        Subtask subtask_1_1 = new Subtask("Подзадача_1_1",
                "Здесь напишем, что хотим в подзадаче_1_1",
                 epic_1.getTaskId());
        Subtask subtask_1_2 = new Subtask("Подзадача_1_2",
                "Здесь напишем, что хотим в подзадаче_1_2",
                epic_1.getTaskId());
        Subtask subtask_2_1 = new Subtask("Подзадача_2_1",
                "Здесь напишем, что хотим в подзадаче_2_1",
             epic_2.getTaskId());

        taskManager.createSubtask(subtask_1_1);
        taskManager.createSubtask(subtask_1_2);
        taskManager.createSubtask(subtask_2_1);

        System.out.println(taskManager.getSubtask(subtask_1_1.getTaskId()));
        System.out.println(taskManager.getSubtask(subtask_1_2.getTaskId()));
        System.out.println(taskManager.getSubtask(subtask_2_1.getTaskId()));
        System.out.println(taskManager.getAllSubtasks());
        System.out.println(taskManager.getHistory());
        System.out.println();

        Task updateTask_1 = taskManager.getTask(task_1.getTaskId());
        updateTask_1.setTaskStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateTask(updateTask_1);

        System.out.println(taskManager.getTask(updateTask_1.getTaskId()));
        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getHistory());
        System.out.println();

        Epic updateEpic_1 = taskManager.getEpic(epic_1.getTaskId());
        updateEpic_1.setTaskStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateEpic(updateEpic_1);

        System.out.println(taskManager.getEpic(epic_2.getTaskId()));
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getHistory());
        System.out.println();

        Subtask updateSubtask_1_1 = taskManager.getSubtask(subtask_1_1.getTaskId());
        updateSubtask_1_1.setTaskStatus(TaskStatus.DONE);
        taskManager.updateSubtask(updateSubtask_1_1);

        Subtask updateSubtask_1_2 = taskManager.getSubtask(subtask_1_2.getTaskId());
        updateSubtask_1_2.setTaskStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateSubtask(updateSubtask_1_2);

        System.out.println(taskManager.getSubtask(updateSubtask_1_1.getTaskId()));
        System.out.println(taskManager.getSubtask(updateSubtask_1_2.getTaskId()));
        System.out.println(taskManager.getAllSubtasks());
        System.out.println(taskManager.getHistory());
        System.out.println();

        taskManager.dellTask(task_2.getTaskId());
        System.out.println(taskManager.getTask(task_2.getTaskId()));
        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getHistory());
        System.out.println();

        taskManager.dellEpic(epic_2.getTaskId());
        System.out.println(taskManager.getTask(epic_2.getTaskId()));
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getHistory());
        System.out.println();


    }
}
