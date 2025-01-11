package Aufgabe3;

class SingleLinkedList {

    Node head;
    int size;

    SingleLinkedList() {
        this.head = null;
        this.size = 0;
    }

    public void append(String str) {
        if (isEmpty()) {
            this.head = new Node(str);
            this.size++;
            return;
        }
        Node temp = new Node("");
        temp = this.head;
        while (temp.next != null) {
            temp = temp.next;
        }
        temp.next = new Node(str);
        this.size++;
    }

    public void print() {
        StringBuilder sb = new StringBuilder("[");
        Node temp = head;
        while (temp != null) {
            sb.append(temp.str).append(", ");
            temp = temp.next;
        }
        if (size != 0) sb.deleteCharAt(sb.length() - 1).deleteCharAt(sb.length() - 1);
        sb.append("]");
        System.out.println(sb.toString());
    }

    public boolean isEmpty() {
        return size == 0;
    }


    public void deleteAt(int index)throws Exception {
        if(index < 0 || index >= size) throw new Exception("Invalid index");
        size--;
        if(index == 0){
            head = head.next;
            return;
        }

        Node temp = head;
        for (; index > 1 ; index--){
            temp = temp.next;
        }
        temp.next = temp.next.next;


    }

    //Ihr Programmcode
    protected static class Node {
        String str;
        Node next;

        protected Node(String str) {
            this.str = str;
            this.next = null;
        }
    }

}

class SingleLinkedListBuilder {

    public static SingleLinkedList fromArray(String[] arr) {
        SingleLinkedList l = new SingleLinkedList();
        for (String str : arr) {
            l.append(str);
        }
        return l;
    }
}

public class Aufgabe3 {

    public static void main(String[] args) throws Exception {

        SingleLinkedList list = SingleLinkedListBuilder.fromArray(
                new String[] { "Das", "Wetter", "ist", "nicht", "schön" });

        list.print();

        for(int i = 0; i<4; i++) {
            System.out.println("\nDeleting on position: "+i+"\n");
            try { list.deleteAt(i);}
            catch (Exception e) { System.out.println(e);}
            list.print();
        }
        System.out.println();

        SingleLinkedList list2 = SingleLinkedListBuilder.fromArray(
                new String[] { "Das", "Wetter", "ist", "nicht", "schön" });

        list2.print();

        for(int i = 0; i<4; i++) {
            System.out.println("\nDeleting on position: "+2+"\n");
            try { list2.deleteAt(2);}
            catch (Exception e) { System.out.println(e);}
            list2.print();
        }
        System.out.println();

        SingleLinkedList list3 = SingleLinkedListBuilder.fromArray(
                new String[] { "Das", "Wetter", "ist", "nicht", "schön" });

        list3.print();

        for(int i = 0; i<6; i++) {
            System.out.println("\nDeleting on position: "+0+"\n");
            try { list3.deleteAt(0);}
            catch (Exception e) { System.out.println(e);}
            list3.print();
        }
    }
}
