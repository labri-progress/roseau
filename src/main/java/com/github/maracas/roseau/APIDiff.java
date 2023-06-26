package com.github.maracas.roseau;

import com.github.maracas.roseau.changes.*;
import com.github.maracas.roseau.model.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.IntStream;


public class APIDiff {
    public API v1;
    public API v2;
    List <BreakingChange> breakingChanges;

    public APIDiff(API v1, API v2) {
        this.v1 = Objects.requireNonNull(v1);
        this.v2 = Objects.requireNonNull(v2);
        this.breakingChanges = new ArrayList<>();
    }

    public List<TypeDeclaration> checkingForRemovedTypes() {
        return v1.getAllTheTypes().stream()
                .filter(type -> v2.getAllTheTypes().stream()
                        .noneMatch(t -> t.getName().equals(type.getName())))
                .peek(removedType -> {
                    System.out.println("Type removed: " + removedType.getName());
                    breakingChanges.add(new BreakingChange(BreakingChangeKind.TYPE_REMOVED, new TypeBreakingChange(removedType)));
                })
                .toList();
    }

    public List<List<TypeDeclaration>> getUnremovedTypes() {

        List<TypeDeclaration> unremovedTypes1 = v1.getAllTheTypes().stream()
                .filter(type -> v2.getAllTheTypes().stream()
                        .anyMatch(t -> t.getName().equals(type.getName())))
                //.peek(removedType -> System.out.println("Type remaining: " + removedType.getName()))

                .toList();


        List<TypeDeclaration> typesInParallelFrom2 = v2.getAllTheTypes().stream()
                .filter(type -> unremovedTypes1.stream()
                        .anyMatch(t -> t.getName().equals(type.getName())))
                //.peek(type -> System.out.println("Types from v2 : " + type.getName()))
                .toList();

        List<List<TypeDeclaration>> result = new ArrayList<>();
        result.add(unremovedTypes1);
        result.add(typesInParallelFrom2);

        return result;

    }


    public List<FieldDeclaration> checkingForRemovedFields(TypeDeclaration type1, TypeDeclaration type2) {
        return type1.getFields().stream()
                .filter(field1 -> type2.getFields().stream()
                        .noneMatch(field2 -> field2.getName().equals(field1.getName())))
                .peek(removedField -> {
                    System.out.println("Field removed: " + removedField.getName());
                    breakingChanges.add(new BreakingChange(BreakingChangeKind.FIELD_REMOVED, new FieldBreakingChange(removedField)));
                })
                .toList();
    }


    public List<MethodDeclaration> checkingForRemovedMethods(TypeDeclaration type1, TypeDeclaration type2) {
        return type1.getMethods().stream()
                .filter(method2 -> type2.getMethods().stream()
                        .noneMatch(method1 -> method1.getSignature().getName().equals(method2.getSignature().getName()) && method1.getSignature().getParameterTypes().equals(method2.getSignature().getParameterTypes())))
                .peek(removedMethod -> {
                    System.out.println("Method removed: " + removedMethod.getName());
                    breakingChanges.add(new BreakingChange(BreakingChangeKind.METHOD_REMOVED, new MethodBreakingChange(removedMethod)));
                })
                        .toList();

    }


    public List<ConstructorDeclaration> checkingForRemovedConstructors(TypeDeclaration type1, TypeDeclaration type2) {
        return type1.getConstructors().stream()
                .filter(constructor1 -> type2.getConstructors().stream()
                        .noneMatch(constructor2 -> constructor2.getSignature().getName().equals(constructor1.getSignature().getName()) && constructor2.getSignature().getParameterTypes().equals(constructor1.getSignature().getParameterTypes())))
                .peek(removedConstructor -> {
                    System.out.println("Constructor removed: " + removedConstructor.getName());
                    breakingChanges.add(new BreakingChange(BreakingChangeKind.CONSTRUCTOR_REMOVED, new ConstructorBreakingChange(removedConstructor)));
                })
                .toList();
    }



    public List<List<FieldDeclaration>> getUnremovedFields(TypeDeclaration type1, TypeDeclaration type2) {
        List<FieldDeclaration> unremovedFields = type1.getFields().stream()
                .filter( field1 -> type2.getFields().stream()
                        .anyMatch(field2 -> field2.getName().equals(field1.getName()) ))
                //.peek(remainingField -> System.out.println("Field Left: " + remainingField.getName()))
                .toList();

        List<FieldDeclaration> parallelFieldsFrom2 = type2.getFields().stream()
                .filter(field2 -> unremovedFields.stream()
                        .anyMatch(field1 -> field1.getName().equals(field2.getName()) ))
                //.peek(parallelField -> System.out.println("Parallel Field from type2: " + parallelField.getName()))
                .toList();

        List<List<FieldDeclaration>> result = new ArrayList<>();
        result.add(unremovedFields);
        result.add(parallelFieldsFrom2);

        return result;
    }


