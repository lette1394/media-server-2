package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

import static java.util.stream.Collectors.toSet;

import com.github.lette1394.mediaserver2.core.configuration.domain.AllMultipleResources;
import com.github.lette1394.mediaserver2.core.configuration.domain.AllSingleResources;
import io.vavr.control.Option;
import io.vavr.control.Try;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import org.reflections.Reflections;
import org.reflections.scanners.TypeAnnotationsScanner;

public class WarmUpFileLoader implements AllSingleResources, AllMultipleResources {
  private final AllSingleResources allSingleResources;
  private final AllMultipleResources allMultipleResources;
  private final ClassPaths classPaths;

  public WarmUpFileLoader(
    AllSingleResources allSingleResources,
    AllMultipleResources allMultipleResources,
    String basePackage,
    ClassPaths classPaths) {
    this.allSingleResources = allSingleResources;
    this.allMultipleResources = allMultipleResources;
    Reflections reflections = new Reflections(basePackage, new TypeAnnotationsScanner());
    this.classPaths = classPaths;

    reflections
      .getTypesAnnotatedWith(SingleResource.class)
      .forEach(allSingleResources::find);

    reflections
      .getTypesAnnotatedWith(MultipleResource.class)
      .forEach(type -> {
        final var directoryPath = type.getAnnotation(MultipleResource.class).directoryPath();
        final var path = this.classPaths.create(directoryPath).toPath();
        final var names = Try
          .of(() -> Files
            .walk(path)
            .map(Path::toFile)
            .filter(File::isFile)
            .filter(File::exists)
            .map(File::getName)
            .collect(toSet()))
          .get();

        names.forEach(name -> allMultipleResources.find(type, name));
      });
  }

  @Override
  public <T> Option<T> find(Class<T> type) {

    return null;
  }

  @Override
  public <T> Option<T> find(Class<T> type, String name) {
    return null;
  }
}
