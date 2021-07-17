package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

import static com.github.lette1394.mediaserver2.core.domain.Contracts.requires;
import static org.apache.commons.lang3.StringUtils.isNoneBlank;

record ClassPath(String rawPath) {

  ClassPath {
    requires(isNoneBlank(rawPath), "isNoneBlank(rawPath)");
    requires(rawPath.startsWith("/"), "rawPath.startsWith(\"/\")");
    requires(!rawPath.endsWith("/"), "!rawPath.endsWith(\"/\")");
  }

  ClassPath concat(ClassPath other) {
    return new ClassPath(this.rawPath + other.rawPath);
  }

  @Override
  public String toString() {
    return rawPath;
  }
}
