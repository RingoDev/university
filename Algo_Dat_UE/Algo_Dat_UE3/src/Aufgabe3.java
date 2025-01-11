import java.io.*;
import java.util.Random;


public class Aufgabe3 {

    public static void main(String[] args) throws FileNotFoundException {

        // Umleitung der Ausgabe in die Datei Aufgabe3.csv
        System.setOut(new PrintStream(new FileOutputStream("Aufgabe3.csv")));

        // Aufrufe Ihres Algorithmus mit geeigneten Testdaten
        long Rec = 0;
        long Iter = 0;


        System.out.println("length;runtime recursive;list");
        for (int i = 100; i < 200; i++) {
            long sum = 0;
            int[] array = initField(getField(i));
            int[] sortedArray = insertionSort(array);//sorted random arrays
            for (int val : sortedArray) {
                for (int k = 0; k < 10000; k++) {//search every value 10000 times
                    long x = System.nanoTime();
                    binarySearchRecursive(sortedArray, val, 0, sortedArray.length);
                    sum += System.nanoTime() - x;
                }
            }
            Rec += sum;
            System.out.println(i + ";" + sum + ";" + stringify(array));
        }

        System.out.println(";;");
        System.out.println("length;runtime iterative;list");
        for (int i = 100; i < 200; i++) {
            long sum = 0;
            int[] array = initField(getField(i));
            int[] sortedArray = insertionSort(array);//sorted random arrays
            for (int val : sortedArray) {
                for (int k = 0; k < 10000; k++) {//search every value 10000 times
                    long x = System.nanoTime();
                    binarySearchIterative(sortedArray, val, 0, array.length);
                    sum += System.nanoTime() - x;
                }
            }
            Iter += sum;
            System.out.println(i + ";" + sum + ";" + stringify(array));
        }
        System.out.println(";;");
        System.out.println(";;");
        System.out.println("Recursive;"+Rec+";");
        System.out.println("Iterativ;"+Iter+";");
        double difference = (Rec-Iter)/1000000000.0;
        System.out.println("Difference in seconds;"+difference+";");

    }


    public static String stringify(int[] array) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int x : array) {
            sb.append(x).append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append("]");
        return sb.toString();
    }

    public static int binarySearchIterative(int[] list, int x, int lowerIndex, int upperIndex) throws IllegalArgumentException {
        if (lowerIndex > upperIndex) throw new IllegalArgumentException("Lower Index Higher than Upper index");

        while (upperIndex >= lowerIndex) {
            int m = (lowerIndex + upperIndex) / 2;
            if (list[m] == x) return m;
            if (x < list[m]) upperIndex = m - 1;
            else lowerIndex = m + 1;
        }
        return -1;
    }


    public static int binarySearchRecursive(int[] list, int x, int lowerIndex, int upperIndex) throws IllegalArgumentException {
        if (lowerIndex > upperIndex) throw new IllegalArgumentException("Lower Index Higher than Upper index");
        int m = (lowerIndex + upperIndex) / 2;
        if (list[m] == x)
            return m;
        if (x < list[m])
            return binarySearchRecursive(list, x, lowerIndex, m - 1);
        return binarySearchRecursive(list, x, m + 1, upperIndex);
    }


    static int[] initField(int[] field) {
        Random randomGenerator = new Random();
        for (int i = 0; i < field.length; i++) {
            field[i] = randomGenerator.nextInt(999) + 1;
        }
        return field;
    }

    public static int[] getField(int length) {
        return new int[length];
    }

    static int[] insertionSort(int[] field) {
        int temp;
        int i = 1;

        while (i < field.length) {
            temp = field[i];
            int j = i;
            while (j > 0 && field[j - 1] > temp) {
                field[j] = field[j - 1];
                j--;
            }
            field[j] = temp;
            i++;
        }
        return field;
    }
}
