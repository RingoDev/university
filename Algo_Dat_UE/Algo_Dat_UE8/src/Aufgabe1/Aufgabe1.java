package Aufgabe1;

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

    void printTree() {
        printTree(root,0);
		System.out.println("\n===========================================================\n");
    }

    void printTree(Node p, int level) {

		if(p.right != null)printTree(p.right, level + 1);
		if (level == 0) System.out.format("[%2d]\n", p.val);
		else System.out.format("%" + (8 * level) + "s[%2d]\n", " ", p.val);

		if(p.left != null)printTree(p.left, level + 1);
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


class Aufgabe1 {


    public static void main(String[] args) throws FileNotFoundException {
        System.setOut(new PrintStream(new FileOutputStream("Aufgabe1.txt")));
        System.out.println("Aufgabe 1 - Ausgabe");
        System.out.println();

        Tree t = new Tree();


        t.insert(8);
        t.printTree();


        t.insert(4);
        t.printTree();


        t.insert(12);
        t.printTree();

        t.insert(2);
        t.printTree();

        t.insert(6);
        t.printTree();

        t.insert(10);
        t.printTree();

        t.insert(14);
        t.printTree();

        t.insert(1);
        t.printTree();


        t.insert(3);
        t.printTree();


        t.insert(5);
        t.printTree();


        t.insert(7);
        t.printTree();


        t.insert(9);
        t.printTree();


        t.insert(11);
        t.printTree();


        t.insert(13);
        t.printTree();


        t.insert(15);
        t.printTree();


    }


}
