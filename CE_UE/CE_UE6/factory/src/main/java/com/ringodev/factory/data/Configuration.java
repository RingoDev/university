package com.ringodev.factory.data;

import java.util.ArrayList;
import java.util.List;

public class Configuration {
    private String handlebarType;
    private String handlebarMaterial;
    private String handlebarGearshift;
    private String handleType;




    public Configuration() {

    }

    public List<String> getAll() {
        List<String> list = new ArrayList<>(4);
        if (handlebarType != null) list.add(handlebarType);
        if (handlebarMaterial != null) list.add(handlebarMaterial);
        if (handlebarGearshift != null) list.add(handlebarGearshift);
        if (handleType != null) list.add(handleType);
        return list;
    }

    public String getHandlebarType() {
        return handlebarType;
    }

    public void setHandlebarType(String handlebarType) {
        this.handlebarType = handlebarType;
    }

    public String getHandlebarMaterial() {
        return handlebarMaterial;
    }

    public void setHandlebarMaterial(String handlebarMaterial) {
        this.handlebarMaterial = handlebarMaterial;
    }

    public String getHandlebarGearshift() {
        return handlebarGearshift;
    }

    public void setHandlebarGearshift(String handlebarGearshift) {
        this.handlebarGearshift = handlebarGearshift;
    }

    public String getHandleType() {
        return handleType;
    }

    public void setHandleType(String handleType) {
        this.handleType = handleType;
    }

    @Override
    public String toString() {
        return "Configuration{" +
                "handlebarType='" + handlebarType + '\'' +
                ", handlebarMaterial='" + handlebarMaterial + '\'' +
                ", handlebarGearshift='" + handlebarGearshift + '\'' +
                ", handleType='" + handleType + '\'' +
                '}';
    }
}
