package Aufgabe3;

import java.io.*;
import java.util.Random;


class Point3D {
    double x, y, z;
    Random r;

    Point3D(double dx, double dy, double dz) {
        r = new Random();
        x = dx;
        y = dy;
        z = dz;
    }

    Point3D randomZmiddlePoint(Point3D p,int n) {
        Point3D m = new Point3D(0.0, 0.0, 0.0);
        m.x = (x + p.x) / 2.0;
        m.y = (y + p.y) / 2.0;
        r.setSeed((long) ((x + p.x + y + p.y + z + p.z) * 100000));
        m.z = ((z+p.z)/2.0)+(r.nextDouble() - 0.5)*Math.pow(2,n)/4.0;
        return m;
    }
}


public class Aufgabe3 {

    public static void printDreieck(Point3D p1, Point3D p2, Point3D p3) {
        System.out.println("	facet normal 0.0 0.0 0.0");
        System.out.println("		outer loop");
        System.out.println("			vertex " + p1.x + " " + p1.y + " " + p1.z);
        System.out.println("			vertex " + p2.x + " " + p2.y + " " + p2.z);
        System.out.println("			vertex " + p3.x + " " + p3.y + " " + p3.z);
        System.out.println("		endloop");
        System.out.println("	endfacet");
    }

    public static void recursion(Point3D p1, Point3D p2, Point3D p3, int n) {

        if (n > 0) {
            if (n == 1) {
                printDreieck(p1, p2, p3);
            }

            recursion(p1, p1.randomZmiddlePoint(p2,n), p1.randomZmiddlePoint(p3,n), n - 1);
            recursion(p1.randomZmiddlePoint(p2,n), p2, p2.randomZmiddlePoint(p3,n), n - 1);
            recursion(p1.randomZmiddlePoint(p2,n), p2.randomZmiddlePoint(p3,n), p1.randomZmiddlePoint(p3,n), n - 1);
            recursion(p1.randomZmiddlePoint(p3,n), p2.randomZmiddlePoint(p3,n), p3, n - 1);

        }
    }

    public static void main(String[] args) throws FileNotFoundException {

        int n;

        try {
            n = Integer.parseInt(args[0]);
        } catch (Exception e) {
            n = 9;
        }

        System.out.println("Aufgabe 3: Landschaftsgenerator");
        System.out.println("n = " + n);


        Point3D p1 = new Point3D(10*n, -15*n, 0.0);
        Point3D p2 = new Point3D(12.5*n, 15.25*n, 0.0);
        Point3D p3 = new Point3D(-12.5 *n, 0.0, 0.0);

        System.setOut(new PrintStream(new FileOutputStream("Aufgabe3.stl")));
        System.out.println("solid Aufgabe3");

        recursion(p1, p2, p3, n);

        System.out.println("endsolid");
    }
    
}
