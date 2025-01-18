package todolist;


import todolist.Todo;
import todolist.Node;

import java.time.LocalDate;


public final class TodoManager {
    private Node head;
    private int nrTodo;


    public TodoManager() {
        head = null;
        nrTodo = 0;
    }


    public void insertTodo(String desc, int year, int month, int day) {
        insertTodo(new Todo(desc, LocalDate.of(year, month, day), nrTodo));
        nrTodo += 1;
    }


    private void insertTodo(Todo todo) {
        Node pre = null;
        Node temp = head;

        for (; temp != null && todo.getDate().isAfter(temp.todo.getDate()); temp = temp.next) {
            pre = temp;
        }
            // find correct position to insert new todo}

        Node node = new Node(todo);

        if (pre == null) {//insert at first position

            node.next = head;
            head = node;

        } else {//inserts Node with Todo into List

            pre.next = node;
            node.next = temp;
        }
    }


    public Todo searchByID(int id) {//returns a Todo with a certain ID
        Node temp = head;

        while (temp != null) {

            if (temp.todo.getID() == id) {
                return temp.todo;

            } else {
                temp = temp.next;
            }

        }
        return null;//returns null if ID doesn't exist
    }

    public void deleteDoneUntil(LocalDate date) {//deletes Nodes until certain Date which are Done
        Node temp = head;
        Node pre = null;

        for (; temp != null; temp = temp.next) {
            if ((date.isAfter(temp.todo.getDate()) || date.isEqual(temp.todo.getDate())) && temp.todo.getStatus() == Status.Done) {
                if (pre == null) {//delete first Node
                    head = temp.next;
                    pre = null;
                } else {//delete Node between Nodes
                    pre.next = temp.next;
                }
            } else {
                pre = temp;
            }
        }
    }


    public Todo[] getAll() {//returns all Todos
        return get(null, null);
    }

    public Todo[] getUntil(LocalDate date) {//returns Todos with earlier dates
        return get(date, null);
    }

    public Todo[] getDone() {//returns all Done Todos
        return get(null, Status.Done);
    }

    public Todo[] getOpen() {//returns all Open Todos
        return get(null, Status.Open);
    }

    public Todo[] getOpenUntil(LocalDate date) {//returns all Open Todos with earlier dates
        return get(date, Status.Open);
    }

    public Todo[] getDoneUntil(LocalDate date) {//returns all Done Todos with earlier dates
        return get(date, Status.Done);
    }


    private int count(LocalDate date, Status stat) {//counts the number of Todo in the List
        int x = 0;
        if ((date == null) && (stat == null)) {//count all todos
            for (Node temp = head; temp != null; temp = temp.next) {
                x++;
            }
        } else if (date == null) {//counts Todo with certain Status
            for (Node temp = head; temp != null; temp = temp.next) {
                if (temp.todo.getStatus() == stat) {
                    x++;
                }
            }
        } else if (stat == null) {//counts Todo before certain date
            for (Node temp = head; temp != null; temp = temp.next) {
                if ((date.isAfter(temp.todo.getDate()) || date.isEqual(temp.todo.getDate()))) {
                    x++;
                }
            }
        } else {//counts Todo with certain Status before certain date
            for (Node temp = head; temp != null; temp = temp.next) {
                if ((date.isAfter(temp.todo.getDate()) || date.isEqual(temp.todo.getDate())) && (temp.todo.getStatus() == stat)) {
                    x++;
                }
            }
        }
        return x;
    }


    private Todo[] get(LocalDate date, Status status) {//returns an array of Todo

        Todo[] array = new Todo[count(date, status)];
        int i = 0;

        if ((date == null) && (status == null)) {//fills with all Todos
            for (Node temp = head; temp != null; temp = temp.next) {
                array[i] = temp.todo;
                i++;
            }
        } else if (date == null) {//fills with Todos with certain Status
            for (Node temp = head; temp != null; temp = temp.next) {
                if (temp.todo.getStatus() == status) {
                    array[i] = temp.todo;
                    i++;
                }
            }
        } else if (status == null) {//fills with Todos before certain date
            for (Node temp = head; temp != null; temp = temp.next) {
                if ((date.isAfter(temp.todo.getDate()) || date.isEqual(temp.todo.getDate()))) {
                    array[i] = temp.todo;
                    i++;
                }
            }
        } else {//fills with Todos with certain Status before certain date
            for (Node temp = head; temp != null; temp = temp.next) {
                if ((date.isAfter(temp.todo.getDate()) || date.isEqual(temp.todo.getDate())) && (temp.todo.getStatus() == status)) {
                    array[i] = temp.todo;
                    i++;
                }
            }
        }
        return array;
    }
} 
