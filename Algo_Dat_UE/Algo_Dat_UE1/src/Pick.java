import java.io.*;

public class Pick {
    public static void main(String[] args) throws FileNotFoundException {

// Umleitung der Ausgabe in die Datei Pick.txt
        System.setOut(new PrintStream(new FileOutputStream("Pick.txt")));
        System.out.println("Ausgabe für Aufgabe 1:");

// Aufrufe des Algorithmus mit geeigneten Testdaten

        int[][] dreieck = new int[][]{
                {}, // zeile 0
                {}, // zeile 1
                {2, 3}, // zeile 2
                {3},
        };

        int[][] quadrat = new int[][]{
                {}, // zeile 0
                {1, 2}, // zeile 1
                {1, 2} // zeile 2
        };
        int[][] achteck = new int[][]{
                {1, 2}, // zeile 0
                {0, 3}, // zeile 1
                {0, 3}, // zeile 2
                {1, 2} // zeile 3
        };
        int[][] vieleck = new int[][]{
                {2}, // zeile 0
                {0, 3}, // zeile 1
                {0, 3}, // zeile 2
                {1, 6}, // zeile 3
                {2, 6}, // zeile 4
                {2, 5}, // zeile 5
                {4}, // zeile 5
        };
        int[][] skewedSquare = new int[][]{
                {3},
                {2, 4},
                {1, 5},
                {2, 4},
                {3}

        };
        int[][] randomPolygon = new int[][]{
                {6},
                {2, 5},
                {2, 3},
                {1, 2},

        };
        int[][] randomPolygon2 = new int[][]{
                {1},
                {1, 4},
                {1, 5},
                {1, 6},
                {1}

        };
        int[][] negativePolygon = new int [][]{
                {-1},
                {-1, 2},
                {-1, 3},
                {-1, 4},
                {-1}
        };

        System.out.println("Dreieck");
        SatzvonPick(dreieck);
        System.out.println();
        System.out.println("Quadrat");
        SatzvonPick(quadrat);
        System.out.println();
        System.out.println("Achteck");
        SatzvonPick(achteck);
        System.out.println();
        System.out.println("Vieleck");
        SatzvonPick(vieleck);
        System.out.println();
        System.out.println("schiefes Quadrat");
        SatzvonPick(skewedSquare);
        System.out.println();
        System.out.println("random Polygon");
        SatzvonPick(randomPolygon);
        System.out.println();
        System.out.println("random Polygon 2");
        SatzvonPick(randomPolygon2);
        System.out.println();
        System.out.println("trying negative coordinates // should have same Area as random Polygon 2");
        SatzvonPick(negativePolygon);
        System.out.println();

    }

    public static void SatzvonPick(int[][] x) {
        //Flaeche = Innenpunkte + (Randpunkte/2) -1
        double I = 0;
        double R = 0;
        for (int[] ints : x) {
            boolean nothingFound = true;
            //a variable to store the y coordinate of the first found point in
            int y = 0;

            for (int anInt : ints) {
                //if its the first point in this line
                if (nothingFound) {
                    //store value of y-coordinate
                    y = anInt;
                    nothingFound = false;

                } else {//if its the second point in the line calculate difference minus 1 and add to Innerpoints count
                    I += ((anInt - y) - 1);
                }
                R++;
            }
        }
        double Area = I + (R / 2) - 1;
        System.out.println("Fläche des Gitterpolygons mit " + R + " Randpunkten: " + Area);
    }
}

//A = I + R/2 - 1