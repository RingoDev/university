package map.app;


import java.util.Optional;

import list.List;
import map.ArrayMap;
import map.Map;
import map.Map.Entry;

public class AssignmentRecordsMain {

    public static void main(String[] args) {
        Map<String, List<Integer>> records = new ArrayMap<>();

        records.put("Huber", List.of(8, 12, 22, 21, 20, 0, 17, 19, 3, 18, 16));
        records.put("Maier", List.of(19, 12, 22, 21, 20, 24, 17, 19, 12, 18, 16));
        records.put("Berger", List.of(19, 0, 22, 21, 20, 0, 17, 19, 0, 18, 0));
        records.put("Mueller", List.of(16, 21, 22, 20, 17, 24, 17, 23, 18, 17, 19));
        records.put("Reiter", List.of(24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24));
        records.put("Hofer", List.of(17, 15, 3, 7, 8, 0, 0, 0, 0, 0, 0));
        records.put("Kaiser", List.of(21, 22, 21, 23, 24, 24, 21, 22, 19, 22, 24));
        records.put("Fischer", List.of(23, 23, 22, 22, 20, 23, 19, 19, 20, 18, 19));
        records.put("Schauer", List.of(24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24));

        // TODO:  Number solved
        Map<String, Integer> nrSolved = records.map((k, v) -> v.filter(e -> e >= 8).size());

        // TODO:  Sum of points total
        Map<String, Integer> pointsTotal = records.map((k, v) -> v.filter(e -> e >= 8).reduce(0, Integer::sum));

        // TODO:  Average points
        Map<String, Double> avrgPoints = records.map((k, v) -> {
            List<Integer> temp = v.filter(e -> e >= 8);
            return temp.reduce(0.0, (e, f) -> e + f / (double) temp.size());
        });

        // TODO:  Find a student which has 24 points for all assignments
        Optional<Entry<String, List<Integer>>> all24 = records.find((k, v) -> v.filter(e -> e == 24).size() == v.size());

        // TODO:  Negative results
        Map<String, List<Integer>> negative = records.filter((k, v) -> ((v.filter(e -> e < 8).size() > 2) ||
                (v.filter(x -> x >= 8).reduce(0.0, (e, f) -> e + f / (double) v.filter(a -> a >= 8).size()) < 12)));

        // TODO:  Grades
        Map<String, Grade> grades = records.map((k, v) -> {
            Grade g;
            Double avg = v.reduce(0.0, (e, f) -> e + f / (double) v.size());
            if ((v.filter(e -> e < 6).size() > 2) || avg < 12) return Grade.INSUFFICIENT;
            else if (avg < 15) return Grade.SUFFICIENT;
            else if (avg < 18) return Grade.SATISFACTORY;
            else if (avg < 21) return Grade.GOOD;
            else return Grade.EXCELLENT;
        });

        // TODO:  Grouping students by grades
        Map<Grade, List<String>> gradesDistr = grades.group((k, v) -> v, (k, v) -> k);


        System.out.println("=========================================================");
        System.out.println("Number of exercises succesfully solved:");
        System.out.println("-------------------");

        for (Entry<String, Integer> e : nrSolved) {
            System.out.println(e.getKey() + ": " + e.getValue());
        }

        System.out.println("=========================================================");
        System.out.println("Total points reached:");
        System.out.println("-------------------");

        for (Entry<String, Integer> e : pointsTotal) {
            System.out.println(e.getKey() + ": " + e.getValue());
        }

        System.out.println("=========================================================");
        System.out.println("Average points per solved Exercise:");
        System.out.println("-------------------");

        for (Entry<String, Double> e : avrgPoints) {
            System.out.println(e.getKey() + ": " + Math.round(e.getValue() * 100.0) / 100.0);
        }

        if (all24.isPresent()) {

            System.out.println("=========================================================");
            System.out.println("A student who has 24 Points on all assignments:");
            System.out.println("-------------------");
            StringBuilder sb = new StringBuilder("[");
            for (Integer i : all24.get().getValue()) {
                sb.append(i).append(", ");
            }
            sb.deleteCharAt(sb.length() - 1).deleteCharAt(sb.length() - 1).append("]");
            System.out.println(all24.get().getKey() + ": " + sb.toString());
        }


        System.out.println("=========================================================");
        System.out.println("Students who failed the course:");
        System.out.println("-------------------");
        for (Entry<String, List<Integer>> e : negative) {
            StringBuilder sb = new StringBuilder("[");
            for (Integer i : e.getValue()) {
                sb.append(i).append(", ");
            }
            sb.deleteCharAt(sb.length() - 1).deleteCharAt(sb.length() - 1).append("]");
            System.out.println(e.getKey() + ": " + sb.toString());
        }

        System.out.println("=========================================================");
        System.out.println("Students with their respective grades:");
        System.out.println("-------------------");

        for (Entry<String, Grade> e : grades) {
            System.out.println(e.getKey() + ": " + e.getValue());
        }

        System.out.println("=========================================================");
        System.out.println("Students grouped by grades:");
        System.out.println("-------------------");

        for (Entry<Grade, List<String>> e : gradesDistr) {
            StringBuilder sb = new StringBuilder("[");
            for (String s : e.getValue()) {
                sb.append(s).append(", ");
            }
            sb.deleteCharAt(sb.length() - 1).deleteCharAt(sb.length() - 1).append("]");
            System.out.println(e.getKey() + ": " + sb.toString());
        }
    }
}
