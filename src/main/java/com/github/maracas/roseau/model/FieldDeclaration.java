package com.github.maracas.roseau.model;


public class FieldDeclaration {
    public String name;
    public AccessModifier visibility;
    public String dataType;



    public FieldDeclaration(String name, AccessModifier visibility, String dataType) {
        this.name = name;
        this.visibility = visibility;
        this.dataType = dataType;

    }

    public String getName() {
        return name;
    }

    public AccessModifier getVisibility() {
        return visibility;
    }

    public String getDataType() {
        return dataType;
    }




    public void printField() {
        System.out.println("Field: " + visibility + " " + dataType + " " + name);
    }
}