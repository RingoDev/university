package app.taskboard.task;

import app.developer.Developer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Ein Task hat eine ID, einen Namen und Zeitdokumentationseinträge
 */
public abstract class Task {
    private static int counter = 0;
    private final String name;
    private final int id;
    private final List<Entry> entries = new ArrayList<>();

    Task(String name) {
        this.name = name;
        id = counter;
        counter++;
    }

    public List<Entry> getEntries() {
        return this.entries;
    }

    public void addEntry(Developer dev, double expenditure, Date date) {
        this.entries.add(new Entry(dev, expenditure, date));
    }

    public String getName() {
        return this.name;
    }

    public int getId() {
        return this.id;
    }

    /**
     * Zeitschätzung
     * @return
     */
    public abstract double getEstimatedDuration();

    /**
     * Tatsächlich verbuchte Zeit
     * @return
     */
    public abstract double getActualDuration();

    /**
     * Beinhaltet der Task einen spezifischen Subtask?
     * @param task
     * @return
     */
    boolean hasChild(Task task) {
        for (Task t : this.getChildren()) {
            if(t == task) return true;
            if (t.hasChild(task)) return true;
        }
        return false;
    }

    public abstract List<Task> getChildren();

    @Override
    public String toString() {
        return
                 name + '\'' +
                ", id=" + id ;
    }
}
