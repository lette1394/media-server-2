package com.github.lette1394.mediaserver2.storage.infrastructure;

import java.io.IOException;

public final class BrokenIOException extends IOException {
  public BrokenIOException(String message) {
    super(message);
  }
}
