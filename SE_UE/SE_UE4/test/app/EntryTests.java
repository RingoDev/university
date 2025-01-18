package app;

import app.developer.Developer;
import app.taskboard.task.Entry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EntryTests {

    Entry entry;
    Developer dev;

    @BeforeEach
    void initialize() {
        this.dev = new Developer(0, "Thomas", "Grininger");
        this.entry = new Entry(dev, 42, new Date(0));
    }


    /**
     * basic Test of Getter and Setter functionality
     */
    @Test
    void getterAndSetterTest() {

        Date newDate = new Date(42);
        assertEquals(entry.getDate(), new Date(0));
        entry.setDate(newDate);
        assertEquals(entry.getDate(), newDate);

        Developer newDev = new Developer(1, "Christopher", "Gusenbauer");
        assertEquals(entry.getDev(), dev);
        entry.setDev(newDev);
        assertEquals(entry.getDev(), newDev);

        assertEquals(entry.getExpenditure(), 42);
        entry.setExpenditure(0);
        assertEquals(entry.getExpenditure(), 0);
    }
}
