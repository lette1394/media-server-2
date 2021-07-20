package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

import com.github.lette1394.mediaserver2.core.configuration.domain.AllMultipleResources;
import java.util.Set;
import org.apache.commons.lang3.tuple.Pair;

class MultiWarmingUp implements AllMultipleResources {
  private final AllMultipleResources resources;

  public MultiWarmingUp(AllMultipleResources resources, MultiScanner multiScanner) {
    this.resources = resources;
    warmUp(multiScanner.scanMulti());
  }

  private void warmUp(Set<? extends Pair<? extends Class<?>, String>> typeAndNames) {
    typeAndNames
      .forEach(typeAndName -> resources.find(typeAndName.getLeft(), typeAndName.getRight()));
  }

  @Override
  public <T> T find(Class<T> type, String name) {
    return resources.find(type, name);
  }
}

