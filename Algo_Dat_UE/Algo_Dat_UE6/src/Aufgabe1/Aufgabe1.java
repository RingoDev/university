package Aufgabe1;

class Suitcase {

    protected Item top;

    public Suitcase() {
        top = null;
    }

    public void pack(String val) {
        Item i = new Item(val);
        i.next = top;
        top = i;
    }

    public String unpack() throws Exception {
        if (isEmpty()) throw new Exception("Der Koffer ist leer!");
        Item temp = top;
        top = top.next;

        return temp.item;
    }

    public boolean isEmpty() {
        return top == null;
    }

    public void print() {
        if (isEmpty()) {
            System.out.println("Ich packe meinen Koffer und nehme nichts mit.");
            return;
        }
        boolean atLeast2 = false;
        StringBuilder sb = new StringBuilder("Ich packe meinen Koffer und nehme mit: ");
        Item temp = top;
        while (temp.next != null) {
            sb.append(temp.item).append(", ");
            temp = temp.next;
            atLeast2 = true;
        }
        if (atLeast2) sb.deleteCharAt(sb.length() - 2).append("und ").append(temp.item).append(".");
        else sb = new StringBuilder("Ich packe meinen Koffer und nehme ").append(temp.item).append(" mit.");
        System.out.println(sb.toString());
    }

    protected static class Item {
        final String item;
        Item next;

        Item(String i) {
            item = i;
            next = null;
        }
    }

}

public class Aufgabe1 {

    public static void main(String[] args) {
        Suitcase koffer = new Suitcase();
        System.out.println("\n========================\n");
        koffer.print();
        koffer.pack("Schuhe");
        System.out.println("\n========================\n");
        koffer.print();
        koffer.pack("Hemd");
        System.out.println("\n========================\n");
        koffer.print();
        koffer.pack("Haube");
        System.out.println("\n========================\n");
        koffer.print();
        koffer.pack("Jacke");
        System.out.println("\n========================\n");
        koffer.print();
        koffer.pack("T-Shirt");
        System.out.println("\n========================\n");
        koffer.print();
        koffer.pack("Socken");
        System.out.println("\n========================\n");
        koffer.print();
        koffer.pack("Laptop");


        System.out.println("\n========================\n");
        koffer.print();
        try {
            koffer.unpack();
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println("\n========================\n");
        koffer.print();
        try {
            koffer.unpack();
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println("\n========================\n");
        koffer.print();
        try {
            koffer.unpack();
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println("\n========================\n");
        koffer.print();
        try {
            koffer.unpack();
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println("\n========================\n");
        koffer.print();
        try {
            koffer.unpack();
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println("\n========================\n");
        koffer.print();
        try {
            koffer.unpack();
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println("\n========================\n");
        koffer.print();
        try {
            koffer.unpack();
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println("\n========================\n");
        koffer.print();
        try {
            koffer.unpack();
        } catch (Exception e) {
            System.out.println(e);
        }


        System.out.println("\n========================\n");
        koffer.print();


    }

}
