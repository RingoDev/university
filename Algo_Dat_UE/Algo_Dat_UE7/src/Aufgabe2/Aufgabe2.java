package Aufgabe2;

import java.io.*;

public class Aufgabe2 { /*
========================*/


    static void printContains(DoubleLinkedList list, int val) { /*
	------------------------------------------------------------*/
        System.out.println(val + " ist" + (list.contains(val) ? "" : " nicht") + " enthalten");
    }

    public static void main(String[] args) throws FileNotFoundException { /*
	----------------------------------------------------------------------*/

        System.setOut(new PrintStream(new FileOutputStream("Aufgabe2.txt")));

        DoubleLinkedList list;

        list = new DoubleLinkedList(new int[]{});
        list.print();
        printContains(list, 0);
        System.out.println();

        list = new DoubleLinkedList(new int[]{1});
        list.print();
        printContains(list, 0);
        printContains(list, 1);
        printContains(list, 2);
        System.out.println();

        list = new DoubleLinkedList(new int[]{1, 2});
        list.print();
        printContains(list, 0);
        printContains(list, 1);
        printContains(list, 2);
        printContains(list, 3);
        System.out.println();

        list = new DoubleLinkedList(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9});
        list.print();
        for (int i = -1; i <= 11; i++) printContains(list, i);
    }
}

class DoubleLinkedList {


    Node head;
    Node tail;
    int size;

    public DoubleLinkedList(int[] values) {
        size = 0;
        for (int v : values) {
            append(v);
        }

    }

    public boolean contains(int x) {
        if (isEmpty()) return false;
        int lowerIndex = 0;
        int upperIndex = size - 1;
        while (upperIndex >= lowerIndex) {
            Node temp = head;
            int m = (lowerIndex + upperIndex) / 2;
            for (int i = 0; i < m; i++) {
                temp = temp.next;
            }
            if (temp.value == x) return true;

            if (x < temp.value) upperIndex = m - 1;
            else lowerIndex = m + 1;
        }
        return false;
    }


    void append(int v) {
        Node insert = new Node(v);
        if (isEmpty()) {
            tail = insert;
            head = insert;
        } else {
            tail.next = insert;
            insert.prev = tail;
            tail = insert;
        }
        size++;
    }

    boolean isEmpty() {
        return head == null;
    }

    public void print() {
        StringBuilder sb = new StringBuilder();
        if (isEmpty()) sb.append("<>");
        else {
            Node temp = head;
            sb.append("<");
            while (temp != null) {
                sb.append(temp.value).append(", ");
                temp = temp.next;
            }
            sb.deleteCharAt(sb.length() - 1).deleteCharAt(sb.length() - 1);
            sb.append(">");
        }
        System.out.println(sb);
    }

    static class Node {
        int value;
        Node next;
        Node prev;

        Node(int v) {
            this.next = null;
            this.prev = null;
            this.value = v;
        }
    }
}
