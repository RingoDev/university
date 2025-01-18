package app.taskboard;

import app.taskboard.task.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * Kategorien beinhalten Tasks.
 */
public class Category {

    List<Task> tasks = new ArrayList<>();
    String name;

    public Category(String name) {
        if (name == null) throw new NullPointerException("Name of Category can't be null");
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    void removeTask(Task task) {
       tasks.remove(task);
    }

    public List<Task> getTasks() {
        return tasks;
    }

    @Override
    public String toString() {
        return "Category{" +
                ", name='" + name + '\'' +
                '}';
    }
}
