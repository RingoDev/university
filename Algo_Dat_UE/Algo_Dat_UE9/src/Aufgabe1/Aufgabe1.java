package Aufgabe1;

import java.io.*;


class Tree {


    static class Node {
        int val;
        Node left;
        Node right;

        Node(int val) {
            this.val = val;
        }
    }


    Node root;


    Tree() {
        root = null;
    }


    Node insert(int v) {
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
        return n;
    }


    void printTree() {
        printTree(root, 0);
    }


    void printTree(Node p, int level) {
        if (p != null) {
            printTree(p.right, level + 1);

            for (int i = 0; i < level; i++)
                System.out.print("     ");
            System.out.format("[%2d]", p.val);
            System.out.println();

            printTree(p.left, level + 1);
        }
    }


    // 1a height
    //------------------------------------------------------------------
    int height() {
        return height(root);
    }


    int height(Node p) {
        if (p == null) return 0;
        int left = height(p.left);
        int right = height(p.right);
        return left > right ? left + 1 : right + 1;
    }


    // 1b isPerfect
    //------------------------------------------------------------------
    boolean isPerfect() {
        return isPerfect(root);
    }


    boolean isPerfect(Node p) {
        if (p == null) return true;
        //if a subtree is not perfect, return false
        if (!isPerfect(p.left) || !isPerfect(p.right))return false;
        // if the height of the left and right branch differ, return false
        return height(p.left) == height(p.right);

    }


    // 1c isAVLTree
    //------------------------------------------------------------------
    boolean isAVLTree() {
        return isAVLTree(root);
    }


    boolean isAVLTree(Node p) {

        if (p == null) return true;

        if(!isAVLTree(p.left) || !isAVLTree(p.right))return false;

        return Math.abs(height(p.right) - height(p.left)) <= 1;
    }


    // 1d rrRotate
    //------------------------------------------------------------------
    Node rrRotate(Node x) {
        Node temp = x.right;
        x.right = temp.left;
        temp.left= x;

        Node father = findFather(root,x);
        //there is a father we have to rearrange
        if (father != null){
            if (father.right == x)father.right = temp;
            else father.left = temp;
        }else{//x was root so now temp will be root.
            root = temp;
        }
        return temp;
    }

    Node findFather(Node tempRoot,Node x){
        if (x == root) return null;
        if(tempRoot.left == x || tempRoot.right == x)return tempRoot;
        if (findFather(tempRoot.left,x) == null)return findFather(tempRoot.right,x);
        else return null;
    }
}


class Aufgabe1 {


    static void printInfo(Tree t) {
        System.out.println("------------------------------------");
        t.printTree();
        System.out.println();
        System.out.println("Height: " + t.height());
        System.out.println(t.isPerfect() ? "perfect" : "not perfect");
        System.out.println(t.isAVLTree() ? "AVL" : "no AVL");
    }


    public static void main(String[] args) throws FileNotFoundException {
        System.setOut(new PrintStream(new FileOutputStream("Aufgabe1.txt")));
        System.out.println("Aufgabe 1 - Ausgeglichene Bäume");
        System.out.println();

        Tree t = new Tree();

        t.insert(1);
        t.insert(4);
        t.insert(6);
        printInfo(t);
        t.rrRotate(t.root);
        printInfo(t);

        t.insert(7);
        t.insert(9);
        printInfo(t);
        t.rrRotate(t.root.right);
        printInfo(t);

        t.insert(2);
        t.insert(3);
        printInfo(t);
        t.rrRotate(t.root.left);
        printInfo(t);

        t.insert(10);
        t.insert(5);
        t.insert(8);
        t.insert(11);
        printInfo(t);
        t.rrRotate(t.root);
        printInfo(t);


    }


}
