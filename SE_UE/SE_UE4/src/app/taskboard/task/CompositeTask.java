package app.taskboard.task;

import java.util.ArrayList;
import java.util.List;

/**
 * Ein Composite Task besteht aus keinem, einen oder mehreren Tasks (Singletasks oder Compositetasks).
 */
public class CompositeTask extends Task {
    List<Task> subTasks = new ArrayList<>();

    public CompositeTask(String name, List<Task> tasks) {
        super(name);
        this.subTasks.addAll(tasks);
    }

    /**
     * Errechnet die geschätzte Gesamtzeit laut gespeicherten Attributen in diesem Task und den Kindertasks und geht dabei soweit in
     * die Tiefe wie möglich
     * @return
     */
    @Override
    public double getEstimatedDuration() {
        return this.subTasks.stream().map(Task::getEstimatedDuration).reduce(0.0, Double::sum);
    }

    /**
     * Errechnet die tatsächliche Zeit laut den via Entries eingetragen Zeiten in diesem Task und den Kindertasks und geht dabei soweit in
     * die Tiefe wie möglich
     * @return
     */    @Override
    public double getActualDuration() {
        return this.subTasks.stream().map(Task::getActualDuration).reduce(0.0,Double::sum);
    }

    @Override
    public List<Task> getChildren() {
        return this.subTasks;
    }


    public void addTask(Task task) {
        if (task.hasChild(this))
            throw new IllegalArgumentException("We are this tasks child, therefore he cannot become our child");
        this.subTasks.add(task);
    }
}
