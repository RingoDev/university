package app;

import app.developer.Developer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

public class DeveloperTests {

    Developer dev;

    @BeforeEach
    void initialize() {
        this.dev = new Developer(0, "Thomas", "Grininger");
    }

    /**
     * basic Test of Getter and Setter functionality
     */
    @Test
    void getterAndSetterTest() {
        assertEquals(dev.getFirstname(), "Thomas");
        dev.setFirstname("Christopher");
        assertEquals(dev.getFirstname(), "Christopher");

        assertEquals(dev.getSurname(), "Grininger");
        dev.setSurname("Gusenbauer");
        assertEquals(dev.getSurname(), "Gusenbauer");

        assertEquals(dev.getId(), 0);
        dev.setId(42);
        assertEquals(dev.getId(), 42);
    }
}
