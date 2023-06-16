package com.github.maracas.roseau;


/** import com.github.maracas.roseau.model.API;**/
import com.github.maracas.roseau.model.ConstructorDeclaration;
import com.github.maracas.roseau.model.FieldDeclaration;
import com.github.maracas.roseau.model.MethodDeclaration;
import com.github.maracas.roseau.model.TypeDeclaration;
import com.github.maracas.roseau.model.TypeType;
import com.github.maracas.roseau.model.AccessModifier;
import com.github.maracas.roseau.model.NonAccessModifiers;
import com.github.maracas.roseau.model.Signature;

import spoon.reflect.CtModel;

import spoon.reflect.declaration.*;
import spoon.reflect.declaration.ModifierKind;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;



public class APIExtractor {
	private final CtModel model;

	public APIExtractor(CtModel model) {
		this.model = Objects.requireNonNull(model);
	}

	//Returning the packages as CtPackages
	public List<CtPackage> RawSpoonPackages() {

		return model.getAllPackages().stream()

				.peek(packageDeclaration -> {
					//System.out.println("Package: " + packageDeclaration.getQualifiedName());
				})
				.toList();
	}


	//Returning the accessible types of a package as CtTypes

	public List<CtType<?>> RawSpoonTypes(CtPackage pkg) {
		List<CtType<?>> types = new ArrayList<>();
		pkg.getTypes().stream()
				.filter(type -> type.getVisibility() == ModifierKind.PUBLIC)
				.forEach(type -> {
					// System.out.println("Type: " + type.getQualifiedName());
					// System.out.println("Type: " + type.getModifiers());
					types.add(type);
					extractingNestedTypes(type, types);

				});
		return types;
	}

	// Handing nested types
	public void extractingNestedTypes(CtType<?> parentType, List<CtType<?>> types) {
		parentType.getNestedTypes().stream()
				.filter(type -> type.getVisibility() == ModifierKind.PUBLIC)
				.forEach(type -> {
					//System.out.println("Type: " + type.getQualifiedName());
					//System.out.println("Type: " + type.getModifiers());
					types.add(type);
					extractingNestedTypes(type, types);
				});
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
	public AccessModifier convertVisibility(ModifierKind visibility) {
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


	public NonAccessModifiers ConvertNonAccessModifier(ModifierKind modifier) {
		if (modifier == ModifierKind.STATIC) {
			return NonAccessModifiers.STATIC;
		} else if (modifier == ModifierKind.FINAL) {
			return NonAccessModifiers.FINAL;
		} else if (modifier == ModifierKind.ABSTRACT) {
			return NonAccessModifiers.ABSTRACT;
		} else if (modifier == ModifierKind.SYNCHRONIZED) {
			return NonAccessModifiers.SYNCHRONIZED;
		} else if (modifier == ModifierKind.VOLATILE) {
			return NonAccessModifiers.VOLATILE;
		} else if (modifier == ModifierKind.TRANSIENT) {
			return NonAccessModifiers.TRANSIENT;
		} else if (modifier == ModifierKind.SEALED) {
			return NonAccessModifiers.SEALED;
		} else if (modifier == ModifierKind.NON_SEALED) {
			return NonAccessModifiers.NON_SEALED;
		} else if (modifier == ModifierKind.NATIVE) {
			return NonAccessModifiers.NATIVE;
		} else {
			return NonAccessModifiers.STRICTFP;
		}
	}

	public List<NonAccessModifiers> filterNonAccessModifiers(Set<ModifierKind> modifiers) {
		List<NonAccessModifiers> nonAccessModifiers = new ArrayList<>();

		for (ModifierKind modifier : modifiers) {
			if (modifier != ModifierKind.PUBLIC && modifier != ModifierKind.PRIVATE
					&& modifier != ModifierKind.PROTECTED ) {
				nonAccessModifiers.add(ConvertNonAccessModifier(modifier));
			}
		}

		return nonAccessModifiers;
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
					String name = spoonType.getQualifiedName();
					AccessModifier visibility = convertVisibility(spoonType.getVisibility());
					TypeType typeType = convertTypeType(spoonType);
					List<NonAccessModifiers> modifiers = filterNonAccessModifiers(spoonType.getModifiers());
					return new TypeDeclaration(name, visibility, typeType, modifiers);
				})

				.toList();
	}


	public List<FieldDeclaration> RawFieldsConversion(List<CtField<?>> spoonFields) {
		return spoonFields.stream()
				.map(spoonField -> {
					String name = spoonField.getSimpleName();
					AccessModifier visibility = convertVisibility(spoonField.getVisibility());
					String dataType = spoonField.getType().getSimpleName();
					List<NonAccessModifiers> modifiers = filterNonAccessModifiers(spoonField.getModifiers());
					return new FieldDeclaration(name, visibility, dataType,modifiers);
				})

				.toList();
	}

	public List<MethodDeclaration> RawMethodsConversion(List<CtMethod<?>> spoonMethods) {
		return spoonMethods.stream()
				.map(spoonMethod -> {
					String name = spoonMethod.getSimpleName();
					AccessModifier visibility = convertVisibility(spoonMethod.getVisibility());
					String returnType = spoonMethod.getType().getSimpleName();
					List<NonAccessModifiers> modifiers = filterNonAccessModifiers(spoonMethod.getModifiers());
					List<String> parametersTypes = spoonMethod.getParameters().stream()
							.map(parameterType -> parameterType.getType().getSimpleName())
							.toList();
					Signature signature = new Signature(name, parametersTypes);
					return new MethodDeclaration(name, visibility, returnType, parametersTypes,modifiers, signature);
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
					List<NonAccessModifiers> modifiers = filterNonAccessModifiers(spoonConstructor.getModifiers());
					Signature signature = new Signature(name, parametersTypes);
					return new ConstructorDeclaration(name, visibility, returnType,parametersTypes,modifiers,signature);
				})

				.toList();
	}

	public List<TypeDeclaration> dataProcessing(APIExtractor extractor) {
		List<CtPackage> packages = extractor.RawSpoonPackages(); // Returning packages
		List<TypeDeclaration> typesConverted = new ArrayList<>();

		if (!packages.isEmpty()) {
			List<CtType<?>> types = extractor.RawSpoonTypes(packages.get(0)); // Only returning the unnamed package's public types
			typesConverted = extractor.RawTypesConversion(types); // Transforming the CtTypes into TypeDeclarations

			if (!typesConverted.isEmpty()) {
				int i=0;
				for (CtType<?> type : types) {
					TypeDeclaration typeDeclaration = typesConverted.get(i);

					List<CtField<?>> fields = extractor.RawSpoonFields(type); // Returning the public fields of public types, still didn't handle the protected case, don't worry I will
					List<FieldDeclaration> fieldsConverted = extractor.RawFieldsConversion(fields); // Transforming them into fieldDeclarations
					typeDeclaration.setFields(fieldsConverted);

					// Doing the same thing for methods and constructors

					List<CtMethod<?>> methods = extractor.RawSpoonMethods(type);
					List<MethodDeclaration> methodsConverted = extractor.RawMethodsConversion(methods);
					typeDeclaration.setMethods(methodsConverted);

					List<CtConstructor<?>> constructors = extractor.RawSpoonConstructors(type);
					List<ConstructorDeclaration> constructorsConverted = extractor.RawConstructorsConversion(constructors);
					typeDeclaration.setConstructors(constructorsConverted);

					i++;
				};
			}

		}
		return typesConverted;

	}

}
