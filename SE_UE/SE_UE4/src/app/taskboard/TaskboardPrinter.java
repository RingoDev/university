package app.taskboard;

import app.developer.Developer;
import app.filter.Filter;
import app.taskboard.task.Task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Ermöglicht die Ansicht(bzw. die Ausgabe) eines Taskboards in verschiedenen Sichten
 */
public class TaskboardPrinter {
    private Taskboard taskboard;

    public TaskboardPrinter(Taskboard taskboard) {
        this.taskboard = taskboard;
    }

    /**
     * Of each category in this Taskboard: Prints category information, then Prints its Tasks and one layer of their subtasks
     * Printed Results can be filtered by 0-unlimited Filters which are based on a functional interface
     *
     * @param filters
     */
    public void printTaskboardView(Filter... filters) {
        String s = getTaskboardView(filters);
        System.out.println(s);
    }


    /**
     * Prints this Taskboards Tasks and all of their subtasks recursively
     * Output contains information about the Task, Category, estimated and actual time used for the task
     * Printed Results can be filtered by 0-unlimited Filters which are based on a functional interface
     *
     * @param filters
     */
    public void printTaskView(Filter... filters) {
        String s = getTaskView(filters);
        print(s);
    }


    /**
     * Prints a Taskview, but only includes Tasks, which include an Entry by one-unlimited given developers
     * Printed Results can be filtered by 0-unlimited Filters which are based on a functional interface
     *
     * @param developers
     * @param filter
     */
    public void printDeveloperView(List<Developer> developers, Filter... filter) {
        String s = getDeveloperView(developers, filter);
        print(s);
    }

    public void print(String s) {
        System.out.println(s);
    }

    public String getDeveloperView(List<Developer> developers, Filter... inputFilter) {
        ArrayList<Filter> filters = new ArrayList<>(Arrays.asList(inputFilter));
        Filter f = t -> {
            for (Developer dev : developers) {
                if (t.getEntries().stream().anyMatch(e -> e.getDev() == dev)) return true;
            }
            return false;
        };
        filters.add(f);
        return getTaskView(filters.toArray(new Filter[0]));
    }

    public String getTaskView(Filter... filters) {
        StringBuilder sb = new StringBuilder();
        for (Category category : this.taskboard.categories) {
            for (Task a : category.getTasks()) {
                boolean validParent;
                validParent = isValid(a, true, filters);
                if (validParent)
                    sb.append("    ").append(a.toString()).append(" | ").append(category.toString()).append(" Estimated Duration: ").append(a.getEstimatedDuration())
                            .append(" Actual Duration: ").append(a.getActualDuration()).append("\n");

                getChildrenRecursively(sb, category, a, filters, 2);
            }

        }
        return sb.toString();
    }

    public String getTaskboardView(Filter... filters) {
        StringBuilder sb = new StringBuilder();
        for (Category category : this.taskboard.categories) {
            sb.append("==============================================\n")
                    .append(category.getName())
                    .append("\n");
            for (Task task : category.getTasks()) {
                boolean valid = isValid(task, true, filters);
                if (valid)
                    sb.append(task.getName()).append("\n");
                    if (!task.getChildren().isEmpty()) this.getChildTasksRecursively(sb, category, task, filters, 2);
            }
        }
        return sb.toString();
    }

    private void getChildTasksRecursively(StringBuilder sb, Category category, Task a, Filter[] filters, int level) {
        for (Task task : a.getChildren()) {
            boolean valid = isValid(task, true, filters);
            if (valid)
                sb.append("    ".repeat(level)).append(task.getName()).append("\n");
            if (!task.getChildren().isEmpty()) this.getChildTasksRecursively(sb, category, task, filters, level + 1);
        }
    }

    /**
     * Recursive helper Function for hierarchy levels
     *
     * @param sb       the StringBuilder we append recursively to
     * @param category
     * @param a
     * @param filters
     * @param level
     */
    private void getChildrenRecursively(StringBuilder sb, Category category, Task a, Filter[] filters, int level) {
        for (Task task : a.getChildren()) {
            boolean valid = isValid(task, true, filters);
            if (valid)
                sb.append("    ".repeat(level)).append(task.toString()).append(" | ").append(category.toString()).append(" Estimated Duration: ").append(task.getEstimatedDuration())
                        .append(" Actual Duration: ").append(task.getActualDuration()).append("\n");
            if (!task.getChildren().isEmpty()) this.getChildrenRecursively(sb, category, task, filters, level + 1);
        }
    }


    /**
     * Does a given task match the filters
     *
     * @param task
     * @param valid
     * @param filters
     * @return
     */
    protected boolean isValid(Task task, boolean valid, Filter[] filters) {
        for (Filter filter : filters) {
            if (!filter.apply(task))
                valid = false;
        }
        return valid;
    }
}
