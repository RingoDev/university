package app.taskboard.task;

import app.developer.Developer;

import java.util.Date;

/**
 * Zeitdokumentationseintrag eines Developers zu einem Task
 */
public class Entry {

    private Developer dev;
    private double expenditure;
    private Date date;

    public Entry(Developer dev, double expenditure, Date date) {
        this.dev = dev;
        this.expenditure = expenditure;
        this.date = date;
    }

    public Developer getDev() {
        return dev;
    }

    public void setDev(Developer dev) {
        this.dev = dev;
    }

    public double getExpenditure() {
        return expenditure;
    }

    public void setExpenditure(double expenditure) {
        this.expenditure = expenditure;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
