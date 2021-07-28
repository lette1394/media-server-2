package com.github.lette1394.mediaserver2.core.fluency.domain;

public class ContractViolationException extends RuntimeException {
  public ContractViolationException() {
    super();
  }

  public ContractViolationException(String message) {
    super(message);
  }
}
