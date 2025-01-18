package com.ringodev.factory;

import javax.persistence.*;
import java.util.List;

@Entity
public class Option {

    @Id
    Part part;
    @ElementCollection
    List<String> types;

    public Option(Part part, List<String> types) {
        this.part = part;
        this.types = types;
    }

    public Option() {

    }

    public Part getPart() {
        return part;
    }

    public void setPart(Part part) {
        this.part = part;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }
}
