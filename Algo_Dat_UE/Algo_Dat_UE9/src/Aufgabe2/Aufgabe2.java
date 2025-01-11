package Aufgabe2;

import java.io.*;
import java.util.Random;


class Aufgabe2 {


    //------------------------------------------------------------------
    static void printResult(long comparisons, long assignments, long time) {

        formatNumber(comparisons);
        formatNumber(assignments);

        if (time < 1000)
            System.out.format("%,8d ns|", time);
        else {
            time = time / 1000;
            if (time < 1000)
                System.out.format("%,8d µs|", time);
            else {
                time = time / 1000;
                if (time < 1000)
                    System.out.format("%,8d ms|", time);
                else {
                    time = time / 1000;
                    if (time < 60)
                        System.out.format("%,9d s|", time);
                    else {
                        time = time / 60;
                        if (time < 60)
                            System.out.format("%,7d min|", time);
                        else {
                            time = time / 60;
                            System.out.format("%,9d h|", time);
                        }
                    }
                }
            }
        }
    }


    //------------------------------------------------------------------
    static void formatNumber(long n) {
        if (n < 1000000)
            System.out.format("%,11d|", n);
        else {
            double d = n / 1000000.0;
            if (d < 1000)
                System.out.format("%,7.1f Mio|", d);
            else {
                d = d / 1000.0;
                if (d < 1000)
                    System.out.format("%,7.1f Mrd|", d);
                else {
                    d = d / 1000.0;
                    if (d < 1000)
                        System.out.format("%,7.1f Bio|", d);
                    else {
                        d = d / 1000.0;
                        System.out.format("%,7.1f Brd|", d);
                    }
                }
            }
        }
    }


    //------------------------------------------------------------------
    static void selectionSort(int[] list) {
        long comp = 0;
        long assign = 0;
        long time = System.nanoTime();
        for (int i = 0; i < list.length - 1; i++) {
            int min = i;
            for (int j = i + 1; j < list.length; j++) {
                comp++;
                if (list[j] < list[min])
                    min = j;
            }
            assign += 3;
            int h = list[i];
            list[i] = list[min];
            list[min] = h;
        }
        printResult(comp/* Vergleiche */, assign/*Zuweisungen*/, System.nanoTime() - time/*Zeit*/);
    }


    //------------------------------------------------------------------
    static void insertionSort(int[] list) {
        long comp = 0;
        long assign = 0;
        long time = System.nanoTime();
        for (int i = 0; i < list.length - 1; i++) {
            assign++;
            int h = list[i + 1];
            int j = i;
            while ((j >= 0) && (list[j] > h)) {
                comp += 2;
                assign++;
                list[j + 1] = list[j];
                j = j - 1;
            }
            assign++;
            list[j + 1] = h;
        }
        printResult(comp/* Vergleiche */, assign/*Zuweisungen*/, System.nanoTime() - time/*Zeit*/);
    }


    //------------------------------------------------------------------
    static void shellSort(int[] list) {
        long comp = 0;
        long assign = 0;
        long time = System.nanoTime();
        int m = list.length / 2;
        while (m > 0) {
            for (int i = 0; i < list.length - m; i++) {
                assign++;
                int h = list[i + m];
                int j = i;
                while ((j >= 0) && (list[j] > h)) {
                    comp += 2;
                    assign++;
                    list[j + m] = list[j];
                    j = j - m;
                }
                assign++;
                list[j + m] = h;
            }
            m = m / 2;
        }
        printResult(comp/* Vergleiche */, assign/*Zuweisungen*/, System.nanoTime() - time/*Zeit*/);
    }


    //------------------------------------------------------------------
    static void quickSort(int[] list) {
        long time = System.nanoTime();
        quickSort(list, 0, list.length - 1);
        printResult(quickComp/* Vergleiche */, quickAssign/*Zuweisungen*/, System.nanoTime() - time/*Zeit*/);
    }
    static long quickComp = 0;
    static long quickAssign = 0;

    static void quickSort(int[] list, int l, int u) {
        if (l < u) {
            int x = list[(l + u) / 2];
            int i = l;
            int j = u;
            while (i <= j) {
                quickComp++;
                while (list[i] < x) {
                    quickComp++;
                    i++;
                }
                while (list[j] > x) {
                    quickComp++;
                    j--;
                }
                if (i <= j) {
                    quickAssign +=3;
                    int h = list[i];
                    list[i] = list[j];
                    list[j] = h;
                    i++;
                    j--;
                }
            }
            quickSort(list, l, j);
            quickSort(list, i, u);
        }
    }


    //------------------------------------------------------------------
    static int[] initRandom(int n) {
        Random r = new Random();
        int[] list = new int[n];
        for (int i = 0; i < n; i++)
            list[i] = r.nextInt(n + 1);
        return list;
    }


    //------------------------------------------------------------------
    public static void main(String[] args) throws FileNotFoundException {
        System.setOut(new PrintStream(new FileOutputStream("Aufgabe2.txt")));
        System.out.println("Aufgabe 2 - Sortieren");
        System.out.println();

        int[] l;
        int[] upper = new int[]{100, 1000, 10000, 100000, 1000000};


        System.out.format("+-----------+-----------------------------------+-----------------------------------+-----------------------------------+-----------------------------------+%n");
        System.out.format("|%11s|%35s|%35s|%35s|%35s|%n", "RANDOM", "Selection Sort", "Insertion Sort", "Shell Sort", "Quick Sort");
        System.out.format("+-----------+-----------------------------------+-----------------------------------+-----------------------------------+-----------------------------------+%n");
        System.out.format("|%11s|%11s|%11s|%11s|%11s|%11s|%11s|%11s|%11s|%11s|%11s|%11s|%11s|%n", "n", "Vgl.", "Zuw.", "Zeit", "Vgl.", "Zuw.", "Zeit", "Vgl.", "Zuw.", "Zeit", "Vgl.", "Zuw.", "Zeit");
        System.out.format("+-----------+-----------+-----------+-----------+-----------+-----------+-----------+-----------+-----------+-----------+-----------+-----------+-----------+%n");

        for (int n = 0; n < upper.length; n++) {
            System.out.format("|%,11d|", upper[n]);
            l = initRandom(upper[n]);
            selectionSort(l);
            l = initRandom(upper[n]);
            insertionSort(l);
            l = initRandom(upper[n]);
            shellSort(l);
            l = initRandom(upper[n]);
            quickSort(l);
            System.out.println();
        }
        System.out.format("+-----------+-----------+-----------+-----------+-----------+-----------+-----------+-----------+-----------+-----------+-----------+-----------+-----------+%n");
        System.out.println();
        System.out.println();
    }


}
