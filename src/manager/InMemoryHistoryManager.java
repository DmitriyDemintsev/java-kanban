package manager;

import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    final Map<Integer, Node> taskMap = new HashMap<>();
    private Node head;
    private Node tail;

    @Override
    public void remove(int id) {
        if (taskMap.containsKey(id)) {
            removeNode(taskMap.get(id));
            taskMap.remove(id);
        }
    }

    @Override
    public void add(Task task) {
        if (taskMap.containsKey(task.getId())) {
            removeNode(taskMap.get(task.getId()));
        }

        Node node = new Node(task);
        linkLast(node);

        taskMap.put(task.getId(), node);
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    private List<Task> getTasks() {
        List<Task> result = new ArrayList<>();
        Node current = head;

        while (current != null) {
            result.add(current.getItem());
            current = current.next;
        }
        return result;
    }

    private void removeNode(Node node) {
        if (node == head && node == tail) {
            head = null;
            tail = null;
        } else if (node == head) {
            head = node.next;
            head.prev = null;
        } else if (node == tail) {
            tail = node.prev;
            tail.next = null;
        } else {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }
    }

    private void linkLast(Node node) {
        final Node last = tail;
        tail = node;
        node.prev = last;
        if (last == null) {
            head = node;
        } else {
            last.next = node;
        }
    }

    private static class Node {
        Task item;
        Node next;
        Node prev;

        public Node(Task element) {
            this.item = element;
        }

        public Task getItem() {
            return item;
        }

        public Node getNext() {
            return next;
        }

        public void setNext(Node next) {
            this.next = next;
        }

        public Node getPrev() {
            return prev;
        }

        public void setPrev(Node prev) {
            this.prev = prev;
        }
    }
}
