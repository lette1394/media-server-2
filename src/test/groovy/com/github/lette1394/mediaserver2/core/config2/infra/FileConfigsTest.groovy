package com.github.lette1394.mediaserver2.core.config2.infra

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.github.lette1394.mediaserver2.core.config2.domain.AllConfigs
import com.github.lette1394.mediaserver2.core.config2.domain.AllConfigsTest
import com.github.lette1394.mediaserver2.core.config2.domain.Key

import static com.github.lette1394.mediaserver2.core.config2.infra.TestFixtures.PERSON_PATH

class FileConfigsTest extends AllConfigsTest {
  @Override
  AllConfigs subject() {
    return new FileConfigs(new ObjectMapper(new YAMLFactory()))
  }

  @Override
  Key<Person> existingKey() {
    return new FileKey<>(Person.class, PERSON_PATH)
  }

  @Override
  Key<Person> missingKey() {
    return new FileKey<>(Person.class, "/not-existing.yaml")
  }
}
