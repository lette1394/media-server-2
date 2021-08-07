package com.github.lette1394.mediaserver2.core.config2.domain;

@Config
@ConfigLocation("/person.yaml")
class NoDeserializationSupport_NotPojo {
  private final String name;
  private final int age;

  public NoDeserializationSupport_NotPojo(String name, int age) {
    this.name = name;
    this.age = age;
  }
}
