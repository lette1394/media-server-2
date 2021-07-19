package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

import static java.util.stream.Collectors.toUnmodifiableSet;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.reflections.Reflections;

@RequiredArgsConstructor
class SingleScanner implements ResourceScanner {
  private static final Class<SingleResource> SCANNING_TYPE = SingleResource.class;

  private final ClassPathFactory classPathFactory;
  private final Reflections reflections;

  @Override
  public Set<? extends FileResource<?>> scan() {
    return reflections
      .getTypesAnnotatedWith(SCANNING_TYPE)
      .stream()
      .map(this::singleFileResources)
      .collect(toUnmodifiableSet());
  }

  private FileResource<?> singleFileResources(Class<?> type) {
    final var path = type.getAnnotation(SCANNING_TYPE).filePath();
    final var classPath = classPathFactory.create(path);

    return new FileResource<>(type, classPath);
  }
}
