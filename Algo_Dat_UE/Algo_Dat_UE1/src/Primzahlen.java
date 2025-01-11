import java.io.*;

public class Primzahlen {
    public static void main(String[] args) throws FileNotFoundException {

// Umleitung der Ausgabe in die Datei Primzahlen.txt
        System.setOut(new PrintStream(new FileOutputStream("Primzahlen.txt")));
        System.out.println("Ausgabe für Aufgabe 1:");

// Aufrufe Ihres Algorithmus mit geeigneten Testdaten

        int x = 1000;

        SiebEratosthenes(x);
    }

    public static void SiebEratosthenes(int x) {
        int[] array = new int[x - 1];
        for (int i = 0; i < x - 1; i++) {
            array[i] = i + 2;
        }
        for (int k = 0; k < array.length; k++) {
            int temp = array[k];
            if (temp != 0) {
                for (int l = 0; l < array.length; l++) {
                    if (array[l] % temp == 0) {//test if number is divisible by temp
                        array[l] = 0;//set number to zero(marked)
                    }
                }
                System.out.print(temp + ",");
            }
        }
    }
}