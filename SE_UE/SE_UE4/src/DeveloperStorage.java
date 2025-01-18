import app.developer.Developer;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Mock-Datenbank
 * simuliert den Zugriff auf eine Developer-Datenbank und ermöglicht das Hinzufügen und Getten von Developern nach ID
 */
public class DeveloperStorage {

    private static List<Developer> developers = new ArrayList<>();

    public static List<Developer> getDevelopers() {
        return developers;
    }

    public static void addDeveloper(Developer dev) throws IllegalArgumentException {
        if (developers.stream().anyMatch(d -> d.getId() == dev.getId()))
            throw new IllegalArgumentException("developer.Developer with id " + dev.getId() + " exists already.");
        developers.add(dev);
    }

    /**
     * @param id the id of the developer.Developer
     * @return the developer.Developer with the specified ID
     * @throws NoSuchElementException if no developer.Developer is found
     */
    public static Developer getDeveloper(int id) throws NoSuchElementException {
        return developers.stream().filter(d -> d.getId() == id).findFirst().orElseThrow(() -> new NoSuchElementException("developer.Developer mit id " + id + " nicht vorhanden:"));
    }
}
