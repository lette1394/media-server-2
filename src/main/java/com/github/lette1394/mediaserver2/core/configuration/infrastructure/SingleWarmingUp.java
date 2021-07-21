package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

import com.github.lette1394.mediaserver2.core.configuration.domain.AllSingleResources;
import io.vavr.control.Try;
import java.util.Set;

class SingleWarmingUp implements AllSingleResources {
  private final AllSingleResources allSingleResources;

  private SingleWarmingUp(AllSingleResources allSingleResources) {
    this.allSingleResources = allSingleResources;
  }

  static Try<AllSingleResources> create(AllSingleResources resources, ResourceScanner scanner) {
    return scanner.scan()
      .andThen(fileResources -> warmUp(resources, fileResources))
      .map(__ -> new SingleWarmingUp(resources));
  }

  private static void warmUp(
    AllSingleResources resources,
    Set<? extends FileResource<?>> fileResources) {

    fileResources.forEach(fileResource -> resources.find(fileResource.type()));
  }

  @Override
  public <T> T find(Class<T> type) {
    return allSingleResources.find(type);
  }
}

