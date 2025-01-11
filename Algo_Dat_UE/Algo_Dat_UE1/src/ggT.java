import java.io.*;

public class ggT {
    public static void main(String[] args) throws FileNotFoundException {

// Umleitung der Ausgabe in die Datei ggt.txt
        System.setOut(new PrintStream(new FileOutputStream("ggt.txt")));
        System.out.println("Ausgabe für Aufgabe 1:");

// Aufrufe Ihres Algorithmus mit geeigneten Testdaten

        int a = 63972;
        int b = 90144;
        System.out.println(EuklidischerAlgorithmus(a, b));
    }
    //rekursiver Euklidischer Algorithmus

    public static int EuklidischerAlgorithmus(int a, int b) {
        if (b == 0) {
            return a;

        } else {
            if (a == 0) {
                return b;
            } else {
                if (a > b) {
                    return EuklidischerAlgorithmus(a - b, b);
                } else {
                    return EuklidischerAlgorithmus(a, b - a);
                }
            }
        }
    }
}
