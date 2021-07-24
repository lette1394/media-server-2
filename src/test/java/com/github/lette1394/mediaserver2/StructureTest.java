package com.github.lette1394.mediaserver2;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_THROW_GENERIC_EXCEPTIONS;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_USE_FIELD_INJECTION;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_USE_JODATIME;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.apache.commons.lang3.ArrayUtils;

@AnalyzeClasses(packages = "com.github.lette1394.mediaserver2")
public class StructureTest {
  private static final String PRIMITIVES = "";
  private static final String[] COMMON_PACKAGES = {
    PRIMITIVES,
    "java.lang..",
    "java.util..",
    "java.text..",
    "java.time..",
    "io.vavr..",
    "org.reactivestreams.."
  };

  @ArchTest
  private static final ArchRule DOMAIN =
    classes().that()
      .resideInAPackage("..domain..")
      .should().onlyDependOnClassesThat()
      .resideInAnyPackage(commonAnd("..domain.."));

  @ArchTest
  private static final ArchRule USECASE =
    classes().that()
      .resideInAPackage("..usecase..")
      .should().onlyDependOnClassesThat()
      .resideInAnyPackage(commonAnd("..domain..", "..usecase.."));

  @ArchTest
  private static final ArchRule no_jodatime = NO_CLASSES_SHOULD_USE_JODATIME;

  @ArchTest
  private static final ArchRule no_field_injection = NO_CLASSES_SHOULD_USE_FIELD_INJECTION;

  @ArchTest
  private static final ArchRule no_generic_exceptions = NO_CLASSES_SHOULD_THROW_GENERIC_EXCEPTIONS;

  @ArchTest
  private static final ArchRule layer_dependencies_are_respected = layeredArchitecture()
    .layer("core").definedBy("com.github.lette1394.mediaserver2.core..")
    .layer("storage").definedBy("com.github.lette1394.mediaserver2.storage..")
    .layer("media").definedBy("com.github.lette1394.mediaserver2.media..")
    .layer("runner").definedBy("com.github.lette1394.mediaserver2.runner..")

    .whereLayer("runner").mayNotBeAccessedByAnyLayer();


  private static String[] commonAnd(String... packages) {
    return ArrayUtils.addAll(packages, COMMON_PACKAGES);
  }
}
