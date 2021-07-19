package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

import static java.util.stream.Collectors.toSet;

import java.util.List;
import java.util.Set;

public class Merging implements ResourceScanner {
  private final List<ResourceScanner> resourceScanners;

  public Merging(ResourceScanner... resourceScanners) {
    this.resourceScanners = List.of(resourceScanners);
  }

  @Override
  public Set<? extends FileResource<?>> scan() {
    return resourceScanners
      .stream()
      .flatMap(scanner -> scanner.scan().stream())
      .collect(toSet());
  }
}
