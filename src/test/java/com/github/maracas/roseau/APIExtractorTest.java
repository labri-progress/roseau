package com.github.maracas.roseau;

import com.github.maracas.roseau.model.TypeDeclaration;
import com.github.maracas.roseau.model.FieldDeclaration;
import com.github.maracas.roseau.model.MethodDeclaration;
import com.github.maracas.roseau.model.ConstructorDeclaration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spoon.Launcher;
import spoon.MavenLauncher;
import spoon.reflect.CtModel;
import java.util.List;
import java.nio.file.Path;




class APIExtractorTest {
	APIExtractor extractor;
	CtModel model;


	@BeforeEach
	void setUp() {
		Path sources = Path.of("src/test/resources/api-extractor-tests/without-modules/v1");
		Launcher launcher = new MavenLauncher(sources.toString(), MavenLauncher.SOURCE_TYPE.APP_SOURCE, new String[0]);
		launcher.getEnvironment().setNoClasspath(true);
		model = launcher.buildModel();
		extractor = new APIExtractor(model);

	}

	@Test
	void write_some_interesting_tests_later() {
		//Extracting data and processing it
		List<TypeDeclaration> convertedTypes = extractor.dataProcessing(extractor);

		//printing the API for each type
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
					System.out.println("");

				}
			}

			System.out.println("\n  =====  NEEEEEEEEXT  =====\n\n");
		}
	}


}