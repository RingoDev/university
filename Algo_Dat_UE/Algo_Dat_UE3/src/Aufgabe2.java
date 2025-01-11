import java.io.*;
import java.lang.*;


public class Aufgabe2 {

    public static void main(String[] args) throws FileNotFoundException {

        // Umleitung der Ausgabe in die Datei Aufgabe2.txt
        System.setOut(new PrintStream(new FileOutputStream("Aufgabe2.txt")));
        String test = "Geist ziert Leben, Mut hegt Siege, Beileid trägt belegbare Reue, Neid dient nie, nun eint Neid die Neuerer, abgelebt gärt die Liebe, Geist geht, umnebelt reizt Sieg.";
        String test2 = "Leben Sie mit Siegreits Rune. Deine Zier sei dies: Reize nie den Urstiergeist im Eisnebel!";
        String test3 = "otta";
        String test4 = "Ne Biene, die bei den Eiben hastet – sah ’ne Biene die beiden Eiben?";
        String test5 = "";
        // Aufrufe Ihres Algorithmus mit geeigneten Testdaten
        System.out.println(test);
        System.out.println(isPalindrome(test));//true
        System.out.println();
        System.out.println(test2);
        System.out.println(isPalindrome(test2));
        System.out.println();
        System.out.println(test3);
        System.out.println(isPalindrome(test3));
        System.out.println();
        System.out.println(test4);
        System.out.println(isPalindrome(test4));
        System.out.println();
        System.out.println(test5);
        System.out.println(isPalindrome(test5));
    }

    public static boolean isPalindrome(String s) {

        return checkPalindrome(clean(s));
    }

    public static boolean checkPalindrome(String s) {
        if (s.length() <= 1) return true;
        else {
            String shortened = s.substring(1, s.length() - 1);
            if (checkPalindrome(shortened)) {
                return s.charAt(0) == s.charAt(s.length() - 1);
            }
            return false;
        }
    }

    public static String clean(String s) {
        s = s.replaceAll("[^a-zA-Z\u00E4\u00F6\u00FC\u00DF\u00C4\u00D6\u00DC]", "");
        return s.toLowerCase();
    }
}
