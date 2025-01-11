

import java.io.*;
import java.util.Random;

class Point {

    double x, y, z;


    Point(double dx, double dy, double dz) {
        x = dx;
        y = dy;
        z = dz;
    }

    Point middlePoint(Point p) {
        Point m = new Point(0.0, 0.0, 0.0);
        m.x = (x + p.x) / 2.0;
        m.y = (y + p.y) / 2.0;
        m.z = (z + p.z) / 2.0;
        return m;
    }

    Point extensionPoint(Point middleP, Point p2) {
        Point result = new Point(0.0, 0.0, 0.0);
        result.x = (middleP.x + (this.x - p2.x) / 2.0);
        result.y = (middleP.y + (this.y - p2.y) / 2.0);
        result.z = (middleP.z + (this.z - p2.z) / 2.0);
        return result;

    }
}


public class Aufgabe1 {


    public static void recursion(Point p1, Point p2, Point p3, int n) {
        if (n > 0) {


            System.out.println("	facet normal 0.0 0.0 0.0");
            System.out.println("		outer loop");
            System.out.println("			vertex " + p1.x + " " + p1.y + " " + p1.z);
            System.out.println("			vertex " + p2.x + " " + p2.y + " " + p2.z);
            System.out.println("			vertex " + p3.x + " " + p3.y + " " + p3.z);
            System.out.println("		endloop");
            System.out.println("	endfacet");

            Point mp1 = p1.middlePoint(p2);
            Point mp2 = p2.middlePoint(p3);
            Point mp3 = p3.middlePoint(p1);

            recursion(p1.extensionPoint(mp1, p3), p2.extensionPoint(mp1, p3), mp1, n - 1);
            recursion(p2.extensionPoint(mp2,p1), p3.extensionPoint(mp2,p1), mp2, n - 1);
            recursion(p3.extensionPoint(mp3,p2), p1.extensionPoint(mp3,p2),mp3,n-1);

        }
    }


    public static void main(String[] args) throws FileNotFoundException {
        int n;
        try {
            n = Integer.parseInt(args[0]);
        } catch (Exception e) {
            n = 8;
        }

        Point p1 = new Point(0, 0, 0.0);
        Point p2 = new Point(2, 0, 0.0);
        Point p3 = new Point(1, 3, 0.0);

        System.setOut(new PrintStream(new FileOutputStream("Aufgabe1.stl")));
        System.out.println("solid AufgabeX");

        recursion(p1, p2, p3, n);

        System.out.println("endsolid");
    }


}
