package com.github.lette1394.mediaserver2.core.fluency.domain;

import java.util.function.Supplier;
import lombok.SneakyThrows;

public final class Contracts {
  private Contracts() {
  }

  @SneakyThrows
  public static void requires(boolean condition, Supplier<? extends Throwable> exceptionSupplier) {
    if (condition) {
      return;
    }
    throw exceptionSupplier.get();
  }

  public static void requires(boolean condition, String message) {
    if (condition) {
      return;
    }
    throw new ContractViolationException(String.format("required: %s", message));
  }

  public static <T extends Throwable> void checkedRequires(boolean condition,
    Supplier<T> throwableSupplier) throws T {
    if (condition) {
      return;
    }
    throw throwableSupplier.get();
  }
}