    public List<List<MethodDeclaration>> getUnremovedMethods(TypeDeclaration type1, TypeDeclaration type2) {
        List<MethodDeclaration> unremovedMethods = type1.getMethods().stream()
                .filter(method1 -> type2.getMethods().stream()
                        .anyMatch(method2 -> method2.getSignature().getName().equals(method1.getSignature().getName()) &&
                                method2.getSignature().getParameterTypes().equals(method1.getSignature().getParameterTypes())))
                //.peek(remainingMethod -> System.out.println("Method Left: " + remainingMethod.getName()))
                .toList();

        List<MethodDeclaration> parallelMethodsFrom2 = type2.getMethods().stream()
                .filter(method2 -> unremovedMethods.stream()
                        .anyMatch(method1 -> method1.getSignature().getName().equals(method2.getSignature().getName()) &&
                                method1.getSignature().getParameterTypes().equals(method2.getSignature().getParameterTypes())))
                //.peek(parallelMethod -> System.out.println("Parallel Method from type2: " + parallelMethod.getName()))
                .toList();

        List<List<MethodDeclaration>> result = new ArrayList<>();
        result.add(unremovedMethods);
        result.add(parallelMethodsFrom2);

        return result;
    }



    public List<List<ConstructorDeclaration>> getUnremovedConstructors(TypeDeclaration type1, TypeDeclaration type2) {
        List<ConstructorDeclaration> unremovedConstructors = type1.getConstructors().stream()
                .filter(constructor1 -> type2.getConstructors().stream()
                        .anyMatch(constructor2 -> constructor2.getSignature().getParameterTypes().equals(constructor1.getSignature().getParameterTypes())))
                //.peek(remainingConstructor -> System.out.println("Constructor Left: " + remainingConstructor.getName()))
                .toList();

        List<ConstructorDeclaration> parallelConstructorsFrom2 = type2.getConstructors().stream()
                .filter(constructor2 -> unremovedConstructors.stream()
                        .anyMatch(constructor1 -> constructor1.getSignature().getParameterTypes().equals(constructor2.getSignature().getParameterTypes())))
                //.peek(parallelConstructor -> System.out.println("Parallel Constructor from type2: " + parallelConstructor.getName()))
                .toList();

        List<List<ConstructorDeclaration>> result = new ArrayList<>();
        result.add(unremovedConstructors);
        result.add(parallelConstructorsFrom2);

        return result;
    }

    public void fieldComparison(FieldDeclaration field1, FieldDeclaration field2) {
        if (!field1.getModifiers().contains(NonAccessModifiers.FINAL) && field2.getModifiers().contains(NonAccessModifiers.FINAL)) {
            breakingChanges.add(new BreakingChange(BreakingChangeKind.FIELD_NOW_FINAL, new FieldBreakingChange(field1)));
            System.out.println("Field v1 " + field1.getName() + " is not final, but Field v2 " + field2.getName() + " is final.");
        }

        if (!field1.getModifiers().contains(NonAccessModifiers.STATIC) && field2.getModifiers().contains(NonAccessModifiers.STATIC)) {
            breakingChanges.add(new BreakingChange(BreakingChangeKind.FIELD_NOW_STATIC, new FieldBreakingChange(field1)));
            System.out.println("Field v1 " + field1.getName() + " is not static, but Field v2 " + field2.getName() + " is static.");
        }

        if (field1.getModifiers().contains(NonAccessModifiers.STATIC) && !field2.getModifiers().contains(NonAccessModifiers.STATIC)) {
            breakingChanges.add(new BreakingChange(BreakingChangeKind.FIELD_NO_LONGER_STATIC, new FieldBreakingChange(field1)));
            System.out.println("Field v1 " + field1.getName() + " is static, but Field v2 " + field2.getName() + " is not.");
        }

        if (!field1.dataType.equals(field2.dataType)) {
            breakingChanges.add(new BreakingChange(BreakingChangeKind.FIELD_TYPE_CHANGED, new FieldBreakingChange(field1)));
            System.out.println("Return type of " + field1.getName() + " changed from " + field1.dataType + " to " + field2.dataType);
        }


        if (field1.getVisibility().equals(AccessModifier.PUBLIC) && field2.getVisibility().equals(AccessModifier.PROTECTED)) {
            breakingChanges.add(new BreakingChange(BreakingChangeKind.FIELD_LESS_ACCESSIBLE, new FieldBreakingChange(field1)));
            System.out.println("Field v1 " + field1.getName() + " is public, but Field v2 " + field2.getName() + " is protected.");
        }

    }



