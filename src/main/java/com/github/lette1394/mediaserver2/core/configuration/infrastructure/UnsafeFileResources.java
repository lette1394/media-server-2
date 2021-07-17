package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

import java.io.IOException;
import java.net.URISyntaxException;

@FunctionalInterface
interface UnsafeFileResources {
  <T> T load(FileResource<T> fileResource) throws IOException, URISyntaxException;
}
