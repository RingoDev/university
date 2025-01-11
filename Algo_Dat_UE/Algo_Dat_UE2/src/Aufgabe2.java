import java.io.*;
import java.util.*;


public class Aufgabe2 {
    //code

    static double insertionSort(int[] field) {
        double sum = 0;
        //code
        int temp;

        int i = 1;


        while (i < field.length) {
            temp = field[i];
            int j = i;
            while (j > 0 && field[j - 1] > temp) {
                field[j] = field[j - 1];
                j--;
                sum++;
            }
            field[j] = temp;
            i++;
        }
        return sum;
    }

    static void initField(int[] field) {
        Random randomGenerator = new Random();
        for (int i = 0; i < field.length; i++) {
            field[i] = randomGenerator.nextInt(field.length) + 1;
        }
    }


    public static void main(String[] args) throws FileNotFoundException {

        // Umleitung der Ausgabe in die Datei Aufgabe2.txt
        System.setOut(new PrintStream(new FileOutputStream("Aufgabe2.csv")));
        System.out.println("n;u;laufzeit");

        for (int n = 0; n <= 1000; n += 10) {
            int[] field = new int[n];
            int samples = 100;

            int u = 0;

            //code

            for (int i = 0; i < samples; i++) {

                //code

                initField(field);
                u += insertionSort(field);

                //code

            }
            int uAvg = u / samples;
            long runtimeAvg;
            if (n == 0) {
                runtimeAvg = (long) 2.5;
            } else {
                int v = n - 1;
                runtimeAvg = (long) (2.5 + 22.4 * v + 20.3 * uAvg);
            }


            System.out.println(n + ";" + uAvg + ";" + runtimeAvg);

        }
    }
}
