package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

import com.github.lette1394.mediaserver2.core.configuration.domain.AllMultipleResources;
import io.vavr.control.Try;
import java.util.Set;

class MultiWarmingUp implements AllMultipleResources {
  private final AllMultipleResources resources;

  private MultiWarmingUp(AllMultipleResources resources) {
    this.resources = resources;
  }

  public static Try<AllMultipleResources> create(
    AllMultipleResources resources,
    ResourceScanner scanner,
    Warmer warmer) {

    return scanner.scan()
      .flatMap(fileResources -> warmUp(resources, fileResources, warmer))
      .map(__ -> new MultiWarmingUp(resources));
  }

  private static Try<Void> warmUp(
    AllMultipleResources resources,
    Set<? extends FileResource<?>> fileResources,
    Warmer warmer) {

    return warmer.submit(() -> fileResources
      .parallelStream()
      .forEach(fileResource -> resources.find(fileResource.type(), fileResource.filename())));
  }

  @Override
  public <T> T find(Class<T> type, String name) {
    return resources.find(type, name);
  }
}

