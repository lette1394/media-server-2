package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

import com.github.lette1394.mediaserver2.core.configuration.domain.AllSingleResources;
import io.vavr.control.Option;
import org.reflections.Reflections;

class SingleWarmingUp implements AllSingleResources {
  private final AllSingleResources allSingleResources;

  public SingleWarmingUp(AllSingleResources allSingleResources, Reflections reflections) {
    this.allSingleResources = allSingleResources;

    warmUp(allSingleResources, reflections);
  }

  private void warmUp(AllSingleResources allSingleResources, Reflections reflections) {
    reflections
      .getTypesAnnotatedWith(SingleFileResource.class)
      .forEach(allSingleResources::find);
  }

  @Override
  public <T> Option<T> find(Class<T> type) {
    return allSingleResources.find(type);
  }
}

