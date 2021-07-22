package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

import static java.util.stream.Collectors.toUnmodifiableSet;

import io.vavr.control.Try;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.reflections.Reflections;

@RequiredArgsConstructor
class SingleScanner implements ResourceScanner {
  private static final Class<SingleFileResource> SCANNING_TYPE = SingleFileResource.class;

  private final FileResourcePathFactory fileResourcePathFactory;
  private final Reflections reflections;

  @Override
  public Try<Set<? extends FileResource<?>>> scan() {
    return Try.of(() -> scanSingle()
      .stream()
      .map(this::singleFileResources)
      .collect(toUnmodifiableSet()));
  }

  public Set<Class<?>> scanSingle() {
    return reflections.getTypesAnnotatedWith(SCANNING_TYPE);
  }

  private FileResource<?> singleFileResources(Class<?> type) {
    final var annotation = type.getAnnotation(SCANNING_TYPE);
    final var classPath = fileResourcePathFactory.create(annotation).get();
    return new FileResource<>(type, classPath);
  }
}
