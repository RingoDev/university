package app.taskboard;

import app.developer.Developer;
import app.taskboard.task.Task;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Ein Taskboard beinhaltet Kategorien(welche Tasks beinhalten)
 */
public class Taskboard {
    List<Category> categories = new ArrayList<>();
    List<Developer> developers = new ArrayList<>();
    Category unassigned = new Category("unassigned");

    public Taskboard() {
    }

    public List<Developer> getDevelopers() {
        return this.developers;
    }

    public List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        tasks.addAll(this.categories.stream().flatMap(c -> c.getTasks().stream()).collect(Collectors.toList()));
        tasks.addAll(this.unassigned.getTasks());
        return tasks;
    }

    public List<Category> getCategories() {
        return this.categories;
    }

    public void addCategory(Category category) {
        if (categories.stream().anyMatch(c -> c.getName().equals(category.getName())))
            throw new IllegalArgumentException("Category with name " + category.getName() + " exists already.");
        categories.add(category);
    }

    public boolean removeCategory(String name) {
        Optional<Category> optionalCategory = categories.stream().filter(c -> c.name.equals(name)).findFirst();
        if (optionalCategory.isEmpty()) return false;
        Category toRemove = optionalCategory.get();
        toRemove.tasks.forEach(t -> unassigned.addTask(t));
        return this.categories.remove(toRemove);
    }

    public boolean removeCategory(Category cat) {
        if (this.categories.contains(cat)) {
            cat.tasks.forEach(t -> unassigned.addTask(t));
        }
        return this.categories.remove(cat);
    }

    public Category getCategory(String name) {
        return categories.stream().filter(c -> c.getName().equals(name))
                .findFirst().orElseThrow(() -> new NoSuchElementException("taskboard.Category with name " + name + " doesn't exist."));
    }

    public void addDeveloper(Developer dev) {
        if (this.developers.stream().anyMatch(d -> d.getId() == dev.getId())) {
            throw new IllegalArgumentException("Developer with Id exists already!");
        }
        this.developers.add(dev);
    }

    // developer will only be removed from list and still exist in entries she/he already worked on
    public boolean removeDeveloper(int id) {
        if (this.developers.stream().anyMatch(d -> d.getId() == id)) {
            this.developers = this.developers.stream().filter(d -> d.getId() != id).collect(Collectors.toList());
            return true;
        }
        return false;
    }

    public boolean removeDeveloper(Developer dev) {
        return this.developers.remove(dev);
    }

    public void addTask(Task task) {
        unassigned.addTask(task);
    }

    public List<Task> getUnassignedTasks() {
        return this.unassigned.getTasks();
    }

    public void moveTaskTo(int taskId, String categoryName) throws NoSuchElementException {
        Task task = this.getTasks().stream().filter(t -> t.getId() == taskId).findFirst().orElseThrow(() -> new NoSuchElementException("Task with id " + taskId + " doesn't exist and can't be moved"));
        Category category = this.categories.stream().filter(c -> c.getName().equals(categoryName)).findFirst().orElseThrow(() -> new NoSuchElementException("Category with name " + categoryName + " doesn't exist in taskboard and can't be moved into"));
        this.moveTaskTo(task, category);
    }

    public void moveTaskTo(Task task, Category category) throws NoSuchElementException {
        if (!this.getTasks().contains(task))
            throw new NoSuchElementException("Task doesn't exist in taskboard and can't be moved");
        if (!this.categories.contains(category))
            throw new NoSuchElementException("Category doesn't exist in taskboard and can't be moved into");

        this.categories.forEach(c -> c.removeTask(task));
        this.unassigned.removeTask(task);
        category.addTask(task);
    }

    public Developer getDeveloper(int id) throws NoSuchElementException {
        return this.developers.stream().filter(d -> d.getId() == id).findFirst().orElseThrow(() -> new NoSuchElementException("Developer with id " + id + " doesn't exist"));
    }


}
