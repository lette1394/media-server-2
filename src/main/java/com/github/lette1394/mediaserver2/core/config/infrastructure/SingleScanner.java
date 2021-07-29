package com.github.lette1394.mediaserver2.core.config.infrastructure;

import static java.util.stream.Collectors.toUnmodifiableSet;

import io.vavr.control.Try;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.reflections.Reflections;

@RequiredArgsConstructor
final class SingleScanner implements ResourceScanner {
  private static final Class<SingleFileResource> SCANNING_TYPE = SingleFileResource.class;

  private final ClassPathFileFactory classPathFileFactory;
  private final Reflections reflections;

  @Override
  public Try<Set<? extends FileConfig<?>>> scan() {
    return Try.of(() -> scanSingle()
      .stream()
      .map(this::singleFileResources)
      .collect(toUnmodifiableSet()));
  }

  public Set<Class<?>> scanSingle() {
    return reflections.getTypesAnnotatedWith(SCANNING_TYPE, true);
  }

  private FileConfig<?> singleFileResources(Class<?> type) {
    final var annotation = type.getAnnotation(SCANNING_TYPE);
    final var classPath = classPathFileFactory.create(annotation).get();
    return new FileConfig<>(type, classPath);
  }
}
