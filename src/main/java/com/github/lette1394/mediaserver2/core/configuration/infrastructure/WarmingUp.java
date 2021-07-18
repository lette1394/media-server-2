package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

import static java.util.stream.Collectors.toSet;

import io.vavr.control.Try;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Stream;
import org.reflections.Reflections;

class WarmingUp implements UnsafeFileResources {
  private final UnsafeFileResources resources;
  private final ClassPathFactory classPathFactory;
  private final Reflections reflections;

  public WarmingUp(
    UnsafeFileResources resources,
    Reflections reflections,
    ClassPathFactory classPathFactory) throws IOException {

    this.resources = resources;
    this.classPathFactory = classPathFactory;
    this.reflections = reflections;

    warmUpSingle();
    warmUpMultiple();
  }

  private void warmUpSingle() throws IOException {
    final var resourceSet = reflections
      .getTypesAnnotatedWith(SingleResource.class)
      .stream()
      .map(this::singleFileResources)
      .collect(toSet());

    loadAll(resourceSet);
  }

  private FileResource<?> singleFileResources(Class<?> type) {
    final var path = type.getAnnotation(SingleResource.class).filePath();
    final var classPath = classPathFactory.create(path);

    return new FileResource<>(type, classPath);
  }

  private void warmUpMultiple() throws IOException {
    final var resourceSet = reflections
      .getTypesAnnotatedWith(MultiResource.class)
      .stream()
      .flatMap(this::multiFileResources)
      .collect(toSet());

    loadAll(resourceSet);
  }

  private Stream<? extends FileResource<?>> multiFileResources(Class<?> type) {
    final var path = type.getAnnotation(MultiResource.class).directoryPath();
    final var directory = classPathFactory.create(path);
    return Try
      .of(() -> Files
        .walk(directory.toPath())
        .map(Path::toFile)
        .filter(File::isFile)
        .filter(File::exists)
        .map(File::getName)
        .map(name -> new FileResource<>(type, directory.concat("/%s".formatted(name)))))
      .get();
  }

  private void loadAll(Set<? extends FileResource<?>> resourceSet) throws IOException {
    for (var fileResource : resourceSet) {
      resources.load(fileResource);
    }
  }

  @Override
  public <T> T load(FileResource<T> fileResource) throws IOException {
    return resources.load(fileResource);
  }
}

