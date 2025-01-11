import java.io.*;

class location {

    private final int PLZ;
    private final String name;

    location(int PLZ, String name) {
        this.PLZ = PLZ;
        this.name = name;
    }

    public String toString() {
        return PLZ + " " + name;
    }
}

class resultVector {
    private final location[] VectorLoc;
    private final int[] VectorDist;
    private final boolean[] wentThere;
    boolean started;
    int startIndex;

    resultVector() {
        VectorLoc = new location[Aufgabe1.locations.length];
        VectorDist = new int[Aufgabe1.locations.length];
        wentThere = new boolean[Aufgabe1.locations.length];
        started = false;
    }

    resultVector(location[] loc, int[] dist) {
        VectorLoc = loc.clone();
        VectorDist = dist.clone();
        wentThere = new boolean[Aufgabe1.locations.length];
        started = false;
    }

    boolean wentEverywhere() {
        for (boolean b : wentThere) {
            if (!b) return false;
        }
        return true;
    }

    void removeLast() {
        for (int i = VectorLoc.length; i > 0; i--) {
            if (VectorLoc[i - 1] != null) {
                VectorLoc[i - 1] = null;
                VectorDist[i - 1] = 0;
                return;
            }
        }
    }

    void add(location l, int x) {
        for (int i = 0; i < VectorLoc.length; i++) {
            if (VectorLoc[i] == null) {
                VectorLoc[i] = l;
                VectorDist[i] = x;
                return;
            }
        }
    }

    int sum() {
        int sum = 0;
        for (int i : VectorDist) {
            sum += i;
        }
        return sum;
    }

    void goThere(int i) {
        wentThere[i] = true;
    }

    void unGoThere(int i) {
        wentThere[i] = false;
    }

    boolean notBeenThere(int i) {
        return !wentThere[i];
    }

    resultVector copy() {
        return new resultVector(this.VectorLoc, this.VectorDist);
    }

    void print() {
        for (int i = 0; i < VectorLoc.length; i++) {
            if (VectorLoc[i] != null) {
                System.out.println(VectorLoc[i].toString() + " " + ((double) VectorDist[i]) / 10.0 + " km");
            }
        }
        if (VectorLoc[0] != null) System.out.println(VectorLoc[0].toString());
        System.out.println("\nLänge der Rundfahrt: " + ((double) sum()) / 10.0 + " km");
    }

    void start(int i){
        if(!started){
            started =true;
            startIndex = i;
            goThere(i);
        }
    }

    int getStartIndex(){
        return startIndex;
    }
}

public class Aufgabe1 {

    static final int INF = Integer.MAX_VALUE;
    public static location[] locations = {
            new location(4072, "Alkhoven"), new location(4082, "Aschach"), new location(4070, "Eferding"),
            new location(4072, "Fraham"), new location(4083, "Haibach"), new location(4081, "Hartkirchen"),
            new location(4070, "Hinzenbach"), new location(4731, "Prambachkirchen"), new location(4070, "Pupping"),
            new location(4612, "Scharten"), new location(4076, "St.Marienkirchen"), new location(4074, "Stroheim")
    };

    static int[][] distance = {
            {0, 141, 78, 61, 249, 142, 91, 182, 123, 87, 155, 150},
            {141, 0, 76, 109, 114, 22, 78, 137, 47, 154, 177, 76},
            {78, 76, 0, 31, 164, 72, 14, 104, 39, 76, 104, 68},
            {61, 109, 31, 0, 196, 104, 46, 129, 71, 41, 107, 100},
            {249, 114, 164, 196, 0, 92, 159, 162, 124, 240, 262, 134},
            {142, 22, 72, 104, 92, 0, 67, 135, 34, 144, 163, 55},
            {91, 78, 14, 46, 159, 67, 0, 92, 34, 90, 96, 54},
            {182, 137, 104, 129, 162, 135, 92, 0, 115, 140, 74, 61},
            {123, 47, 39, 71, 124, 34, 34, 115, 0, 111, 130, 51},
            {87, 154, 76, 41, 240, 144, 90, 140, 111, 0, 105, 146},
            {155, 177, 104, 107, 262, 163, 96, 74, 130, 105, 0, 120},
            {150, 76, 68, 100, 134, 55, 54, 61, 51, 146, 120, 0}
    };

