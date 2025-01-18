import app.developer.Developer;
import app.filter.Filter;
import app.taskboard.TaskboardPrinter;
import app.taskboard.task.CompositeTask;
import app.taskboard.task.SingleTask;
import app.taskboard.task.Task;
import app.taskboard.Category;
import app.taskboard.Taskboard;

import java.util.Date;
import java.util.List;

public class App {
    public static void main(String[] args) {

        // setting up developers
        DeveloperStorage.addDeveloper(new Developer(0, "Thomas", "Grininger"));
        DeveloperStorage.addDeveloper(new Developer(1, "Christopher", "Gusenbauer"));
        DeveloperStorage.addDeveloper(new Developer(2, "Dominik", "Fischer"));
        DeveloperStorage.addDeveloper(new Developer(3, "Rainer", "Weinreich"));
        DeveloperStorage.addDeveloper(new Developer(4, "Stefan", "Schiffer"));

        // creating new taskboard
        Taskboard tb = new Taskboard();

        DeveloperStorage.getDevelopers().forEach(tb::addDeveloper);

        Category open = new Category("open");
        Category active = new Category("active");
        Category done = new Category("done");

        tb.addCategory(open);
        tb.addCategory(active);
        tb.addCategory(done);

        Task task1 = new SingleTask("Datenmodell erweitern und Datenzugriff implementieren", 13);
        Task task2 = new SingleTask("Idle Animation neu erstellen", 44);
        Task task3 = new SingleTask("Restaufwand prüfen", 3.5);
        Task task4 = new CompositeTask("Retouren für Kunden zulassen", List.of(
                new SingleTask("Datenzugriff testen", 22),
                new CompositeTask("StampType und Nummernkreise mappen", List.of(new SingleTask("Very deep nested Task",0)))));

        open.addTask(task1);
        open.addTask(task2);
        active.addTask(task3);
        done.addTask(task4);

        task1.addEntry(tb.getDeveloper(1), 2.4, new Date());
        task2.addEntry(tb.getDeveloper(0),2.1,new Date());
        System.out.println("TASKBOARDVIEW");
        TaskboardPrinter tbp = new TaskboardPrinter(tb);
        tbp.printTaskboardView();

        // only keeps Tasks who contain Datenzugriff in the Name or in their Subtasks name
        Filter filter1 = t -> t.toString().contains("Datenzugriff");

        Filter filter2 = t -> t instanceof SingleTask;
        System.out.println("Datenzugriffilter");
        tbp.printTaskboardView(filter1);
        System.out.println("Singletask");

        tbp.printTaskboardView(filter2);
        System.out.println("Beide Filter");

        tbp.printTaskboardView(filter1, filter2);

        System.out.println("TASKVIEW");
        tbp.printTaskView();
        System.out.println("Datenzugriffilter");
        tbp.printTaskView(filter1);
        System.out.println("Singletask");

        tbp.printTaskView(filter2);
        System.out.println("Beide Filter");

        tbp.printTaskView(filter1, filter2);
        System.out.println("DEVELOPERVIEW");
        System.out.println("Dev 0");
        tbp.printDeveloperView(List.of(DeveloperStorage.getDeveloper(0)));
        System.out.println("Dev 0,1");
        tbp.printDeveloperView(List.of(DeveloperStorage.getDeveloper(0),DeveloperStorage.getDeveloper(1)));
        System.out.println("Dev 0 with filters");
        tbp.printDeveloperView(List.of(DeveloperStorage.getDeveloper(0)),filter1);

        tbp.printDeveloperView(List.of(DeveloperStorage.getDeveloper(0)),filter2);

        tbp.printDeveloperView(List.of(DeveloperStorage.getDeveloper(0)),filter1, filter2);

    }
}
