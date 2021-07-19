package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

import static java.util.stream.Collectors.toUnmodifiableSet;

import io.vavr.control.Try;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.reflections.Reflections;

@RequiredArgsConstructor
class MultiScanner implements ResourceScanner {
  private static final Class<MultiResource> SCANNING_TYPE = MultiResource.class;

  private final ClassPathFactory classPathFactory;
  private final Reflections reflections;

  @Override
  public Set<? extends FileResource<?>> scan() {
    return reflections
      .getTypesAnnotatedWith(SCANNING_TYPE)
      .stream()
      .flatMap(this::multiFileResources)
      .collect(toUnmodifiableSet());
  }

  private Stream<? extends FileResource<?>> multiFileResources(Class<?> type) {
    final var path = type.getAnnotation(SCANNING_TYPE).directoryPath();
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
}