    static resultVector vector = new resultVector();
    static resultVector vectorOptimum = new resultVector();

    static int tspBacktrackingCalls;
    static int sumOpt = INF;

    static void reset() {
        vector = new resultVector();
    }

    static void resetOpt() {
        vectorOptimum = new resultVector();
        sumOpt = INF;
    }

    static void tspOriginalOrder() {

        System.out.println("Rundfahrt in der ursprünglichen Reihenfolge der Gemeinden:\n");

        for (int i = 0; i < distance.length; i++) {
            if (i != distance.length - 1) vector.add(locations[i], distance[i][i + 1]);
            else vector.add(locations[i], distance[i][0]);
        }
        vector.print();
    }

    static void tspNearestNeighbourSimpleSolutionRecursive(int idx) {
        vector.start(idx);
        vector.goThere(idx);
        int closest = -1;
        int smallest = INF;
        if (vector.wentEverywhere()) {
            vector.add(locations[idx], distance[idx][vector.getStartIndex()]);
            return;
        }
        for (int i = 0; i < locations.length; i++) {
            if (distance[idx][i] < smallest && vector.notBeenThere(i) && i != idx) {
                smallest = distance[idx][i];
                closest = i;
            }
        }
        vector.add(locations[idx], smallest);
        tspNearestNeighbourSimpleSolutionRecursive(closest);
    }

    static void tspNearestNeighbourSimpleSolution() {
        reset();
        System.out.println("\n===================================================================================\n");
        System.out.println("Fahrt zur jeweils nächstgelegenen Gemeinde beginnend mit Alkhoven:\n");
        tspNearestNeighbourSimpleSolutionRecursive( 0);//starting in Alkhoven
        vector.print();
    }

    static void tspNearestNeighbourBestSolution() {

        System.out.println("\n===================================================================================\n");
        System.out.println("Fahrt zur jeweils nächstgelegenen Gemeinde beginnend mit der optimal gelegenen Gemeinde:");
        System.out.println();
        for (int i = 0; i < locations.length; i++) {
            reset();
            tspNearestNeighbourSimpleSolutionRecursive(i);
            if (vector.sum() < sumOpt) {
                vectorOptimum = vector.copy();
                sumOpt = vector.sum();
            }
        }
        vectorOptimum.print();
    }

    static void tspBacktrackingBestSolution() {

        System.out.println("\n===================================================================================\n");
        System.out.println("Optimale Rundfahrt mit Backtracking:\n");
        reset();
        resetOpt();
        tspBacktrackingrecursive(5);
        vectorOptimum.print();
        System.out.println("\nAnzahl der rekursiven Aufrufe der Backtracking-Funktion: " + tspBacktrackingCalls);
    }

    static void tspBacktrackingrecursive(int idx) {

        vector.start(idx);
        if (vector.wentEverywhere()) {//found new vector;
            vector.add(locations[idx], distance[idx][vector.getStartIndex()]);
            if (vector.sum() < sumOpt) {
                sumOpt = vector.sum();
                vectorOptimum = vector.copy();
            }
            vector.removeLast();
            return;
        }
        for (int i = 0; i < locations.length; i++) {
            if (vector.notBeenThere(i) && i != idx) {
                vector.add(locations[idx], distance[idx][i]);
                if (vector.sum() < sumOpt) { //try to find a better solution
                    vector.goThere(i);
                    tspBacktrackingCalls++;
                    tspBacktrackingrecursive(i);
                    vector.unGoThere(i);
                }
                vector.removeLast();
            }
        }
    }

    public static void main(String[] args) throws FileNotFoundException {

        System.setOut(new PrintStream(new FileOutputStream("Aufgabe1.txt")));

        tspOriginalOrder();
        tspNearestNeighbourSimpleSolution();
        tspNearestNeighbourBestSolution();
        tspBacktrackingBestSolution();
    }
}


