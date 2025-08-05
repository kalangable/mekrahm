package com.mekrahm;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

class ApplicationTest {

    ApplicationModules modules = ApplicationModules.of(Application.class);

    @Test
    void shouldBeCompliant() {
        modules.verify();
    }

    @Test
    void writeDocumentationSnippets() {
        new Documenter(modules)
            .writeModuleCanvases()
            .writeModulesAsPlantUml()
            .writeIndividualModulesAsPlantUml();
    }
}
