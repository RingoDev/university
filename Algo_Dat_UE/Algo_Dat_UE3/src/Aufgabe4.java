import java.io.*;


public class Aufgabe4 {

    public static void main(String[] args) throws FileNotFoundException {

// Umleitung der Ausgabe in die Datei Aufgabe4.txt
        System.setOut(new PrintStream(new FileOutputStream("Aufgabe4.txt")));
        for (int n = -1; n<= 10 ;n++){

            System.out.println("n: "+n);
            try {
                System.out.println("Triangles: " + numberTriangles(n));
            }catch(Exception e){System.out.println(e.toString());}
            System.out.println();
        }

// Aufrufe Ihres Algorithmus mit geeigneten Testdaten
    }
        static int numberTriangles(int n)throws IllegalArgumentException{
            if(n <1) throw new IllegalArgumentException("Value is smaller than 1");
            if(n == 1)return 1;
            return 1+3*numberTriangles(n-1);
        }

}

