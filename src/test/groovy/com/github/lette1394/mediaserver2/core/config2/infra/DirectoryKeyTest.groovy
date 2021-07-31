package com.github.lette1394.mediaserver2.core.config2.infra

import com.github.lette1394.mediaserver2.core.config2.domain.ConfigException
import spock.lang.Specification

import static com.github.lette1394.mediaserver2.core.config2.infra.TestFixtures.PERSON_DIRECTORY_PATH

class DirectoryKeyTest extends Specification {
  def "Creation should fail if the directory does not exist"() {
    when:
      new DirectoryKey<>(Person, "/unknown-directory")
    then:
      thrown ConfigException
  }

  def "It should have all file key inside the directory"() {
    when:
      def files = new DirectoryKey<>(Person, PERSON_DIRECTORY_PATH).fileKeySet()
      def expected = Set.of(
        new FileKey<>(Person, "$PERSON_DIRECTORY_PATH/person-1.yaml"),
        new FileKey<>(Person, "$PERSON_DIRECTORY_PATH/person-2.yaml"),
        new FileKey<>(Person, "$PERSON_DIRECTORY_PATH/inner/person-3.yaml"))
    then:
      files == expected
  }
}
