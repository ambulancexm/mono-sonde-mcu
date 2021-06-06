package fr.thomas.iot;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {
        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("fr.thomas.iot");

        noClasses()
            .that()
            .resideInAnyPackage("fr.thomas.iot.service..")
            .or()
            .resideInAnyPackage("fr.thomas.iot.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..fr.thomas.iot.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
