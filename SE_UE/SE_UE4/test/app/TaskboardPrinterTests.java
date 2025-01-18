package app;

import app.developer.Developer;
import app.taskboard.Category;
import app.taskboard.Taskboard;
import app.taskboard.TaskboardPrinter;
import app.taskboard.task.CompositeTask;
import app.taskboard.task.SingleTask;
import app.taskboard.task.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests if
 */
public class TaskboardPrinterTests {
    Taskboard taskboard;
    Category open;
    Task task1;
    Task task2;


    @BeforeEach
    void initializeTaskboard() {
        this.taskboard = new Taskboard();
        this.open = new Category("open");
        this.task1 = new SingleTask("Datenmodell erweitern und Datenzugriff implementieren", 13);
        this.task2 = new CompositeTask("Retouren für Kunden zulassen", List.of(
                new SingleTask("Datenzugriff testen", 22),
                new CompositeTask("StampType und Nummernkreise mappen", List.of(
                        new SingleTask("Idle Animation neu erstellen",10)
                ))));
        open.addTask(task1);
        open.addTask(task2);
        taskboard.addCategory(open);
    }


    @Test
    void taskViewTest(){

        TaskboardPrinter printer = new TaskboardPrinter(taskboard);
        String s = printer.getTaskView();
        assertTrue( s.contains("Datenmodell erweitern und Datenzugriff implementieren"));
        assertTrue( s.contains("Retouren für Kunden zulassen"));
        assertTrue( s.contains("Datenzugriff testen"));
        assertTrue( s.contains("StampType und Nummernkreise mappen"));
        assertTrue( s.contains("Idle Animation neu erstellen"));

        assertTrue(s.contains("13"));
        assertTrue(s.contains("22"));
        assertTrue(s.contains("10"));

        assertDoesNotThrow((Executable) printer::printTaskView);

    }

    @Test
    void taskboardViewTest(){

        TaskboardPrinter printer = new TaskboardPrinter(taskboard);
        String s = printer.getTaskboardView();

        assertTrue(s.contains("open"));

        assertTrue( s.contains("Datenmodell erweitern und Datenzugriff implementieren"));
        assertTrue( s.contains("Retouren für Kunden zulassen"));
        assertTrue( s.contains("Datenzugriff testen"));
        assertTrue( s.contains("StampType und Nummernkreise mappen"));
        assertTrue( s.contains("Idle Animation neu erstellen"));

        assertDoesNotThrow((Executable) printer::printTaskboardView);
    }
    @Test
    void developerViewTest(){

        TaskboardPrinter printer = new TaskboardPrinter(taskboard);

        Developer dev = new Developer(1,"Thomas","Grininger");
        task1.addEntry(dev,42,new Date());
        String s = printer.getDeveloperView(List.of(dev));

        assertTrue(s.contains("open"));

        assertTrue( s.contains("Datenmodell erweitern und Datenzugriff implementieren"));
        assertFalse( s.contains("Retouren für Kunden zulassen"));
        assertFalse( s.contains("Datenzugriff testen"));
        assertFalse( s.contains("StampType und Nummernkreise mappen"));
        assertFalse( s.contains("Idle Animation neu erstellen"));

        assertDoesNotThrow(() -> printer.printDeveloperView(List.of(dev)));

    }
}
