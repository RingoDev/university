package app.taskboard.task;

import java.util.ArrayList;
import java.util.List;

/**
 * Ein Singletask ist ein Task, der keine Sub., bzw Kindertasks hat und als Attribut eine Schätzung des für ihn
 * benötigten Aufwands beinhaltet
 */
public class SingleTask extends Task {

    double estimatedDuration;

    public SingleTask(String name, double duration) {
        super(name);
        this.estimatedDuration = duration;
    }

    @Override
    public double getEstimatedDuration() {
        return this.estimatedDuration;
    }

    @Override
    public double getActualDuration() {
       return this.getEntries().stream().map(Entry::getExpenditure).reduce(0.0,Double::sum);
    }

    /**
     * Ein Singletask hat keine ChildrenTasks, deswegen returnt er eine leere ArrayList
     * @return
     */
    @Override
    public List<Task> getChildren() {
        return new ArrayList<>();
    }

    @Override
    public String toString() {
        return super.toString() +
                ", estimatedDuration=" + estimatedDuration +
                '}';
    }

}
