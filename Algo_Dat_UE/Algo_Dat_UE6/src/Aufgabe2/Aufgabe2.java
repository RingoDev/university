package Aufgabe2;

class SingleLinkedList {

    Node head;
    int size;

    SingleLinkedList() {
        this.head = null;
        this.size = 0;
    }

    public void append(int i) {
        if (isEmpty()) {
            this.head = new Node(i);
            this.size++;
            return;
        }
        Node temp = new Node(0);
        temp = this.head;
        while (temp.next != null) {
            temp = temp.next;
        }
        temp.next = new Node(i);
        this.size++;
    }

    public void print() {
        StringBuilder sb = new StringBuilder("[");
        Node temp = head;
        while (temp != null) {
            sb.append(temp.value).append(", ");
            temp = temp.next;
        }
        if (size != 0) sb.deleteCharAt(sb.length() - 1).deleteCharAt(sb.length() - 1);
        sb.append("]");
        System.out.println(sb.toString());
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean isSorted() {
        Node temp = head;
        if (isEmpty()) return true;
        boolean asc = temp.value < temp.next.value;
        while (temp.next != null) {
            if ((asc && temp.value > temp.next.value)
                    || (!asc && temp.value < temp.next.value)) return false;
            temp = temp.next;
        }
        return true;
    }

    protected static class Node {
        int value;
        Node next;

        protected Node(int value) {
            this.value = value;
            this.next = null;
        }
    }
}

class ListCalculation {


    public static double calculateAverage(SingleLinkedList l)throws Exception {
        if (l.isEmpty()) throw new Exception("List is empty");
        int sum = 0;
        SingleLinkedList.Node temp = l.head;
        while (temp != null) {
            sum += temp.value;
            temp = temp.next;
        }
        return Math.round((sum / (double) l.size) * 100) / 100.0;
    }

    public static double calculateMedian(SingleLinkedList l) throws Exception {
        if (!l.isSorted()) throw new Exception("List is not sorted");
        if (l.isEmpty()) throw new Exception("List is empty");
        int i = 0;
        SingleLinkedList.Node temp = l.head;
        for (i = 1; i < l.size / 2; i++) {
            temp = temp.next;

        }
        if (l.size % 2 == 0) return (temp.value + temp.next.value) / 2.0;
        else return temp.next.value;
    }

    //Ihr Programmcode

}

class SingleLinkedListBuilder {

    public static SingleLinkedList fromArray(int[] values) {
        SingleLinkedList l = new SingleLinkedList();
        for (int val : values) {
            l.append(val);
        }
        return l;
    }

}

public class Aufgabe2 {

    public static void main(String[] args) {

        SingleLinkedList list1 = SingleLinkedListBuilder.fromArray(
                new int[]{1, 4, 6, 9});
        printify(list1);

        SingleLinkedList list2 = SingleLinkedListBuilder.fromArray(
                new int[]{10, 8, 4, -3, -5});
        printify(list2);

        SingleLinkedList list3 = SingleLinkedListBuilder.fromArray(
                new int[]{1, 4, 5, 6, 7, 8, 9, 2, 10});
        printify(list3);

        SingleLinkedList list4 = SingleLinkedListBuilder.fromArray(
                new int[]{3, 4, 5, 6, 7});
        printify(list4);

        SingleLinkedList list5 = SingleLinkedListBuilder.fromArray(
                new int[]{10, 8, 6, 5});
        printify(list5);
        SingleLinkedList list6 = SingleLinkedListBuilder.fromArray(
                new int[]{});
        printify(list6);

    }

    private static void printify(SingleLinkedList list) {
        list.print();

        try {
            System.out.println("Average: " + ListCalculation.calculateAverage(list));
        } catch (Exception e) {
            System.out.println(e);
        }

        try {
            System.out.println("Median: " + ListCalculation.calculateMedian(list));
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println();
    }
}
