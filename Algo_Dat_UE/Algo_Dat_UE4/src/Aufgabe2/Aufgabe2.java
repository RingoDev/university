package Aufgabe2;

import java.io.*;

class Point3D {
    double x, y, z;


    Point3D(double angle) {
        this.x = (Math.cos(Math.toRadians(angle)));
        this.y = (Math.sin(Math.toRadians(angle)));
        this.z = 0.0;
    }
}

public class Aufgabe2 {

    public static void printDreieck(Point3D p1, Point3D p2, Point3D p3) {
        System.out.println("	facet normal 0.0 0.0 0.0");
        System.out.println("		outer loop");
        System.out.println("			vertex " + p1.x + " " + p1.y + " " + p1.z);
        System.out.println("			vertex " + p2.x + " " + p2.y + " " + p2.z);
        System.out.println("			vertex " + p3.x + " " + p3.y + " " + p3.z);
        System.out.println("		endloop");
        System.out.println("	endfacet");
    }

    public static void draw(int n) {
        Point3D p1 = new Point3D(210);
        Point3D p2 = new Point3D(330);
        Point3D p3 = new Point3D(90);

        printDreieck(p1, p2, p3);


        recursion(210, 330, n);
        recursion(330, 90, n);
        recursion(90, 210, n);


    }

    public static void recursion(double angle1, double angle2, int n) {
        Point3D p1 = new Point3D(angle1);
        Point3D p2 = new Point3D(angle2);
        double a3;

        // 330° -> 30°
        if (angle1 > angle2) {

            // 330° -> 345°
            if (angle1 + angle2 >= 360) a3 = ((angle1 + angle2) - 360) / 2;

            // 330° -> 10°
            else a3 = (angle1 + angle2 + 360) / 2;

        // 90° -> 270°
        } else a3 = (angle1 + angle2) / 2;
        Point3D p3 = new Point3D(a3);

        if (n > 0) {
            printDreieck(p1, p2, p3);
            recursion(a3, angle2, n - 1);
            recursion(angle1, a3, n - 1);
        }
    }

    public static void main(String[] args) throws FileNotFoundException {

        int n;

        try {
            n = Integer.parseInt(args[0]);
        } catch (Exception e) {
            n = 8;
        }

        System.out.println("Aufgabe 2: Kreis-Annäherung");
        System.out.println("n = " + n);
        System.setOut(new PrintStream(new FileOutputStream("Aufgabe2.stl")));

        System.out.println("solid Aufgabe2");

        draw(n);

        System.out.println("endsolid");
    }
}
