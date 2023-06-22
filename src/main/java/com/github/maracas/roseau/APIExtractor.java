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
import com.github.maracas.roseau.model.API;

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
	public List<CtPackage> rawSpoonPackages() {
		return model.getAllPackages().stream()
				.peek(packageDeclaration -> {
					//System.out.println("Package: " + packageDeclaration.getQualifiedName());
				})
				.toList();
	}


	private boolean typeIsAccessible(CtType<?> type) {
		if (type.getVisibility() == ModifierKind.PUBLIC) {
			return true;
		} else if (type.getVisibility() == ModifierKind.PROTECTED) {
			return !type.isFinal() && !type.getModifiers().contains(ModifierKind.SEALED);
		} else {
			return false;
		}
	}

	private boolean memberIsAccessible(CtModifiable member) {
		return member.isPublic() || member.isProtected();
	}

	//Returning the accessible types of a package as CtTypes

	public List<CtType<?>> rawSpoonTypes(CtPackage pkg) {
		List<CtType<?>> types = new ArrayList<>();
		pkg.getTypes().stream()
				.filter(this::typeIsAccessible)
				.forEach(type -> {
					// System.out.println("Type: " + type.getQualifiedName());
					// System.out.println("Type: " + type.getModifiers());
					types.add(type);
					extractingNestedTypes(type);
				});
		return types;
	}

	// Handing nested types
	public List<CtType<?>> extractingNestedTypes(CtType<?> parentType) {
		List<CtType<?>> types = new ArrayList<>();
		parentType.getNestedTypes().stream()
				.filter(this::typeIsAccessible)
				.forEach(type -> {
					//System.out.println("Type: " + type.getQualifiedName());
					//System.out.println("Type: " + type.getModifiers());
					types.add(type);
					types.addAll(extractingNestedTypes(type));
				});
		return types;
	}
	//Returning the accessible fields of a type as CtFields
	public List<CtField<?>> rawSpoonFields(CtType<?> type) {
		return type.getFields().stream()
				.filter(this::memberIsAccessible)
				.peek(field -> {
					//System.out.println("Field: " + field.getType().getSimpleName());
				})
				.toList();
	}

    //Returning the accessible methods of a type as CtMethods
	public List<CtMethod<?>> rawSpoonMethods(CtType<?> type) {
		return type.getMethods().stream()
				.filter(this::memberIsAccessible)
				.peek(method -> {
					//System.out.println("Method: " + method.getSimpleName());
				})
				.toList();
	}

	//Returning the accessible constructors of a type as CtConstructors
	public List<CtConstructor<?>> rawSpoonConstructors(CtType<?> type) {
		if (type instanceof CtClass<?> cls) {
			return new ArrayList<>(cls.getConstructors().stream()
					.filter(this::memberIsAccessible)
					.peek(constructor -> {
						//System.out.println("Constructor: " + constructor.getSimpleName());
					})
					.toList());
		}
		return Collections.emptyList();

	}



	// Converting spoon's access ModifierKind to our enum: AccessModifier
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

	// Converting spoon's Non-access ModifierKind to our enum: NonAccessModifier
	public NonAccessModifiers convertNonAccessModifier(ModifierKind modifier) {
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

	// Filtering access modifiers because the convertVisibility() handles them already
	public List<NonAccessModifiers> filterNonAccessModifiers(Set<ModifierKind> modifiers) {
		List<NonAccessModifiers> nonAccessModifiers = new ArrayList<>();

		for (ModifierKind modifier : modifiers) {
			if (modifier != ModifierKind.PUBLIC && modifier != ModifierKind.PRIVATE
					&& modifier != ModifierKind.PROTECTED ) {
				nonAccessModifiers.add(convertNonAccessModifier(modifier));
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


	/** The conversion functions : Moving from spoon's Ct kinds to our Declaration kinds **/

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
					String dataType = spoonField.getType().getQualifiedName();
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
					String returnType = spoonMethod.getType().getQualifiedName();
					List<NonAccessModifiers> modifiers = filterNonAccessModifiers(spoonMethod.getModifiers());
					List<String> parametersTypes = spoonMethod.getParameters().stream()
							.map(parameterType -> parameterType.getType().getQualifiedName())
							.toList();
					Signature signature = new Signature(name, parametersTypes);
					List<String> exceptions = spoonMethod.getThrownTypes().stream()
							.map(exception-> exception.getQualifiedName())
							.toList();
					return new MethodDeclaration(name, visibility, returnType, parametersTypes,modifiers, signature, exceptions);
				})

				.toList();
	}

	public List<ConstructorDeclaration> RawConstructorsConversion(List<CtConstructor<?>> spoonConstructors) {
		return spoonConstructors.stream()
				.map(spoonConstructor -> {
					String name = spoonConstructor.getSimpleName();
					AccessModifier visibility = convertVisibility(spoonConstructor.getVisibility());
					String returnType = spoonConstructor.getType().getQualifiedName();
					List<String> parametersTypes = spoonConstructor.getParameters().stream()
							.map(parameterType -> parameterType.getType().getQualifiedName())
							.toList();
					List<NonAccessModifiers> modifiers = filterNonAccessModifiers(spoonConstructor.getModifiers());
					Signature signature = new Signature(name, parametersTypes);
					List<String> exceptions = spoonConstructor.getThrownTypes().stream()
							.map(exception-> exception.getQualifiedName())
							.toList();
					return new ConstructorDeclaration(name, visibility, returnType,parametersTypes,modifiers,signature, exceptions);
				})

				.toList();
	}

	// Processing data and structuring the API using the previous functions

	public API dataProcessing(APIExtractor extractor) {
		List<CtPackage> packages = extractor.rawSpoonPackages(); // Returning packages
		List<TypeDeclaration> AllTheTypes = new ArrayList<>();

		for (CtPackage pkg : packages) {

			List<CtType<?>> types = extractor.rawSpoonTypes(pkg); // Only returning the packages' accessible types
			List<TypeDeclaration> typesConverted = extractor.RawTypesConversion(types); // Transforming the CtTypes into TypeDeclarations

			if (!typesConverted.isEmpty()) {
				int i=0;
				for (CtType<?> type : types) {
					TypeDeclaration typeDeclaration = typesConverted.get(i);

					List<CtField<?>> fields = extractor.rawSpoonFields(type); // Returning the accessible fields of accessible types
					List<FieldDeclaration> fieldsConverted = extractor.RawFieldsConversion(fields); // Transforming them into fieldDeclarations
					typeDeclaration.setFields(fieldsConverted);

					// Doing the same thing for methods and constructors

					List<CtMethod<?>> methods = extractor.rawSpoonMethods(type);
					List<MethodDeclaration> methodsConverted = extractor.RawMethodsConversion(methods);
					typeDeclaration.setMethods(methodsConverted);

					List<CtConstructor<?>> constructors = extractor.rawSpoonConstructors(type);
					List<ConstructorDeclaration> constructorsConverted = extractor.RawConstructorsConversion(constructors);
					typeDeclaration.setConstructors(constructorsConverted);

					i++;
				};
			}

			AllTheTypes.addAll(typesConverted);

		}

		API api = new API(AllTheTypes);


		return api;

	}

	// A method for printing the API
	public void printingData(API api) {

		List<TypeDeclaration> convertedTypes = api.getAllTheTypes();
		for (TypeDeclaration typeDeclaration : convertedTypes) {
			System.out.println("Type name: " + typeDeclaration.getName());
			System.out.println("Visibility: " + typeDeclaration.getVisibility());
			System.out.println("Type's Type: " + typeDeclaration.getTypeType());
			System.out.println("Type's Modifiers: " + typeDeclaration.getModifiers());
			System.out.println("");
			List<FieldDeclaration> fields = typeDeclaration.getFields();
			if (fields != null) {
				System.out.println("Fields: ");
				for (FieldDeclaration field : fields) {
					System.out.println("    Name: " + field.getName());
					System.out.println("    Visibility: " + field.getVisibility());
					System.out.println("    Data type: " + field.getDataType());
					System.out.println("    Modifiers: " + field.getModifiers());
					System.out.println("");
				}
			}

			List<MethodDeclaration> methods = typeDeclaration.getMethods();
			if (methods != null) {
				System.out.println("Methods:");
				for (MethodDeclaration method : methods) {
					System.out.println("    Name: " + method.getName());
					System.out.println("    Visibility: " + method.getVisibility());
					System.out.println("    Return Type: " + method.getReturnType());
					System.out.println("    Modifiers: " + method.getModifiers());
					System.out.println("    Parameters: " + method.getParametersTypes());
					System.out.println("    Signature: " + method.getSignature().getName() +  "  &  " + method.getSignature().getParameterTypes() );
					System.out.println("    Exceptions: " + method.getExceptions());
					System.out.println("");
				}
			}

			List<ConstructorDeclaration> constructors = typeDeclaration.getConstructors();
			if (constructors != null) {
				System.out.println("Constructors:");
				for (ConstructorDeclaration constructor : constructors) {
					System.out.println("    Name: " + constructor.getName());
					System.out.println("    Visibility: " + constructor.getVisibility());
					System.out.println("    Return Type: " + constructor.getReturnType());
					System.out.println("    Modifiers: " + constructor.getModifiers());
					System.out.println("    Parameters: " + constructor.getParametersTypes());
					System.out.println("    Signature: " + constructor.getSignature().getName() +  "  &  " +constructor.getSignature().getParameterTypes());
					System.out.println("    Exceptions: " + constructor.getExceptions());
					System.out.println("");

				}
			}

			System.out.println("\n  =====  NEEEEEEEEXT  =====\n\n");
		}

	}





}
