package com.github.lette1394.mediaserver2.storage.domain;

import java.io.IOException;

final class BrokenIOException extends IOException {
  public BrokenIOException(String message) {
    super(message);
  }
}
