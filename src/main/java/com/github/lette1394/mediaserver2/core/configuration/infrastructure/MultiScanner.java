package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

import static java.util.stream.Collectors.toUnmodifiableSet;

import io.vavr.control.Try;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.reflections.Reflections;

@RequiredArgsConstructor
class MultiScanner implements ResourceScanner {
  private static final Class<MultiFileResource> SCANNING_TYPE = MultiFileResource.class;

  private final FileResourcePathFactory fileResourcePathFactory;
  private final Reflections reflections;

  @Override
  public Set<? extends FileResource<?>> scan() {
    return scanMulti()
      .stream()
      .map(typeAndName -> {
        final var type = typeAndName.getLeft();
        final var name = typeAndName.getRight();
        final var annotation = type.getAnnotation(SCANNING_TYPE);

        return new FileResource<>(type, fileResourcePathFactory.create(annotation, name));
      })
      .collect(toUnmodifiableSet());
  }

  public Set<? extends Pair<? extends Class<?>, String>> scanMulti() {
    return reflections
      .getTypesAnnotatedWith(SCANNING_TYPE)
      .stream()
      .flatMap(this::multiFile)
      .collect(toUnmodifiableSet());
  }

  private Stream<? extends Pair<? extends Class<?>, String>> multiFile(Class<?> type) {
    final var path = type.getAnnotation(SCANNING_TYPE).directoryPath();
    final var directory = fileResourcePathFactory.create(path);
    return Try
      .of(() -> Files
        .walk(directory.toPath())
        .map(Path::toFile)
        .filter(File::isFile)
        .filter(File::exists)
        .map(File::getName)
        .map(name -> Pair.of(type, name)))
      .get();
  }
}
