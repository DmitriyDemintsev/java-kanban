public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        TaskManager taskManager = new TaskManager();

        Epic epic_1 = new Epic("Глобальная задача_1",
                "Тест трекера задач через Epic",
                "NEW",
                taskManager.generateId());

        Subtask subtask_1 = new Subtask("Подзадача_1",
                "Тест трекера задач через Subtask",
                "NEW",
                taskManager.generateId(),
                epic_1.taskId);

        Subtask subtask_2 = new Subtask("Подзадача_2",
                "Тест трекера задач через Subtask",
                "NEW",
                taskManager.generateId(),
                epic_1.taskId);

        Epic epic_2 = new Epic("Глобальная задача_2",
                "Тест трекера задач через Epic",
                "NEW",
                taskManager.generateId());

        Subtask subtask_2_1 = new Subtask("Подзадача_2_1",
                "Тест трекера задач через Subtask",
                "NEW",
                taskManager.generateId(),
                epic_2.taskId);

        Task task_1 = new Task("Просто задача_1",
                "Тест трекера задач через Task",
                "NEW",
                taskManager.generateId());

        Task task_2 = new Task("Просто задача_2",
                "Тест трекера задач через Task",
                "NEW",
                taskManager.generateId());

        taskManager.createEpic(epic_1);
        taskManager.createEpic(epic_2);
        taskManager.createTask(task_1);
        taskManager.createTask(task_2);
        taskManager.createSubtask(subtask_1);
        taskManager.createSubtask(subtask_2);
        taskManager.createSubtask(subtask_2_1);

        for (Epic epic : taskManager.getAllEpics()) {
            System.out.println(epic);
        }
        for (Subtask subtask : taskManager.getAllSubtasks()) {
            System.out.println(subtask);
        }
        for (Task task : taskManager.getAllTasks()) {
            System.out.println(task);
        }
        System.out.println();

        subtask_2_1 = new Subtask("Подзадача_2_1",
                "Меняем описание, смотрим результат",
                "DONE",
                subtask_2_1.taskId,
                epic_2.taskId);


        taskManager.updateEpic(epic_1);
        taskManager.updateEpic(epic_2);
        taskManager.updateTask(task_1);
        taskManager.updateTask(task_2);
        taskManager.updateSubtask(subtask_1);
        taskManager.updateSubtask(subtask_2);
        taskManager.updateSubtask(subtask_2_1);

        for (Epic epic : taskManager.getAllEpics()) {
            System.out.println(epic);
        }
        for (Subtask subtask : taskManager.getAllSubtasks()) {
            System.out.println(subtask);
        }
        for (Task task : taskManager.getAllTasks()) {
            System.out.println(task);
        }
        System.out.println();

        taskManager.dellEpic(epic_1.taskId);
        taskManager.dellTask(task_2.taskId);


        for (Epic epic : taskManager.getAllEpics()) {
            System.out.println(epic);
        }
        for (Subtask subtask : taskManager.getAllSubtasks()) {
            System.out.println(subtask);
        }
        for (Task task : taskManager.getAllTasks()) {
            System.out.println(task);
        }
    }
}
