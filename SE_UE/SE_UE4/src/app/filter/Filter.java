package app.filter;

import app.taskboard.task.Task;

/**
 * Generischer Interface um mittels einer Methode einen Task zu Filtern
 */
@FunctionalInterface
public interface Filter {
    boolean apply(Task t);
}
