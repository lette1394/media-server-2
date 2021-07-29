package com.github.lette1394.mediaserver2.core.config.infrastructure;

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
final class MultiScanner implements ResourceScanner {
  private static final Class<MultiFileResource> SCANNING_TYPE = MultiFileResource.class;
  private final ClassPathFileFactory classPathFileFactory;
  private final Reflections reflections;

  @Override
  public Try<Set<? extends FileConfig<?>>> scan() {
    return scanMulti();
  }

  private Try<Set<? extends FileConfig<?>>> scanMulti() {
    return Try.of(() -> reflections
      .getTypesAnnotatedWith(SCANNING_TYPE, true)
      .stream()
      .flatMap(this::multiFile)
      .collect(toUnmodifiableSet()));
  }

  @SneakyThrows
  private Stream<? extends FileConfig<?>> multiFile(Class<?> type) {
    final var directoryPath = type.getAnnotation(SCANNING_TYPE).directoryPath();
    final var directory = classPathFileFactory.create(directoryPath).get();

    return Files
      .walk(directory.toJavaPath())
      .map(Path::toFile)
      .filter(File::isFile)
      .filter(File::exists)
      .map(File::getName)
      .map(name -> {
        final var annotation = type.getAnnotation(SCANNING_TYPE);
        final var filePath = classPathFileFactory.create(annotation, name).get();
        return new FileConfig<>(type, filePath);
      });
  }
}
