package com.github.maracas.roseau;

import com.github.maracas.roseau.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spoon.Launcher;
import spoon.MavenLauncher;

import java.nio.file.Path;
import java.util.List;

class APIDiffTest {

    APIDiff diff;


    @BeforeEach
    void setUp() {

        Path v1 = Path.of("src/test/resources/api-extractor-tests/without-modules/v1");
        Launcher launcher1 = new MavenLauncher(v1.toString(), MavenLauncher.SOURCE_TYPE.APP_SOURCE, new String[0]);
        launcher1.getEnvironment().setNoClasspath(true);
        APIExtractor extractor1 = new APIExtractor(launcher1.buildModel());

        Path v2 = Path.of("src/test/resources/api-extractor-tests/without-modules/v2");
        Launcher launcher2 = new MavenLauncher(v2.toString(), MavenLauncher.SOURCE_TYPE.APP_SOURCE, new String[0]);
        launcher2.getEnvironment().setNoClasspath(true);
        APIExtractor  extractor2 = new APIExtractor(launcher2.buildModel());

        diff = new APIDiff(extractor1.dataProcessing(extractor1), extractor2.dataProcessing(extractor2));
    }


    @Test
    void BreakingChangesTesting() {

        List<List<TypeDeclaration>> futarinodiffu = diff.getUnremovedTypes();

        List<TypeDeclaration> unremovedTypes1 = futarinodiffu.get(0);
        List<TypeDeclaration> typesInParallelFrom2 = futarinodiffu.get(1);

        int size = unremovedTypes1.size();

        for (int i = 0; i < size; i++) {


            diff.typeComparison(futarinodiffu.get(0).get(i), futarinodiffu.get(1).get(i));

            List<FieldDeclaration> removedFields = diff.CheckingForRemovedFields(futarinodiffu.get(0).get(i), futarinodiffu.get(1).get(i));
            List<MethodDeclaration> removedMethods = diff.CheckingForRemovedMethods(futarinodiffu.get(0).get(i), futarinodiffu.get(1).get(i));
            List<ConstructorDeclaration> removedConstructors = diff.CheckingForRemovedConstructors(futarinodiffu.get(0).get(i), futarinodiffu.get(1).get(i));
            List<List<MethodDeclaration>> remainingMethods = diff.getUnremovedMethods(futarinodiffu.get(0).get(i), futarinodiffu.get(1).get(i));
            List<List<FieldDeclaration>> remainingFields = diff.getUnremovedFields(futarinodiffu.get(0).get(i), futarinodiffu.get(1).get(i));

            int size2 = remainingMethods.get(0).size();

            for (int j = 0; j < size2; j++) {
                diff.methodComparison(remainingMethods.get(0).get(j), remainingMethods.get(1).get(j));

            }
            List<List<ConstructorDeclaration>> remainingConstructors = diff.getUnremovedConstructors(futarinodiffu.get(0).get(i), futarinodiffu.get(1).get(i));
            //int size3 = remainingConstructors.get(0).size();
            //for (int j = 0; j < size3; j++) {

            //}
        }





    }


}