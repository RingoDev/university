import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class App {
        public static void main(String[] args) throws FileNotFoundException {

// Umleitung der Ausgabe in die Datei Primzahlen.txt
            System.setOut(new PrintStream(new FileOutputStream("Primzahlen.txt")));
            System.out.println("Ausgabe für Aufgabe 1:");

// Aufrufe Ihres Algorithmus mit geeigneten Testdaten

            int x = 5;
            try {
                for(int prim : getPrimzahlen(x)){
                    System.out.println(prim+",");
                }

            }catch(Exception e){
                System.out.println(e.getMessage());
            }
        }

    static int[] getPrimzahlen(int max) throws Exception {
        if (max < 0) throw new Exception("Negative Number entered, not allowed");
        final int largestPrim = (int) Math.sqrt(max) + 2;
        boolean[] numbers = new boolean[max];

        for (int i = 0; i < max; i++)
            numbers[i] = i % 2 == 1;


        int prim = 3;
        if (prim < largestPrim) {
            do {
                if (numbers[prim]) {
                    for (int i = prim; i <= max / prim; i++) {
                        final int zahl = i * prim;
                        if (zahl < max)
                            numbers[zahl] = false;
                    }
                }
                prim += 2;
            } while (prim < largestPrim);
        }


        int anzahl = 0;
        for (boolean istPrim : numbers)
            if (istPrim)
                anzahl++;

        int[] primeNumbers = new int[anzahl];
        int index = 0;
        for (int i = 0; i < numbers.length; i++)
            if (numbers[i])
                primeNumbers[index++] = i;
        primeNumbers[0] = 2;
        return primeNumbers;
    }
}
