package com.github.lette1394.mediaserver2.core.resource.infra;

import static com.github.lette1394.mediaserver2.core.fluency.domain.Contracts.requires;

import com.github.lette1394.mediaserver2.core.resource.domain.Resource;
import com.github.lette1394.mediaserver2.core.resource.domain.Loader;
import java.io.FileNotFoundException;

public class RootPathLoader implements Loader {
  private final Loader loader;
  private final String rootPath;

  public RootPathLoader(Loader loader, String rootPath) {
    requires(rootPath.startsWith("/"), IllegalArgumentException::new);
    requires(getClass().getResourceAsStream(rootPath) != null, FileNotFoundException::new);

    this.loader = loader;
    this.rootPath = rootPath;
  }

  @Override
  public Resource load(String path) {
    return loader.load(rootPath + path);
  }
}
