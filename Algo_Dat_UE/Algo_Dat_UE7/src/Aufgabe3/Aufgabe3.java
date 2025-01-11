package Aufgabe3;

import java.io.*;


public class Aufgabe3 {

    public static void main(String[] args) throws FileNotFoundException {

        System.setOut(new PrintStream(new FileOutputStream("Aufgabe3.txt")));

        new Ringlist(1).josephus(1);
        new Ringlist(1).josephus(5);
        new Ringlist(5).josephus(1);
        new Ringlist(7).josephus(11);
        new Ringlist(19).josephus(2);
        new Ringlist(41).josephus(3);

        System.out.println();

        new Ringlist(1).josephusThereAndBack(1);
        new Ringlist(1).josephusThereAndBack(5);
        new Ringlist(5).josephusThereAndBack(1);
        new Ringlist(7).josephusThereAndBack(11);
        new Ringlist(19).josephusThereAndBack(2);
        new Ringlist(41).josephusThereAndBack(3);
    }
}

class Ringlist {

    Node head;
    Node tail;
    int size;

    public Ringlist(int n) {
        size = 0;
        for (int i = 0; i < n; i++) {
            append(i + 1);
        }
    }

    public void josephus(int k) {
        System.out.print("n=" + size + "  josephus(" + k + "): ");
        Node temp = head;
        int i = 1;
        while (temp != null) {
            if (i == k) {
                Node prev = temp.prev;
                Node next = temp.next;
                prev.next = next;
                next.prev = prev;

                System.out.print(temp.value + " ");
                size--;
                i = 1;
            } else i++;
            temp = temp.next;
            if (isEmpty()) {
                System.out.println();
                return;
            }
        }

    }

    public void josephusThereAndBack(int k) {

        System.out.print("n=" + size + "  josephusThereAndBack(" + k + "): ");
        boolean forward = true;
        Node temp = head;
        for (int i =1;temp != null;i++) {
            if (i == k) {
                Node prev = temp.prev;
                Node next = temp.next;
                prev.next = next;
                next.prev = prev;

                System.out.print(temp.value + " ");
                size--;
                i = 0;
                forward = !forward;
            }

            if (forward) temp = temp.next;
            else temp = temp.prev;

            if (isEmpty()) {
                System.out.println();
                return;
            }
        }
    }

    void append(int v) {
        Node insert = new Node(v);
        if (isEmpty()) {
            tail = insert;
            head = insert;
            tail.next = head;
            tail.prev = head;
            // points to itself
        } else {
            tail.next = insert;
            insert.prev = tail;
            insert.next = head;
            head.prev = insert;
            tail = insert;
        }
        size++;
    }

    boolean isEmpty() {
        return size == 0;
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