    public void methodComparison(MethodDeclaration method1, MethodDeclaration method2) {
        if (!method1.getModifiers().contains(NonAccessModifiers.FINAL) && method2.getModifiers().contains(NonAccessModifiers.FINAL)) {
            breakingChanges.add(new BreakingChange(BreakingChangeKind.METHOD_NOW_FINAL, new MethodBreakingChange(method1)));
            System.out.println("Method v1 " + method1.getName() + " is not final, but Method v2 " + method2.getName() + " is final.");
        }

        if (!method1.getModifiers().contains(NonAccessModifiers.STATIC) && method2.getModifiers().contains(NonAccessModifiers.STATIC)) {
            breakingChanges.add(new BreakingChange(BreakingChangeKind.METHOD_NOW_STATIC, new MethodBreakingChange(method1)));
            System.out.println("Method v1 " + method1.getName() + " is not static, but Method v2 " + method2.getName() + " is static.");
        }

        if (method1.getModifiers().contains(NonAccessModifiers.STATIC) && !method2.getModifiers().contains(NonAccessModifiers.STATIC)) {
            breakingChanges.add(new BreakingChange(BreakingChangeKind.METHOD_NO_LONGER_STATIC, new MethodBreakingChange(method1)));
            System.out.println("Method v1 " + method1.getName() + " is static, but Method v2 " + method2.getName() + " is not.");
        }

        if (!method1.getModifiers().contains(NonAccessModifiers.ABSTRACT) && method2.getModifiers().contains(NonAccessModifiers.ABSTRACT)) {
            breakingChanges.add(new BreakingChange(BreakingChangeKind.METHOD_NOW_ABSTRACT, new MethodBreakingChange(method1)));
            System.out.println("Method v1 " + method1.getName() + " is not abstract, but Method v2 " + method2.getName() + " is abstract.");
        }

        if (method1.getModifiers().contains(NonAccessModifiers.ABSTRACT) && !method2.getModifiers().contains(NonAccessModifiers.ABSTRACT)) {
            breakingChanges.add(new BreakingChange(BreakingChangeKind.METHOD_ABSTRACT_NOW_DEFAULT, new MethodBreakingChange(method1)));
            System.out.println("Method v1 " + method1.getName() + " is abstract, but Method v2 " + method2.getName() + " is not.");
        }

        if (method1.getVisibility().equals(AccessModifier.PUBLIC) && method2.getVisibility().equals(AccessModifier.PROTECTED)) {
            breakingChanges.add(new BreakingChange(BreakingChangeKind.METHOD_LESS_ACCESSIBLE, new MethodBreakingChange(method1)));
            System.out.println("Method v1 " + method1.getName() + " is public, but Method v2 " + method2.getName() + " is protected.");
        }

        if (!method1.returnType.equals(method2.returnType)) {
            breakingChanges.add(new BreakingChange(BreakingChangeKind.METHOD_RETURN_TYPE_CHANGED, new MethodBreakingChange(method1)));
            System.out.println("Return type of " + method1.getName() + " changed from " + method1.returnType + " to " + method2.returnType);
        }

        List<String> exceptions1 = method1.getExceptions();
        List<String> exceptions2 = method2.getExceptions();

        List<String> additionalExceptions1 = exceptions1.stream()
                .filter(e -> !exceptions2.contains(e))
                .toList();

        List<String> additionalExceptions2 = exceptions2.stream()
                .filter(e -> !exceptions1.contains(e))
                .toList();


        if (!additionalExceptions1.isEmpty()) {
            breakingChanges.add(new BreakingChange(BreakingChangeKind.METHOD_NO_LONGER_THROWS_CHECKED_EXCEPTION, new MethodBreakingChange(method1)));
            System.out.println("Method v1 " + method1.getName() + " has more exceptions: " + additionalExceptions1);
        }

        if (!additionalExceptions2.isEmpty()) {
            breakingChanges.add(new BreakingChange(BreakingChangeKind.METHOD_NOW_THROWS_CHECKED_EXCEPTION, new MethodBreakingChange(method1)));
            System.out.println("Method v2 " + method2.getName() + " has more exceptions: " + additionalExceptions2);
        }



    }

