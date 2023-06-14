package com.github.maracas.roseau.model;

public class TypeDeclaration {
    public String name;
    public AccessModifier visibility;
    public TypeType typeType;

    public TypeDeclaration(String name, AccessModifier visibility, TypeType typeType) {
        this.name = name;
        this.visibility = visibility;
        this.typeType = typeType;
    }

    public String getName() {
        return name;
    }

    public AccessModifier getVisibility() {
        return visibility;
    }

    public TypeType getDataType() {
        return typeType;
    }

    public void printType() {
        System.out.println("Type: " + visibility + " " + typeType + " " + name);
    }
}


