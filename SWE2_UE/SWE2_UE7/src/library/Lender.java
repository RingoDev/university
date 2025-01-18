package library;

import java.util.*;

public class Lender implements Comparable<Lender>{

    String name;
    SortedSet<Book> ss;

    public Lender(String name){
        this.name = name;
        ss = new TreeSet<>(Book.getNameComparator());
    }

    protected void add(Book book) throws IllegalStateException{
        //cant reach this place because already caught in library
        if (ss.contains(book)) throw new IllegalStateException(this.toString() +" already lends " + book.getName());
        ss.add(book);
    }

    protected Book remove(String bookName){
        for (Book book : this.ss){
            if (book.getName().equals(bookName)){
                ss.remove(book);
                return book;
            }
            if(book.getName().compareTo(bookName) > 0)break;
        }
        return null;
    }

    protected boolean hasBook(String bookname){
        for(Book b :ss){
            if (b.getName().equals(bookname)) return true;
        }
        return false;
    }

    @Override
    public int compareTo(Lender l) {
        return this.name.compareTo(l.name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lender lender = (Lender) o;
        return this.name.equals(lender.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name);
    }
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder("Lender: ");
        sb.append(this.name).append(", books: [");
        boolean notEmpty = false;
        for(Book book : this.ss ){
            notEmpty = true;
            sb.append(book.getName()).append(", ");
        }
        if(notEmpty)sb.deleteCharAt(sb.length()-1).deleteCharAt(sb.length()-1);
        sb.append("]");
        return sb.toString();
    }
}
