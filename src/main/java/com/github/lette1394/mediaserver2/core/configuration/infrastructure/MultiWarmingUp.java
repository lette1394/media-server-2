package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

import static java.util.stream.Collectors.toSet;

import com.github.lette1394.mediaserver2.core.configuration.domain.AllMultipleResources;
import io.vavr.control.Option;
import io.vavr.control.Try;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import org.reflections.Reflections;

class MultiWarmingUp implements AllMultipleResources {
  private final AllMultipleResources resources;
  private final ClassPathFactory classPathFactory;

  public MultiWarmingUp(
    AllMultipleResources resources,
    String basePackage,
    ClassPathFactory classPathFactory) {

    this.resources = resources;
    this.classPathFactory = classPathFactory;

    var reflections = new Reflections(basePackage);
    warmUpMultiple(resources, reflections);
  }

  private void warmUpMultiple(AllMultipleResources allMultipleResources, Reflections reflections) {
    reflections
      .getTypesAnnotatedWith(MultiResource.class)
      .forEach(type -> {
        final var directoryPath = type.getAnnotation(MultiResource.class).directoryPath();
        final var path = classPathFactory.create(directoryPath).toPath();
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
  public <T> Option<T> find(Class<T> type, String name) {
    return resources.find(type, name);
  }
}

