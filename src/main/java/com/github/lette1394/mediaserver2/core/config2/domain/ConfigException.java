package com.github.lette1394.mediaserver2.core.config2.domain;

public class ConfigException extends RuntimeException {
  public ConfigException() {
  }

  public ConfigException(Throwable cause) {
    super(cause);
  }

  public ConfigException(String message, Throwable cause) {
    super(message, cause);
  }
}
