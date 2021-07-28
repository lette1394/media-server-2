package com.github.lette1394.mediaserver2.core.resource.infra;

import com.github.lette1394.mediaserver2.core.resource.domain.Loader;
import com.github.lette1394.mediaserver2.core.resource.domain.Resource;

public enum FileLoader implements Loader {
  INSTANCE;

  @Override
  public Resource load(String path) {
    return new FileResource(path);
  }
}
