package com.github.lette1394.mediaserver2.core.config2.infra

import com.github.lette1394.mediaserver2.core.config2.domain.AllConfigs
import com.github.lette1394.mediaserver2.core.config2.domain.AllConfigsTest
import com.github.lette1394.mediaserver2.core.config2.domain.Key

import java.nio.file.Path

class FileConfigsTest extends AllConfigsTest {
  @Override
  AllConfigs subject() {
    return new FileConfigs()
  }

  @Override
  Key existingKey() {
    return new FileKey(Person.class, path("/core/config2/person.yaml"))
  }

  @Override
  Key missingKey() {
    return new FileKey(Person.class, path("/core/config2/not-existing.yaml"))
  }

  private path(String path) {
    return Path.of(getClass().getResource(path).toURI())
  }

  class Person {
    String name
    int age
  }
}
