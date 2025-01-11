import java.io.*;


public class Aufgabe1 {
    final static int rate = 3;

    public static void main(String[] args) throws FileNotFoundException {

// Umleitung der Ausgabe in die Datei Aufgabe1.txt
        System.setOut(new PrintStream(new FileOutputStream("Aufgabe1a.csv")));

// Aufrufe Ihres Algorithmus mit geeigneten Testdaten
        nichtRekursiv(1, 200000);
        System.setOut(new PrintStream(new FileOutputStream("Aufgabe1b.csv")));
        rekursiv(1,200000,0);
    }



    public static void nichtRekursiv(int inf, int max) {
        int day = 0;
        System.out.println("tag;infiziert");
        System.out.println(day + ";" + inf);
        while (inf < max) {
            int temp = inf * rate;
            inf += temp;
            day++;
            if (inf > max) System.out.println(day + ";" + max);
            else System.out.println(day + ";" + inf);
        }

    }

    public static int rekursiv(int inf, int max,int day) {
        System.out.println("tag;infiziert");
        if (inf < max) {
            System.out.println(day + ";" + inf);

            int temp = inf * rate;
            inf += temp;
            day++;

            inf = rekursiv(inf, max,day);

        }
        else System.out.println(day + ";" + max);
        return inf;
    }
}
