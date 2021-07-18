package com.github.lette1394.mediaserver2.core.configuration.domain;

import com.github.lette1394.mediaserver2.core.configuration.infrastructure.MappedResource;
import com.github.lette1394.mediaserver2.core.configuration.infrastructure.SingleResource;

@SingleResource(filePath = "/person.yaml")
public record PersonResource(
  String name,
  Type type,
  int age1,
  int age2
) implements MappedResource<Person> {

  @Override
  public Person toMapped() {
    if (type == Type.MALE) {
      return new Person() {
        @Override
        public boolean isKim() {
          return name.startsWith("kim");
        }

        @Override
        public String hello() {
          return "hello [M] %s, %s".formatted(name, age1 + age2);
        }
      };
    }
    if (type == Type.FEMALE) {
      return new Person() {
        @Override
        public boolean isKim() {
          return name.startsWith("kim");
        }

        @Override
        public String hello() {
          return "hello [F] %s, %s".formatted(name, age1 + age2);
        }
      };
    }

    throw new IllegalArgumentException();
  }
}

enum Type {
  MALE,
  FEMALE,
}