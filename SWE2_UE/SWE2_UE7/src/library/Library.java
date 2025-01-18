package library;

import java.util.*;


public class Library {

    Map<String, List<Book>> bookMap;
    Map<String, Lender> lenderMap;

    public Library() {
        bookMap = new HashMap<>();
        lenderMap = new HashMap<>();
    }

    public void addBook(String bookName) {
        Book book = new Book(bookName);
        if (bookMap.containsKey(bookName)) {
            bookMap.get(bookName).add(book);
        } else {
            List<Book> l = new ArrayList<>();
            l.add(book);
            bookMap.put(bookName, l);
        }
    }

    public void addLender(String lenderName) throws IllegalStateException {
        if (lenderMap.containsKey(lenderName)) throw new IllegalStateException("Lender name already taken: " + lenderName);
        lenderMap.put(lenderName, new Lender(lenderName));
    }

    public void lendBook(String bookName, String lenderName) throws NoSuchElementException,IllegalStateException {
        if (!lenderMap.containsKey(lenderName)) throw new NoSuchElementException("Unknown lender: "+lenderName);
        if (!bookMap.containsKey(bookName)) throw new NoSuchElementException(bookName+" doesnt exist in the library");

        //throwing Exception here already so I dont have to remove the Book from bookMap getting the Error in Lender and having to add it again to bookMap
        if (lenderMap.get(lenderName).hasBook(bookName))throw new IllegalStateException("Lender "+ lenderName+" already lends "+bookName);
        if (bookMap.get(bookName).size() == 0) throw new NoSuchElementException("Book not available: "+bookName);

        lenderMap.get(lenderName).add(bookMap.get(bookName).remove(0));

    }

    public void returnBook(String bookName, String lenderName) throws NoSuchElementException {
        if (!lenderMap.containsKey(lenderName)) throw new NoSuchElementException("Unknown lender: "+lenderName+" tries to return: "+bookName);
        if (!bookMap.containsKey(bookName)) throw new NoSuchElementException(bookName+" doesnt exist in the library, return tried by "+lenderName);
        if (!lenderMap.get(lenderName).hasBook(bookName)) throw new NoSuchElementException("Not our book: "+bookName+", return tried by "+lenderName);
        Book b = lenderMap.get(lenderName).remove(bookName);
        b.incLentCount();
        bookMap.get(bookName).add(b);
    }

    public List<Book> getAvailableBooksOrderedByAlphabet() {
        List<Book> l = new ArrayList<>();
        bookMap.forEach((k, v) -> l.addAll(v));
        l.sort(Book.getNameComparator());
        return l;
    }
    public SortedSet<Book> getAvailableBooksOrderedByLentCount(){
        SortedSet<Book> ss= new TreeSet<>(Book.getLentComparator());
        bookMap.forEach((k,v) -> ss.addAll(v));
        return ss;
    }

    public SortedSet<Lender> getLendersOrderedByName(){
        SortedSet<Lender> ss = new TreeSet<>();
        lenderMap.forEach((n,l) -> ss.add(l));
        return ss;
    }
}
