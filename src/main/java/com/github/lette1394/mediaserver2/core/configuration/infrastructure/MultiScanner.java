package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

import static java.util.stream.Collectors.toUnmodifiableSet;

import io.vavr.control.Try;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.reflections.Reflections;

@RequiredArgsConstructor
class MultiScanner implements ResourceScanner {
  private static final Class<MultiFileResource> SCANNING_TYPE = MultiFileResource.class;

  private final FileResourcePathFactory fileResourcePathFactory;
  private final Reflections reflections;

  @Override
  public Try<Set<? extends FileResource<?>>> scan() {
    return scanMulti().map(this::toFileResources);
  }

  private Set<? extends FileResource<?>> toFileResources(Set<? extends MultiKey<?>> multiKeys) {
    return multiKeys.stream()
      .map(key -> {
        final var type = key.type();
        final var name = key.name();
        final var annotation = type.getAnnotation(SCANNING_TYPE);
        return new FileResource<>(type, fileResourcePathFactory.create(annotation, name));
      })
      .collect(toUnmodifiableSet());
  }

  private Try<Set<? extends MultiKey<?>>> scanMulti() {
    return Try.of(() -> reflections
      .getTypesAnnotatedWith(SCANNING_TYPE)
      .stream()
      .flatMap(this::multiFile)
      .collect(toUnmodifiableSet()));
  }

  @SneakyThrows
  private Stream<? extends MultiKey<?>> multiFile(Class<?> type) {
    final var path = type.getAnnotation(SCANNING_TYPE).directoryPath();
    final var directory = fileResourcePathFactory.create(path);
    return Files
      .walk(directory.toPath())
      .map(Path::toFile)
      .filter(File::isFile)
      .filter(File::exists)
      .map(File::getName)
      .map(name -> new MultiKey<>(type, name));
  }
}
