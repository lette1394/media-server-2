package com.github.lette1394.mediaserver2.core.resource.infra;

import static com.github.lette1394.mediaserver2.core.fluency.domain.Contracts.requires;

import com.github.lette1394.mediaserver2.core.resource.domain.Resource;
import com.github.lette1394.mediaserver2.core.resource.domain.ResourceLoader;
import java.io.FileNotFoundException;

public class RootPathResourceLoader implements ResourceLoader {
  private final ResourceLoader resourceLoader;
  private final String rootPath;

  public RootPathResourceLoader(ResourceLoader resourceLoader, String rootPath) {
    requires(rootPath.startsWith("/"), IllegalArgumentException::new);
    requires(getClass().getResourceAsStream(rootPath) != null, FileNotFoundException::new);

    this.resourceLoader = resourceLoader;
    this.rootPath = rootPath;
  }

  @Override
  public Resource load(String path) {
    return resourceLoader.load(rootPath + path);
  }
}
