package com.github.maracas.roseau.model;


import java.util.List;

public class ConstructorDeclaration {
    public String name;
    public AccessModifier visibility;
    public String returnType;
    public List<String> parametersTypes;

    public ConstructorDeclaration(String name, AccessModifier visibility, String returnType, List<String> parametersTypes) {
        this.name = name;
        this.visibility = visibility;
        this.returnType = returnType;
        this.parametersTypes = parametersTypes;
    }

    public String getName() {
        return name;
    }

    public AccessModifier getVisibility() {
        return visibility;
    }

    public String getReturnType() {
        return returnType;
    }

    public List<String> getParameters() { return parametersTypes; }

    public void printConstructor() {
        System.out.println("Constructor: " + visibility + " " + returnType + " " + name);
    }
}
