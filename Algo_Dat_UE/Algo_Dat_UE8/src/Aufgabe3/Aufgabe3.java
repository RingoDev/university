package Aufgabe3;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;


class Tree {


    Node root;
    Node lastLeftTurn;

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

    /**
     * Finds the Node with the next higher value and returns the value.
     * If the value doesn't exist in the tree or there is no next higher value this method returns -1.
     *
     * @param v the value that is used as a base to search for the next higher value
     * @return the next higher value,
     * or -1 if the base value doesn't exist in the tree,
     * or -1 if the base value is the highest value in the tree already
     */
    int nextHigherValue(int v) {
        lastLeftTurn = null;

        //finding the Node in the tree
        Node node = findNode(root, v);
        if (node == null) return -1;

        // if a right branch exists follow it and then get the leftest Leaf
        if (node.right != null) return getLeftest(node.right).val;

        // no right branch exists, our Solution is the last left Turn;
        if (lastLeftTurn != null) return lastLeftTurn.val;

            // there is no next higher value for the highest value... abort mission
        else return -1;

    }

    /**
     * finds a Node with the specified Value and returns it. Saves the Last Left Turn in lastLeftTurn.
     * If no Node with the value exists return null;
     *
     * @param node the starting Node
     * @param v    the searched value
     * @return the Node with the specified Value or null if no Node is found
     */
    Node findNode(Node node, int v) {
        if (node == null) return null;
        if (node.val < v) return findNode(node.right, v);
        if (node.val > v) {
            lastLeftTurn = node;
            return findNode(node.left, v);
        }
        return node;
    }

    /**
     * follows the left branch as long as there is one and returns the last Node
     *
     * @param node the starting Node
     * @return the Leftest Node
     */
    Node getLeftest(Node node) {
        return node.left == null ? node : getLeftest(node.left);
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


class Aufgabe3 {


    public static void main(String[] args) throws FileNotFoundException {
        System.setOut(new PrintStream(new FileOutputStream("Aufgabe3.txt")));
        System.out.println("Aufgabe 3 - Nächstgrößerer Wert");
        System.out.println();

        Tree t = new Tree();

        t.insert(10);
        t.insert(4);
        t.insert(25);
        t.insert(2);
        t.insert(8);
        t.insert(15);
        t.insert(30);
        t.insert(7);
        t.insert(9);
        t.insert(12);
        t.insert(17);
        t.insert(11);
        t.insert(19);

        System.out.println("nextHigherValue von 2: " + t.nextHigherValue(2));
        System.out.println("nextHigherValue von 4: " + t.nextHigherValue(4));
        System.out.println("nextHigherValue von 10: " + t.nextHigherValue(10));
        System.out.println("nextHigherValue von 12: " + t.nextHigherValue(12));
        System.out.println("nextHigherValue von 19: " + t.nextHigherValue(19));
        System.out.println("nextHigherValue von 28: " + t.nextHigherValue(28));
        System.out.println("nextHigherValue von 30: " + t.nextHigherValue(30));
    }
}
