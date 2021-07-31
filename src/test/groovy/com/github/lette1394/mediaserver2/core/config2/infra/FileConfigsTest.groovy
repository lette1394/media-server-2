package com.github.lette1394.mediaserver2.core.config2.infra

import com.github.lette1394.mediaserver2.core.config2.domain.AllConfigs
import com.github.lette1394.mediaserver2.core.config2.domain.AllConfigsTest
import com.github.lette1394.mediaserver2.core.config2.domain.Key
import groovy.transform.Canonical
import groovy.transform.Immutable
import groovy.transform.builder.Builder

import java.nio.file.Path

class FileConfigsTest extends AllConfigsTest {
  @Override
  AllConfigs subject() {
    return new FileConfigs()
  }

  @Override
  Key<Person> existingKey() {
    return new FileKey<>(Person.class, "/core/config2/person.yaml")
  }

  @Override
  Key<Person> missingKey() {
    return new FileKey<>(Person.class, "/core/config2/not-existing.yaml")
  }

  @Immutable
  static class Person {
    String name
    int age
  }
}