    public void constructorComparison(ConstructorDeclaration constructor1, ConstructorDeclaration constructor2) {
        if (constructor1.getVisibility().equals(AccessModifier.PUBLIC) && constructor2.getVisibility().equals(AccessModifier.PROTECTED)) {
            breakingChanges.add(new BreakingChange(BreakingChangeKind.CONSTRUCTOR_LESS_ACCESSIBLE, new ConstructorBreakingChange(constructor1)));
            System.out.println("Constructor v1 " + constructor1.getName() + " is public, but Constructor v2 " + constructor2.getName() + " is protected.");
        }

    }


    public void typeComparison(TypeDeclaration type1, TypeDeclaration type2) {
        if (!type1.getModifiers().contains(NonAccessModifiers.FINAL) && type2.getModifiers().contains(NonAccessModifiers.FINAL)) {
            breakingChanges.add(new BreakingChange(BreakingChangeKind.TYPE_NOW_FINAL, new TypeBreakingChange(type1)));
            System.out.println("Type v1 " + type1.getName() + " is not final, but Type v2 " + type2.getName() + " is final.");
        }

        if (!type1.getModifiers().contains(NonAccessModifiers.ABSTRACT) && type2.getModifiers().contains(NonAccessModifiers.ABSTRACT)) {
            breakingChanges.add(new BreakingChange(BreakingChangeKind.TYPE_NOW_ABSTRACT, new TypeBreakingChange(type1)));
            System.out.println("Type v1 " + type1.getName() + " is not abstract, but Type v2 " + type2.getName() + " is abstract.");
        }

        if (type1.getVisibility().equals(AccessModifier.PUBLIC) && type2.getVisibility().equals(AccessModifier.PROTECTED)) {
            breakingChanges.add(new BreakingChange(BreakingChangeKind.TYPE_LESS_ACCESSIBLE, new TypeBreakingChange(type1)));
            System.out.println("Type v1 " + type1.getName() + " is public, but Type v2 " + type2.getName() + " is protected.");
        }

        if (!type1.typeType.equals(type2.typeType)) {
            breakingChanges.add(new BreakingChange(BreakingChangeKind.TYPE_TYPE_CHANGED, new TypeBreakingChange(type1)));
            System.out.println("Type of " + type1.getName() + " changed from " + type1.typeType + " to " + type2.typeType);
        }

    }

    public void diffTesting() {
        List<TypeDeclaration> removedTypes = checkingForRemovedTypes();
        List<List<TypeDeclaration>> commonTypes = getUnremovedTypes();
        List<TypeDeclaration> commonTypesInV1 = commonTypes.get(0);
        List<TypeDeclaration> commonTypesInV2 = commonTypes.get(1);

        IntStream.range(0, commonTypesInV1.size())
                .forEach(i -> {
                    typeComparison(commonTypesInV1.get(i), commonTypesInV2.get(i));

                    List<FieldDeclaration> removedFields = checkingForRemovedFields(commonTypesInV1.get(i), commonTypesInV2.get(i));
                    List<MethodDeclaration> removedMethods = checkingForRemovedMethods(commonTypesInV1.get(i), commonTypesInV2.get(i));
                    List<ConstructorDeclaration> removedConstructors = checkingForRemovedConstructors(commonTypesInV1.get(i), commonTypesInV2.get(i));

                    List<List<MethodDeclaration>> remainingMethods = getUnremovedMethods(commonTypesInV1.get(i), commonTypesInV2.get(i));
                    List<List<FieldDeclaration>> remainingFields = getUnremovedFields(commonTypesInV1.get(i), commonTypesInV2.get(i));
                    List<List<ConstructorDeclaration>> remainingConstructors = getUnremovedConstructors(commonTypes.get(0).get(i), commonTypes.get(1).get(i));

                    IntStream.range(0, remainingMethods.get(0).size())
                            .forEach(j -> {
                                methodComparison(remainingMethods.get(0).get(j), remainingMethods.get(1).get(j));
                            });

                    IntStream.range(0, remainingConstructors.get(0).size())
                            .forEach(j -> {
                                constructorComparison(remainingConstructors.get(0).get(j), remainingConstructors.get(1).get(j));
                            });

                    IntStream.range(0, remainingFields.get(0).size())
                            .forEach(j -> {
                                fieldComparison(remainingFields.get(0).get(j), remainingFields.get(1).get(j));
                            });


                });
    }



    public void diffPrinting() {
        for (BreakingChange breakingChange : breakingChanges) {
            System.out.println("Breaking change kind: " + breakingChange.getBreakingChangeKind());
            System.out.println("In : " + breakingChange.getBreakingChangeElement().getElement());
            System.out.println("---------------------------------");
        }
    }








    public void trying(){

        System.out.println(" I'm tryyyinnnng !!! ");


    }


}
