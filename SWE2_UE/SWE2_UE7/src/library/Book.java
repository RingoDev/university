package library;

import java.util.Comparator;

public class Book {

    private final String name;
    private int lent;

    public Book(String name){
        this.name = name;
    }

    int getLent(){
        return this.lent;
    }

    String getName(){
        return this.name;
    }

    protected void incLentCount(){
        this.lent++;
    }

    public String toString(){
        return this.name + " lent count: "+lent;
    }

    public static Comparator<Book> getLentComparator(){
        return (b1, b2) -> {
            int compare = b2.lent - b1.lent;
            if (compare == 0){
                compare = b1.name.compareTo(b2.name);
            }
            return compare;
        };
    }
    public static Comparator<Book> getNameComparator(){
        return Comparator.comparing(b -> b.name);
    }
}
