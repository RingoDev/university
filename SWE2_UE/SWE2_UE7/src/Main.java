import library.*;
public class Main {

    public static void main(String[] args) {

        Library Wissensturm = new Library();

        Wissensturm.addLender("Robert C. Martin");
        Wissensturm.addLender("Strolchi");
        Wissensturm.addLender("Susi");
        Wissensturm.addLender("Alfred");

        try {
            Wissensturm.addLender("Robert C. Martin");
            System.out.println("Error: Robert C. Martin should not be able to be registered twice!");
        } catch (Exception e) {
            System.out.println("OK, expected exception: " + e.getMessage());
        }

        Wissensturm.addBook("Clean Code");
        Wissensturm.addBook("Clean Code");
        Wissensturm.addBook("Clean Agile");
        Wissensturm.addBook("Bible");
        Wissensturm.addBook("A Good Book");
        Wissensturm.addBook("A Good Book");
        Wissensturm.addBook("Bible");
        Wissensturm.addBook("Bible");

        Wissensturm.lendBook("Clean Code", "Susi");

        try {
            Wissensturm.lendBook("Clean Code", "Susi");
            System.out.println("Error: Susi is already lending Clean Code");
        } catch (Exception e) {
            System.out.println("OK, expected exception: " + e.getMessage());
        }
        Wissensturm.lendBook("Clean Agile", "Susi");
        Wissensturm.lendBook("Clean Code", "Robert C. Martin");

        try {
            Wissensturm.returnBook("Clean Code", "Strolchi");
            System.out.println("Error: Strolchi never lent Clean Code");
        } catch (Exception e) {
            System.out.println("OK, expected exception: " + e.getMessage());
        }

        try {
            Wissensturm.lendBook("Clean Code", "Strolchi");
            System.out.println("Error: The Book Clean Code isn't available in the library right now");
        } catch (Exception e) {
            System.out.println("OK, expected exception: " + e.getMessage());
        }

        try {
            Wissensturm.lendBook("Clean Agile", "Strolchi");
            System.out.println("Error: The Book Clean Agile isn't available in the library right now");
        } catch (Exception e) {
            System.out.println("OK, expected exception: " + e.getMessage());
        }
        Wissensturm.returnBook("Clean Code", "Robert C. Martin");

        try {
            Wissensturm.lendBook("Bible", "Max");
            System.out.println("Error: Max is not registered in the library");
        } catch (Exception e) {
            System.out.println("OK, expected exception: " + e.getMessage());
        }

        try {
            Wissensturm.returnBook("Bible", "Max");
            System.out.println("Error: Max is not registered in the library");
        } catch (Exception e) {
            System.out.println("OK, expected exception: " + e.getMessage());
        }

        try {
            Wissensturm.lendBook("Business for Dummies", "Susi");
            System.out.println("Error: The Book Business for Dummies doesnt exist in the library");
        } catch (Exception e) {
            System.out.println("OK, expected exception: " + e.getMessage());
        }
        Wissensturm.lendBook("Clean Code", "Alfred");
        Wissensturm.returnBook("Clean Code", "Susi");
        Wissensturm.lendBook("Clean Code", "Susi");
        Wissensturm.returnBook("Clean Code", "Susi");
        Wissensturm.lendBook("Clean Code", "Susi");
        Wissensturm.returnBook("Clean Code", "Susi");
        Wissensturm.lendBook("Clean Code", "Susi");
        Wissensturm.returnBook("Clean Code", "Susi");
        Wissensturm.lendBook("Clean Code", "Susi");
        Wissensturm.returnBook("Clean Code", "Susi");
        Wissensturm.returnBook("Clean Code", "Alfred");
        Wissensturm.lendBook("A Good Book", "Alfred");
        Wissensturm.lendBook("A Good Book", "Susi");
        Wissensturm.returnBook("A Good Book", "Susi");
        Wissensturm.lendBook("A Good Book", "Susi");
        Wissensturm.returnBook("A Good Book", "Susi");
        Wissensturm.lendBook("Bible", "Alfred");


        System.out.println();
        System.out.println("Books:");
        Wissensturm.getAvailableBooksOrderedByAlphabet().forEach(System.out::println);

        System.out.println();
        System.out.println("Most lent books:");
        Wissensturm.getAvailableBooksOrderedByLentCount().forEach(System.out::println);

        System.out.println();
        System.out.println("Lenders:");
        Wissensturm.getLendersOrderedByName().forEach(System.out::println);

    }
}
