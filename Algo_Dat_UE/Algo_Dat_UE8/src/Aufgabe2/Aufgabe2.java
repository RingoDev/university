package Aufgabe2;

import java.io.*;


class Tree {


    Node root;


    Tree() {
        root = null;
    }

    void insert(int v) {
        Node n = new Node(v);
        if (root == null) {
            root = n;
        } else {
            Node p = root;
            Node q = null;

            while (p != null) {
                q = p;
                if (v < p.val)
                    p = p.left;
                else
                    p = p.right;
            }

            if (v < q.val)
                q.left = n;
            else
                q.right = n;
        }
    }

    // 1a numberOfNodes
    //------------------------------------------------------------------
    int numberOfNodes() {
        return numberOfNodes(root);
    }

    //#shortcode
    int numberOfNodes(Node p) {
        return p == null ? 0 : numberOfNodes(p.left) + numberOfNodes(p.right) + 1;
    }

    // 1b numberOfLeaves
    //------------------------------------------------------------------
    int numberOfLeaves() { return numberOfLeaves(root); }

    int numberOfLeaves(Node p) {
        if (p == null) return 0;
        return p.right == null && p.left == null ? 1 : numberOfLeaves(p.right) + numberOfLeaves(p.left);
    }

    // 1c numberOfInnerNodes
    //------------------------------------------------------------------
    int numberOfInnerNodes() {
        int i = numberOfInnerNodes(root);
        return i == 0 ? 0 : i - 1;
    }

    int numberOfInnerNodes(Node p) {
        if (p == null) return 0;
        return p.right == null && p.left == null ? 0 : numberOfInnerNodes(p.right) + numberOfInnerNodes(p.left) + 1;
    }

    static class Node {
        int val;
        Node left;
        Node right;

        Node(int val) {
            this.val = val;
        }
    }


}


class Aufgabe2 {


    public static void main(String[] args) throws FileNotFoundException {
        System.setOut(new PrintStream(new FileOutputStream("Aufgabe2.txt")));
        System.out.println("Aufgabe 2 - Knoten zählen");
        System.out.println();

        Tree t = new Tree();

        System.out.println("Leerer Baum");
        System.out.println("------------------------");
        System.out.println("a) numberOfNodes: " + t.numberOfNodes());
        System.out.println("b) numberOfLeaves: " + t.numberOfLeaves());
        System.out.println("c) numberOfInnerNodes: " + t.numberOfInnerNodes());
        System.out.println();

        t.insert(8);

        System.out.println("Ein Knoten");
        System.out.println("------------------------");
        System.out.println("a) numberOfNodes: " + t.numberOfNodes());
        System.out.println("b) numberOfLeaves: " + t.numberOfLeaves());
        System.out.println("c) numberOfInnerNodes: " + t.numberOfInnerNodes());
        System.out.println();

        t.insert(4);

        System.out.println("Zwei Knoten");
        System.out.println("------------------------");
        System.out.println("a) numberOfNodes: " + t.numberOfNodes());
        System.out.println("b) numberOfLeaves: " + t.numberOfLeaves());
        System.out.println("c) numberOfInnerNodes: " + t.numberOfInnerNodes());
        System.out.println();

        t.insert(12);
        t.insert(2);
        t.insert(6);
        t.insert(10);
        t.insert(14);
        t.insert(1);
        t.insert(3);
        t.insert(5);
        t.insert(7);
        t.insert(9);
        t.insert(11);
        t.insert(13);
        t.insert(15);

        System.out.println("Baum aus Aufgabe 1");
        System.out.println("------------------------");
        System.out.println("a) numberOfNodes: " + t.numberOfNodes());
        System.out.println("b) numberOfLeaves: " + t.numberOfLeaves());
        System.out.println("c) numberOfInnerNodes: " + t.numberOfInnerNodes());
        System.out.println();


    }


}
