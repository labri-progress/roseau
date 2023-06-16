package com.github.maracas.roseau.model;
import java.lang.reflect.Method;
import java.util.List;
public class TypeDeclaration {
    public String name;
    public AccessModifier visibility;
    public TypeType typeType;
    public List<FieldDeclaration> fields;
    public List<MethodDeclaration> methods;
    public List<ConstructorDeclaration> constructors;
    public List<NonAccessModifiers> Modifiers;

    public TypeDeclaration(String name, AccessModifier visibility, TypeType typeType, List<NonAccessModifiers> Modifiers) {
        this.name = name;
        this.visibility = visibility;
        this.typeType = typeType;
        this.Modifiers = Modifiers;
    }

    public String getName() {
        return name;
    }

    public AccessModifier getVisibility() {
        return visibility;
    }

    public TypeType getTypeType() {
        return typeType;
    }

    public List<FieldDeclaration> getFields() { return fields; }

    public List<MethodDeclaration> getMethods() { return methods; }

    public List<ConstructorDeclaration> getConstructors() { return constructors; }

    public List<NonAccessModifiers> getModifiers() { return Modifiers; }

    public void setFields(List<FieldDeclaration> fields) {
        this.fields = fields;
    }

    public void setMethods(List<MethodDeclaration> methods) {
        this.methods = methods;
    }

    public void setConstructors(List<ConstructorDeclaration> constructors) {
        this.constructors = constructors;
    }
    public void printType() {
        System.out.println("Type: " + visibility + " " + typeType + " " + name);
    }
}


