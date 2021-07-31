package com.github.lette1394.mediaserver2.core.config2.infra

import com.github.lette1394.mediaserver2.core.config2.domain.ConfigException
import spock.lang.Specification

import static com.github.lette1394.mediaserver2.core.config2.infra.TestFixtures.PERSON_PATH

class FileKeyTest extends Specification {
  def "equals should be true if same parameters"() {
    expect:
      subject() == subject()
  }

  def "Its contents should equals classpath file's one"() {
    given:
      def expected = """\
name: na
age: 28
"""
    when:
      def contents = new String(subject().contents())
    then:
      contents == expected
  }

  def "creation should fail if no files"() {
    when:
      new FileKey<>(Person.class, "/not-existing-file.yaml")
    then:
      thrown ConfigException
  }


  static def subject() {
    return new FileKey(Person, PERSON_PATH)
  }
}
