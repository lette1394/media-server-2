package com.github.lette1394.mediaserver2.core.config2.infra

import com.github.lette1394.mediaserver2.core.config2.domain.ConfigException
import spock.lang.Specification

class FileKeyTest extends Specification {
  def "Equals should be true if same parameters"() {
    expect:
      subject() == subject()
  }

  def "Its contents should be same with classpath file's one"() {
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

  def "Creation should fail if no files"() {
    when:
      new FileKey<>(Person.class, "/not-existing-file.yaml")
    then:
      thrown ConfigException
  }

  static def subject() {
    return new FileKey(Person, "/core/config2/infra/person.yaml")
  }
}
