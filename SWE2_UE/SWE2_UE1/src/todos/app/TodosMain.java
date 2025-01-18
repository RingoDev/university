package todos.app;

import java.time.LocalDate;
import java.util.Objects;

import todolist.TodoManager;
import todolist.Todo;

import inout.In;
import inout.Out;

public class TodosMain {

    public static void main(String[] args) {

        TodoManager TM = new TodoManager();

        In.open("todos.txt");
        if (!In.done()) {
            Out.println("Cannot open file todos.txt");
            return;
        }

        int year = In.readInt();
        while (In.done()) {
            int month = In.readInt();
            int day = In.readInt();
            String descr = In.readString();
            // TODO: Add todo with year, month, and day to manager
            TM.insertTodo(descr, year, month, day);
            year = In.readInt();

        }

        In.close();

        Out.println();
        Out.println("All Todos:");
        Out.println("===========");
        // TODO: get and print all todos
        print(TM.getAll());


        Out.println();
        Out.println("Until March 9:");
        Out.println("==============");
        // TODO: get and print todos until march 9
        print(TM.getUntil(LocalDate.of(2020, 3, 9)));

        // TODO: complete some todos
        int a = 2;
        Todo b = TM.searchByID(a);
        if (b != null) b.setDone();

        a = 4;
        b = TM.searchByID(a);
        if (b != null) b.setDone();

        a = 1;
        b = TM.searchByID(a);
        if (b != null) b.setDone();

        a = 6;
        b = TM.searchByID(a);
        if (b != null) b.setDone();


        Out.println();
        Out.println("Done:");
        Out.println("===========");
        // TODO get and print todos which are done
        print(TM.getDone());

        Out.println();
        Out.println("Still open:");
        Out.println("===========");
        // TODO get and print todos which are still open
        print(TM.getOpen());

        Out.println();
        Out.println("Still open until Until March 9:");
        Out.println("===============================");
        // TODO get and print todos which are still open until March 9
        print(TM.getOpenUntil(LocalDate.of(2020, 3, 9)));

        Out.println();
        Out.println("================================");
        print(TM.getAll());
        //delete all done Todos until March 9

        TM.deleteDoneUntil(LocalDate.of(2020, 03, 9));
        Out.println();
        Out.println("================================");
        print(TM.getAll());
    }

    public static void print(Todo[] array) {
        for (Todo todo: array) {
            Out.println(todo);
        }
    }
}
