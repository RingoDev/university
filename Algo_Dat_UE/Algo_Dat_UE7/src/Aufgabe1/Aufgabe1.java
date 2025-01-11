package Aufgabe1;

import java.io.*;

public class Aufgabe1 {

    static void printArray(int[] values) {
        StringBuilder sb = new StringBuilder("[");
        for (int v : values) {
            sb.append(v).append(", ");
        }
        if (values.length != 0) sb.deleteCharAt(sb.length() - 1).deleteCharAt(sb.length() - 1);
        sb.append("]");
        System.out.println(sb);
    }

    public static void main(String[] args) throws FileNotFoundException {

        System.setOut(new PrintStream(new FileOutputStream("Aufgabe1.txt")));

        for (int length : new int[]{0, 1, 2, 9}) {

            int[] array = new int[length];
            for (int i = 0; i < array.length; i++) array[i] = i + 1;

            System.out.println("Listenlänge: " + array.length);
            System.out.println("--------------");

            System.out.print("SingleLinkedList().toDoubleLinkedList(): ");
            new SingleLinkedList(array).toDoubleLinkedList().print();
            System.out.print("SingleLinkedList().invert(): ");
            new SingleLinkedList(array).invert().print();
            System.out.print("SingleLinkedList().toArray(): ");
            printArray(new SingleLinkedList(array).toArray());
            System.out.println();

            System.out.print("DoubleLinkedList().toSingleLinkedList(): ");
            new DoubleLinkedList(array).toSingleLinkedList().print();
            System.out.print("DoubleLinkedList().invert(): ");
            new DoubleLinkedList(array).invert().print();
            System.out.print("DoubleLinkedList().toArray(): ");
            printArray(new DoubleLinkedList(array).toArray());
            System.out.println();
        }
    }
}

class SingleLinkedList {

    Node head;
    int size;


    public SingleLinkedList() {
        size = 0;
    }

    public SingleLinkedList(int[] values) {
        size = 0;
        for (int v : values) {
            append(v);
        }
    }

    boolean isEmpty() {
        return head == null;
    }

    void append(int x) {
        Node insert = new Node(x);
        if (isEmpty()) head = insert;
        else {
            Node temp = head;
            while (temp.next != null) {
                temp = temp.next;
            }
            temp.next = insert;
        }
        size++;
    }

    public DoubleLinkedList toDoubleLinkedList() {
        return new DoubleLinkedList(this.toArray());
    }

    public SingleLinkedList invert() {
        if (isEmpty()) return this;
        Node prev = head;
        Node curr = head.next;
        head.next = null;
        while (curr != null) {
            Node temp = curr.next;
            curr.next = prev;

            prev = curr;
            curr = temp;
        }
        head = prev;
        return this;
    }

    public int[] toArray() {
        int[] arr = new int[size];
        Node temp = head;
        for (int i = 0; i < size; i++) {
            arr[i] = temp.value;
            temp = temp.next;
        }
        return arr;
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

        Node(int v) {
            this.next = null;
            this.value = v;
        }
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

    public SingleLinkedList toSingleLinkedList() {
        SingleLinkedList sl = new SingleLinkedList();
        Node temp = head;
        while (temp != null) {
            sl.append(temp.value);
            temp = temp.next;
        }
        return sl;
    }

    public DoubleLinkedList invert() {
        if (isEmpty()) return this;
        Node prev = head;
        Node curr = head.next;
        head.next = null;
        while (curr != null) {
            Node temp = curr.next;
            curr.next = prev;
            prev.prev = curr;

            prev = curr;
            curr = temp;
        }
        tail = head;
        head = prev;
        return this;
    }

    public int[] toArray() {
        return toSingleLinkedList().toArray();
    }

    public void print() {
        toSingleLinkedList().print();
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
