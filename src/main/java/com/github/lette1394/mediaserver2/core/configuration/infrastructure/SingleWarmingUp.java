package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

import com.github.lette1394.mediaserver2.core.configuration.domain.AllSingleResources;
import io.vavr.control.Option;
import java.util.Set;

class SingleWarmingUp implements AllSingleResources {
  private final AllSingleResources allSingleResources;

  public SingleWarmingUp(AllSingleResources allSingleResources, SingleScanner scanner) {
    this.allSingleResources = allSingleResources;
    warmUp(scanner.scanSingle());
  }

  private void warmUp(Set<Class<?>> types) {
    types.forEach(allSingleResources::find);
  }

  @Override
  public <T> Option<T> find(Class<T> type) {
    return allSingleResources.find(type);
  }
}

