package app;

import app.developer.Developer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import app.taskboard.Category;
import app.taskboard.Taskboard;
import app.taskboard.task.SingleTask;
import app.taskboard.task.Task;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class TaskboardTests {

    Taskboard taskboard;

    @BeforeEach
    void initializeTaskboard() {
        this.taskboard = new Taskboard();
    }

    @Test
    void taskboardIsCreatedEmpty() {
        assertTrue(this.taskboard.getCategories().isEmpty());
        assertTrue(this.taskboard.getTasks().isEmpty());
    }

    @Test
    void addCategory() {
        Category cat = new Category("Test");
        this.taskboard.addCategory(cat);

        assertSame(this.taskboard.getCategory("Test"), cat);
        assertEquals(this.taskboard.getCategories().size(), 1);
        assertTrue(this.taskboard.getCategories().contains(cat));
    }

    @Test
    void getCategoryByName() {
        Category cat = new Category("Test");
        this.taskboard.addCategory(cat);
        assertSame(this.taskboard.getCategory("Test"), cat);
    }

    @Test
    void removeCategory() {

        Category cat = new Category("Test");
        this.taskboard.addCategory(cat);

        Category cat2 = new Category("Test2");

        assertFalse(this.taskboard.removeCategory(cat2));
        assertTrue(this.taskboard.removeCategory(cat));

        assertTrue(this.taskboard.getCategories().isEmpty());
        assertFalse(this.taskboard.removeCategory(cat));
    }

    @Test
    void removeCategoryByName() {
        Category cat = new Category("Test");
        this.taskboard.addCategory(cat);

        new Category("Test2");

        assertFalse(this.taskboard.removeCategory("Test2"));
        assertTrue(this.taskboard.removeCategory("Test"));

        assertTrue(this.taskboard.getCategories().isEmpty());
        assertFalse(this.taskboard.removeCategory("Test"));
    }

    @Test
    void addDeveloperToTaskboard() {
        Developer dev = new Developer(0, "Thomas", "Grininger");
        this.taskboard.addDeveloper(dev);

        assertSame(this.taskboard.getDeveloper(0), dev);
        assertEquals(this.taskboard.getDevelopers().size(), 1);
        assertTrue(this.taskboard.getDevelopers().contains(dev));
    }

    @Test
    void getDeveloperById() {
        Developer dev = new Developer(0, "Thomas", "Grininger");
        this.taskboard.addDeveloper(dev);
        assertSame(this.taskboard.getDeveloper(0), dev);
    }

    @Test
    void removeDeveloperFromTaskboard() {
        Developer dev = new Developer(0, "Thomas", "Grininger");

        assertFalse(this.taskboard.removeDeveloper(dev));
        assertTrue(this.taskboard.getDevelopers().isEmpty());

        this.taskboard.addDeveloper(dev);
        assertTrue(this.taskboard.getDevelopers().contains(dev));

        assertTrue(this.taskboard.removeDeveloper(dev));
        assertTrue(this.taskboard.getDevelopers().isEmpty());
    }

    @Test
    void removeDeveloperFromTaskboardById() {
        Developer dev = new Developer(0, "Thomas", "Grininger");

        assertFalse(this.taskboard.removeDeveloper(0));
        assertTrue(this.taskboard.getDevelopers().isEmpty());

        this.taskboard.addDeveloper(dev);
        assertTrue(this.taskboard.getDevelopers().contains(dev));

        assertTrue(this.taskboard.removeDeveloper(0));
        assertTrue(this.taskboard.getDevelopers().isEmpty());
    }

    @Test
    void addDeveloperToTaskboardSameId() {
        Developer dev1 = new Developer(0, "Thomas", "Grininger");
        Developer dev2 = new Developer(0, "Dominik", "Fischer");

        this.taskboard.addDeveloper(dev1);
        assertThrows(IllegalArgumentException.class, () -> this.taskboard.addDeveloper(dev2));
        assertThrows(IllegalArgumentException.class, () -> this.taskboard.addDeveloper(dev1));
    }

    @Test
    void addCategorySameName() {
        Category cat1 = new Category("Test");
        Category cat2 = new Category("Test");

        this.taskboard.addCategory(cat1);
        assertThrows(IllegalArgumentException.class, () -> this.taskboard.addCategory(cat2));
        assertThrows(IllegalArgumentException.class, () -> this.taskboard.addCategory(cat1));
    }

    @Test
    void addTaskToCategory() {
        Category cat = new Category("Test");
        this.taskboard.addCategory(cat);
        Task task = new SingleTask("Testtask", 42);
        cat.addTask(task);

        assertEquals(this.taskboard.getTasks().size(), 1);
        assertTrue(this.taskboard.getTasks().contains(task));
        assertEquals(this.taskboard.getCategory("Test").getTasks().size(), 1);
        assertTrue(this.taskboard.getCategory("Test").getTasks().contains(task));
    }

    /**
     * tests if Task added to Taskboard without category lands in the unassigned section
     */
    @Test
    void addTaskToTaskboard() {
        Task task = new SingleTask("Testtask", 42);
        taskboard.addTask(task);

        assertEquals(this.taskboard.getTasks().size(), 1);
        assertTrue(this.taskboard.getTasks().contains(task));

        assertEquals(this.taskboard.getUnassignedTasks().size(), 1);
        assertTrue(this.taskboard.getUnassignedTasks().contains(task));
    }

    @Test
    void moveTaskBetweenCategories() {
        Task task = new SingleTask("TestTask", 42);
        Category cat = new Category("TestCategory");

        assertThrows(NoSuchElementException.class, () -> taskboard.moveTaskTo(task, cat));

        taskboard.addTask(task);

        assertThrows(NoSuchElementException.class, () -> taskboard.moveTaskTo(task, cat));

        taskboard.addCategory(cat);

        taskboard.moveTaskTo(task, cat);

        assertTrue(cat.getTasks().contains(task));
        assertTrue(this.taskboard.getTasks().contains(task));
    }

    @Test
    void moveTaskBetweenCategoriesById(){
        Task task = new SingleTask("TestTask", 42);
        Category cat = new Category("TestCategory");

        assertThrows(NoSuchElementException.class, () -> taskboard.moveTaskTo(task.getId(), "TestCategory"));

        taskboard.addTask(task);

        assertThrows(NoSuchElementException.class, () -> taskboard.moveTaskTo(task.getId(), "TestCategory"));

        taskboard.addCategory(cat);

        taskboard.moveTaskTo(task.getId(), "TestCategory");

        assertTrue(cat.getTasks().contains(task));
        assertTrue(this.taskboard.getTasks().contains(task));
    }
}
