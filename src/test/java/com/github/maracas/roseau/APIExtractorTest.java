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
import spoon.reflect.declaration.*;

import java.util.ArrayList;
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

		List<CtPackage> result = extractor.RawSpoonPackages(); //Returning packages

		if (!result.isEmpty()) {

			List<CtType<?>>  types = extractor.RawSpoonTypes(result.get(0)); //Only returning the unnamed package's public types
			List<TypeDeclaration> typesConverted = extractor.RawTypesConversion(types); //Transforming the CtTypes into TypeDeclarations

			if (!types.isEmpty()) {
				List<CtField<?>> fields = new ArrayList<>();
				List<CtMethod<?>> methods = new ArrayList<>();
				List<CtConstructor<?>> constructors = new ArrayList<>();

				types.forEach(type -> fields.addAll(extractor.RawSpoonFields(type)));  //Returning the public fields of public types, still didn't handle the protected case, don't worry I will
				List<FieldDeclaration> fieldsConverted = extractor.RawFieldsConversion(fields); //Transforming them into fieldDeclarations
				// Doing the same thing for methods and constructors
				types.forEach(type -> methods.addAll(extractor.RawSpoonMethods(type)));
				List<MethodDeclaration> methodsConverted = extractor.RawMethodsConversion(methods);
				types.forEach(type -> constructors.addAll(extractor.RawSpoonConstructors(type)));
				List<ConstructorDeclaration> constructorsConverted = extractor.RawConstructorsConversion(constructors);
			}
		}








	}


}