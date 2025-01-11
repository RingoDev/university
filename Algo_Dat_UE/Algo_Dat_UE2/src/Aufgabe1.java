import java.io.*;
import java.util.concurrent.ThreadLocalRandom;

public class Aufgabe1 {
    final static int min = 0;
    final static int max = 5;
    public static float sum = 0;


    public static void main(String[] args) throws FileNotFoundException {

// Umleitung der Ausgabe in die Datei Aufgabe1.txt
        System.setOut(new PrintStream(new FileOutputStream("Aufgabe1.txt")));
        System.out.println("Ausgabe für Aufgabe 1:");

// Aufrufe Ihres Algorithmus mit geeigneten Testdaten

        int rep = 1000000;
        for(int i = 0; i<21;i++){


            run(rep, i);
            System.out.println("Average Schleifendurchläufe bei Array Länge " +i+": "+ Math.round((sum/rep)*100)/100.0);
            System.out.println();
            sum = 0;

        }
    }



    // nextInt is normally exclusive of the top value,
// so add 1 to make it inclusive

    public static int[] makeArray(int x) {
        int[] array = new int[x];
        for (int i = 0; i < array.length; i++) {
            array[i] = ThreadLocalRandom.current().nextInt(min, max + 1);
        }
        return array;
    }

    static boolean Test(int[] values) {
        //messwerte nur von 0 bis 5
        int i;
        for (i = 0; i < values.length; i++) {
            if (values[i]>=2) {
                return false;
            }

        }
        return true;
    }

    public static boolean pressureIsOK(int[] values) {
        if(values == null)return true;
        for (int i = 0; i < values.length; i++) {
            if (values[i] < 2) {
                sum += (i + 1);
                return false;
            }
        }
        sum += (values.length);
        return true;
    }

    public static void run(int rep, int length) {
        for (int i = 0; i < rep; i++) {
            pressureIsOK(makeArray(length));
        }
    }
}

//minimale Anzahl an Durchläufen = 1
//maximal Anzahl an Durchläufen = n
//durchschnittliche Anzahl = min+max/2 = (1+n)/2