package app;

import map.ArrayMap;
import map.SortedArrayMap;
import map.Map;
import map.Map.Entry;
import map.SortedMap;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.Objects;

public class Main {

    public static void main(String[] args) throws IOException {
        // testing "ArrayMap"
        Map<String, Person> personByFirstName = new ArrayMap<>();

        // testing "put" with firstName (should overwrite in 6 cases, i.e., 18 entries remain)
        for (String line : Files.readAllLines(Paths.get("persons.txt"))) {
            String[] parts = line.split(" ");
            String firstName = parts[0].replace("\"", "");
            String lastName = parts[1].replace("\"", "");
            int year = Integer.parseInt(parts[2]);
            int month = Integer.parseInt(parts[3]);
            int day = Integer.parseInt(parts[4]);
            LocalDate birthday = LocalDate.of(year, month, day);
            Person person = new Person(firstName, lastName, birthday);
            personByFirstName.put(firstName, person);
        }
        int actualSize = 18;
        assertEquals(actualSize, personByFirstName.size());

        System.out.println("ArrayMap contains " + personByFirstName.size() + " entries:");
        System.out.println(mapToString(personByFirstName));

        // testing "get"
        Person p;
        // normal test
        p = personByFirstName.get("Reinhard");
        assertEquals("Klein", p.getLastName());
        // should have been overwritten/replaced
        p = personByFirstName.get("Andreas");
        assertEquals("Hermann", p.getLastName());
        // null test
        p = personByFirstName.get("?");
        assertEquals(null, p);
        // "size" should not have changed
        assertEquals(actualSize, personByFirstName.size());

        // testing "contains"
        boolean contains;
        contains = personByFirstName.contains("Peter");
        assertEquals(true, contains);
        contains = personByFirstName.contains("?");
        assertEquals(false, contains);
        // "size" should not have changed
        assertEquals(actualSize, personByFirstName.size());

        // testing "remove"
        boolean removed;
        // contained
        removed = personByFirstName.remove("Silvia");
        actualSize--;
        assertEquals(true, removed);
        assertEquals(actualSize, personByFirstName.size());
        // not contained
        removed = personByFirstName.remove("?");
        assertEquals(false, removed);
        assertEquals(actualSize, personByFirstName.size());
        // not contained anymore
        removed = personByFirstName.remove("Silvia");
        assertEquals(false, removed);
        assertEquals(actualSize, personByFirstName.size());

        // testing "keyIterator", "valueIterator", "iterator" by checking if the iteration order is the same
        Iterator<String> keyIterator = personByFirstName.keyIterator();
        Iterator<Person> valueIterator = personByFirstName.valueIterator();
        Iterator<Entry<String, Person>> entryIterator = personByFirstName.iterator();
        int iterationSize = 0;
        while (keyIterator.hasNext()) {
            assertEquals(true, valueIterator.hasNext());
            assertEquals(true, entryIterator.hasNext());
            String firstName = keyIterator.next();
            Person person = valueIterator.next();
            Entry<String, Person> entry = entryIterator.next();
            assertEquals(firstName, person.getFirstName());
            assertEquals(firstName, entry.getKey());
            assertEquals(firstName, entry.getValue().getFirstName());
            assertEquals(person, entry.getValue());
            iterationSize++;
        }
        assertEquals(false, valueIterator.hasNext());
        assertEquals(false, entryIterator.hasNext());
        // "size" should be the same as the number of iterations and should not have changed
        assertEquals(actualSize, iterationSize);
        assertEquals(actualSize, personByFirstName.size());

        System.out.println("\nArrayMap contains " + personByFirstName.size() + " entries:");
        System.out.println(mapToString(personByFirstName));

        // testing "SortedArrayMap"
        SortedMap<LocalDate, Person> personByBirthday = new SortedArrayMap<>();
        // testing "valueIterator" of the ArrayMap again (should be a new iterator object)
        // testing "put" (no overwrites expected)
        valueIterator = personByFirstName.valueIterator();
        while (valueIterator.hasNext()) {
            Person person = valueIterator.next();
            personByBirthday.put(person.getBirthday(), person);
        }
        assertEquals(actualSize, personByBirthday.size());

        System.out.println("\nSortedArrayMap contains " + personByBirthday.size() + " entries:");
        System.out.println(mapToString(personByBirthday));

        // testing "put", "contains" and "get" again
        LocalDate dummyBirthday = LocalDate.of(1999, 3, 28);
        Person dummyPerson1 = new Person("First name 1", "Last name 1", dummyBirthday);
        personByBirthday.put(dummyBirthday, dummyPerson1);
        actualSize++;
        assertEquals(actualSize, personByBirthday.size());
        contains = personByBirthday.contains(dummyBirthday);
        assertEquals(true, contains);
        p = personByBirthday.get(dummyBirthday);
        assertEquals(dummyPerson1, p);
        // overwrite existing entry
        Person dummyPerson2 = new Person("First name 2", "Last name 2", dummyBirthday);
        personByBirthday.put(dummyBirthday, dummyPerson2);
        assertEquals(actualSize, personByBirthday.size());
        contains = personByBirthday.contains(dummyBirthday);
        assertEquals(true, contains);
        p = personByBirthday.get(dummyBirthday);
        assertEquals(dummyPerson2, p);

        // testing whether dummyPerson2 was correctly inserted at the second last position
        // foreach iteration (as all other iterations) preserves the insertion order so the array is sorted as well
        Person[] persons = new Person[personByBirthday.size()];
        int i = 0;
        for (Entry<LocalDate, Person> entry : personByBirthday) {
            persons[i] = entry.getValue();
            i++;
        }
        assertEquals(dummyPerson2, persons[persons.length - 2]);

        // testing whether all entries were correctly inserted
        for (i = 0; i < persons.length - 1; i++) {
            boolean ascendingOrder = persons[i].getBirthday().compareTo(persons[i + 1].getBirthday()) <= 0;
            assertEquals(true, ascendingOrder);
        }

        System.out.println("\nSortedArrayMap contains " + personByBirthday.size() + " entries:");
        System.out.println(mapToString(personByBirthday));
    }

    private static <K, V> String mapToString(Map<K, V> map) {
        StringBuilder sb = new StringBuilder();
        for (Entry<K, V> e : map) {
            sb.append(e.getKey()).append(": ").append(e.getValue()).append("\n");
        }
        if (map.size() > 0) {
            // remove the last unnecessary line break
            sb.deleteCharAt(sb.lastIndexOf("\n"));
        }
        return sb.toString();
    }

    private static void assertEquals(Object expected, Object actual) {
        if (!Objects.equals(expected, actual)) {
            throw new AssertionError("expected: " + expected + ", actual: " + actual);
        }
    }

    private static void assertEquals(boolean expected, boolean actual) {
        if (expected != actual) {
            throw new AssertionError("expected: " + expected + ", actual: " + actual);
        }
    }

    private static void assertEquals(int expected, int actual) {
        if (expected != actual) {
            throw new AssertionError("expected: " + expected + ", actual: " + actual);
        }
    }

}
