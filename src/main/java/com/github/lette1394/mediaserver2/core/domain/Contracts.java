package com.github.lette1394.mediaserver2.core.domain;

public final class Contracts {
  private Contracts() {
  }

  public static void requires(boolean condition, String message) {
    if (condition) {
      return;
    }
    throw new ContractViolationException(String.format("required: %s", message));
  }
}
