package app;

import org.junit.jupiter.api.Test;
import app.taskboard.task.CompositeTask;
import app.taskboard.task.SingleTask;
import app.taskboard.task.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TaskTests {

    @Test
    void taskIsCreatedEmpty() {
        Task task = new SingleTask("TestTask", 0.0);
        assertTrue(task.getEntries().isEmpty());
        assertEquals(task.getName(), "TestTask");
        assertEquals(task.getEstimatedDuration(), 0);
    }

    @Test
    void createTaskWithSubtasks() {
        Task task1 = new SingleTask("Task1", 0);
        Task task2 = new SingleTask("Task2", 0);
        Task task3 = new SingleTask("Task4", 0);
        Task task4 = new CompositeTask("Task3", List.of(task3));

        Task testTask = new CompositeTask("TestTask", List.of(task1, task2, task4));

        assertEquals(testTask.getChildren().size(), 3);
        assertTrue(testTask.getChildren().contains(task1));
        assertTrue(testTask.getChildren().contains(task2));
        assertFalse(testTask.getChildren().contains(task3));
        assertTrue(testTask.getChildren().contains(task4));
    }

    @Test
    void addSubtaskToTask() {
        CompositeTask task = new CompositeTask("Task", List.of());
        Task task2 = new SingleTask("Task2", 0);
        task.addTask(task2);

        assertTrue(task.getChildren().contains(task2));
        assertEquals(task.getChildren().size(), 1);
    }

    @Test
    void estimatedDurationGetsAdded() {
        double duration1 = 2.2;
        double duration2 = 4.3;
        Task task = new CompositeTask("TestTask",
                List.of(new SingleTask("Task1", duration1), new SingleTask("Task2", duration2)));
        assertEquals(duration1 + duration2, task.getEstimatedDuration());
    }

    @Test
    void noDeepCircularChildren() {

        CompositeTask child = new CompositeTask("Task2", List.of());

        CompositeTask parent = new CompositeTask("TestTask",
                List.of(new SingleTask("decoy", 0),
                        child));

        CompositeTask grandParent = new CompositeTask("TestTask2", List.of(parent));

        assertThrows(IllegalArgumentException.class, () -> child.addTask(grandParent));

    }
}
