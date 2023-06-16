package com.github.maracas.roseau.model;


import java.util.List;

public class FieldDeclaration {
    public String name;
    public AccessModifier visibility;
    public String dataType;
    public List<NonAccessModifiers> Modifiers;



    public FieldDeclaration(String name, AccessModifier visibility, String dataType, List<NonAccessModifiers> Modifiers) {
        this.name = name;
        this.visibility = visibility;
        this.dataType = dataType;
        this.Modifiers = Modifiers;
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

    public List<NonAccessModifiers> getModifiers() { return Modifiers; }




    public void printField() {
        System.out.println("Field: " + visibility + " " + dataType + " " + name);
    }
}