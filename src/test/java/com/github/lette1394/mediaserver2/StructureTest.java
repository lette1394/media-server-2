package com.github.lette1394.mediaserver2;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.DependencyRules.NO_CLASSES_SHOULD_DEPEND_UPPER_PACKAGES;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_THROW_GENERIC_EXCEPTIONS;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_USE_FIELD_INJECTION;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_USE_JODATIME;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import java.util.Arrays;
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
  private static final ArchRule CORE_CONTEXT =
    noClasses().that()
      .resideInAPackage(context("core"))
      .should().dependOnClassesThat()
      .resideInAnyPackage(contexts("runner", "storage", "media"));

  @ArchTest
  private static final ArchRule NO_CYCLE =
    slices().matching("..mediaserver2.(**)..").should().beFreeOfCycles();

  @ArchTest
  private static final ArchRule NO_CYCLE2 =
    slices().matching("..mediaserver2.(*)..").should().beFreeOfCycles();

  @ArchTest
  private static final ArchRule no_jodatime = NO_CLASSES_SHOULD_USE_JODATIME;

  @ArchTest
  private static final ArchRule no_field_injection = NO_CLASSES_SHOULD_USE_FIELD_INJECTION;

  @ArchTest
  private static final ArchRule no_generic_exceptions = NO_CLASSES_SHOULD_THROW_GENERIC_EXCEPTIONS;


  private static String[] commonAnd(String... packages) {
    return ArrayUtils.addAll(packages, COMMON_PACKAGES);
  }

  private static String[] contexts(String... names) {
    return Arrays.stream(names)
      .map(StructureTest::context)
      .toArray(String[]::new);
  }

  private static String context(String name) {
    return "..mediaserver2.%s..".formatted(name);
  }
}
