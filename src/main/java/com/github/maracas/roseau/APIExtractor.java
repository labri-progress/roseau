package com.github.maracas.roseau;


/** import com.github.maracas.roseau.model.API;**/
import com.github.maracas.roseau.model.ConstructorDeclaration;
import com.github.maracas.roseau.model.FieldDeclaration;
import com.github.maracas.roseau.model.MethodDeclaration;
import com.github.maracas.roseau.model.TypeDeclaration;
import com.github.maracas.roseau.model.TypeType;
import com.github.maracas.roseau.model.AccessModifier;

import spoon.reflect.CtModel;

import spoon.reflect.declaration.*;
import spoon.reflect.declaration.ModifierKind;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class APIExtractor {
	private final CtModel model;

	public APIExtractor(CtModel model) {
		this.model = Objects.requireNonNull(model);
	}

	//Returning the packages as CtPackages
	public List<CtPackage> RawSpoonPackages() {

		return model.getAllPackages().stream()

				.peek(packageDeclaration -> {
					//System.out.println("Package: " + packageDeclaration.getSimpleName());
				})
				.toList();
	}


	//Returning the accessible types of a package as CtTypes
	public List<CtType<?>> RawSpoonTypes(CtPackage pkg) {

		return pkg.getTypes().stream()
				.filter(types -> types.getVisibility()== ModifierKind.PUBLIC) //YES, don't worry I will change this

				.peek(types -> {
					//System.out.println("Type: " + types.getSimpleName());
				})
				.toList();
	}


	//Returning the accessible fields of a type as CtFields
	public List<CtField<?>> RawSpoonFields(CtType<?> type) {
		return type.getFields().stream()
				.filter(field -> field.getVisibility()== ModifierKind.PUBLIC)  //YES, don't worry I will change this

				.peek(field -> {
					//System.out.println("Field: " + field.getType().getSimpleName());
				})
				.toList();
	}

    //Returning the accessible methods of a type as CtMethods
	public List<CtMethod<?>> RawSpoonMethods(CtType<?> type) {
		return type.getMethods().stream()
				.filter(method -> method.getVisibility()== ModifierKind.PUBLIC)  //YES, don't worry I will change this
				.peek(method -> {
					//System.out.println("Method: " + method.getSimpleName());
				})
				.toList();
	}

	//Returning the accessible constructors of a type as CtConstructors
	public List<CtConstructor<?>> RawSpoonConstructors(CtType<?> type) {
		if (type instanceof CtClass<?> cls) {
			return new ArrayList<>(cls.getConstructors().stream()
					.filter(constructor -> constructor.getVisibility()== ModifierKind.PUBLIC)  //YES, don't worry I will change this
					.peek(constructor -> {
						//System.out.println("Constructor: " + constructor.getSimpleName());
					})
					.toList());
		}
		return Collections.emptyList();

	}

	// Converting spoon's ModifierKind to our enum: AccessModifier
	private AccessModifier convertVisibility(ModifierKind visibility) {
		if (visibility == ModifierKind.PUBLIC) {
			return AccessModifier.PUBLIC;
		} else if (visibility == ModifierKind.PRIVATE) {
			return AccessModifier.PRIVATE;
		} else if (visibility == ModifierKind.PROTECTED) {
			return AccessModifier.PROTECTED;
		} else {
			return AccessModifier.DEFAULT;
		}
	}

	// Returning the types' types, whether if it's a class or an enum or whatever
	public TypeType convertTypeType(CtType<?> type) {
		if (type.isClass())
			return TypeType.CLASS;
		if (type.isInterface())
			return TypeType.INTERFACE;
		if (type.isEnum())
			return TypeType.ENUM;
		if (type.isAnnotationType())
			return TypeType.ANNOTATION;
		else
			return TypeType.RECORD;

	}


	// The conversion functions : Moving from spoon's Ct kinds to our Declaration kinds
	public List<TypeDeclaration> RawTypesConversion(List<CtType<?>> spoonTypes) {
		return spoonTypes.stream()
				.map(spoonType -> {
					String name = spoonType.getSimpleName();
					AccessModifier visibility = convertVisibility(spoonType.getVisibility());
					TypeType typeType = convertTypeType(spoonType);
					return new TypeDeclaration(name, visibility, typeType);
				})
				.peek(typeDeclaration -> {
					/** System.out.println("Type name in conversion function: " + typeDeclaration.name);
					System.out.println("Type visibility in conversion function: " + typeDeclaration.visibility);
					System.out.println("Type type in conversion function: " + typeDeclaration.typeType); **/
				})
				.toList();
	}


	public List<FieldDeclaration> RawFieldsConversion(List<CtField<?>> spoonFields) {
		return spoonFields.stream()
				.map(spoonField -> {
					String name = spoonField.getSimpleName();
					AccessModifier visibility = convertVisibility(spoonField.getVisibility());
					String dataType = spoonField.getType().getSimpleName();
					return new FieldDeclaration(name, visibility, dataType);
				})
				.peek(fieldDeclaration -> {
					/** System.out.println("Field name in conversion function: " + fieldDeclaration.name);
					System.out.println("Field visibility in conversion function: " + fieldDeclaration.visibility);
					System.out.println("Field datatype in conversion function: " + fieldDeclaration.dataType); **/
				})
				.toList();
	}

	public List<MethodDeclaration> RawMethodsConversion(List<CtMethod<?>> spoonMethods) {
		return spoonMethods.stream()
				.map(spoonMethod -> {
					String name = spoonMethod.getSimpleName();
					AccessModifier visibility = convertVisibility(spoonMethod.getVisibility());
					String returnType = spoonMethod.getType().getSimpleName();
					List<String> parametersTypes = spoonMethod.getParameters().stream()
							.map(parameterType -> parameterType.getType().getSimpleName())
							.toList();

					return new MethodDeclaration(name, visibility, returnType, parametersTypes);
				})
				.peek(methodDeclaration -> {
					/** System.out.println("Method name in conversion function: " + methodDeclaration.name);
					System.out.println("Method visibility in conversion function: " + methodDeclaration.visibility);
					System.out.println("Method datatype in conversion function: " + methodDeclaration.returnType);
					System.out.println("Method parameters types in conversion function: " + methodDeclaration.parametersTypes); **/
				})
				.toList();
	}

	public List<ConstructorDeclaration> RawConstructorsConversion(List<CtConstructor<?>> spoonConstructors) {
		return spoonConstructors.stream()
				.map(spoonConstructor -> {
					String name = spoonConstructor.getSimpleName();
					AccessModifier visibility = convertVisibility(spoonConstructor.getVisibility());
					String returnType = spoonConstructor.getType().getSimpleName();
					List<String> parametersTypes = spoonConstructor.getParameters().stream()
							.map(parameterType -> parameterType.getType().getSimpleName())
							.toList();
					return new ConstructorDeclaration(name, visibility, returnType,parametersTypes);
				})
				.peek(constructorDeclaration -> {
					/**System.out.println("Constructor name in conversion function: " + constructorDeclaration.name);
					System.out.println("Constructor visibility types in conversion function: " + constructorDeclaration.visibility);
					System.out.println("Constructor returnType in conversion function: " + constructorDeclaration.returnType);
					System.out.println("Constructor parameters types in conversion function: " + constructorDeclaration.parametersTypes); **/
				})
				.toList();
	}

	public List<TypeDeclaration> dataProcessing(APIExtractor extractor) {
		List<CtPackage> packages = extractor.RawSpoonPackages(); // Returning packages
		List<TypeDeclaration> convertedTypes = new ArrayList<>();

		if (!packages.isEmpty()) {
			List<CtType<?>> types = extractor.RawSpoonTypes(packages.get(0)); // Only returning the unnamed package's public types
			List<TypeDeclaration> typesConverted = extractor.RawTypesConversion(types); // Transforming the CtTypes into TypeDeclarations

			if (!typesConverted.isEmpty()) {
				typesConverted.forEach(typeDeclaration -> {
					List<CtField<?>> fields = new ArrayList<>();
					List<CtMethod<?>> methods = new ArrayList<>();
					List<CtConstructor<?>> constructors = new ArrayList<>();

					types.forEach(type -> fields.addAll(extractor.RawSpoonFields(type))); // Returning the public fields of public types, still didn't handle the protected case, don't worry I will
					List<FieldDeclaration> fieldsConverted = extractor.RawFieldsConversion(fields); // Transforming them into fieldDeclarations
					typeDeclaration.setFields(fieldsConverted);

					// Doing the same thing for methods and constructors
					types.forEach(type -> methods.addAll(extractor.RawSpoonMethods(type)));
					List<MethodDeclaration> methodsConverted = extractor.RawMethodsConversion(methods);
					typeDeclaration.setMethods(methodsConverted);

					types.forEach(type -> constructors.addAll(extractor.RawSpoonConstructors(type)));
					List<ConstructorDeclaration> constructorsConverted = extractor.RawConstructorsConversion(constructors);
					typeDeclaration.setConstructors(constructorsConverted);

					convertedTypes.add(typeDeclaration);
				});
			}
		}

		return convertedTypes;
	}










}
