package com.github.lette1394.mediaserver2.core.config2.infra

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory

import static com.github.lette1394.mediaserver2.core.config2.infra.TestFixtures.PERSON_FILE_PATH

class JacksonTest extends LoaderTest {
  @Override
  Loader subject() {
    return new Jackson(new ObjectMapper(new YAMLFactory()))
  }

  @Override
  Key<Person> existingKey() {
    return new FileKey<>(Person, PERSON_FILE_PATH)
  }

  @Override
  Key<Person> missingKey() {
    return new FileKey<>(Person, "/not-existing.yaml")
  }
}
