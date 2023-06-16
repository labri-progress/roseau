package com.github.maracas.roseau.model;


import java.util.List;

public class ConstructorDeclaration {
    public String name;
    public AccessModifier visibility;
    public String returnType;
    public List<String> parametersTypes;
    public List<NonAccessModifiers> Modifiers;
    public Signature signature;
    public ConstructorDeclaration(String name, AccessModifier visibility, String returnType, List<String> parametersTypes, List<NonAccessModifiers> Modifiers, Signature signature) {
        this.name = name;
        this.visibility = visibility;
        this.returnType = returnType;
        this.parametersTypes = parametersTypes;
        this.Modifiers = Modifiers;
        this.signature = signature;
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

    public List<String> getParametersTypes() { return parametersTypes; }

    public List<NonAccessModifiers> getModifiers() { return Modifiers; }

    public Signature getSignature() { return signature; }

    public void printConstructor() {
        System.out.println("Constructor: " + visibility + " " + returnType + " " + name);
    }
}
