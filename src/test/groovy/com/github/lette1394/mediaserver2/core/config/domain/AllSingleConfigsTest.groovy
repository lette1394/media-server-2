package com.github.lette1394.mediaserver2.core.config.domain

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.github.lette1394.mediaserver2.core.config.infrastructure.AllResources
import com.github.lette1394.mediaserver2.core.config.infrastructure.SingleFileResource
import groovy.transform.Canonical
import spock.lang.Specification

import static com.github.lette1394.mediaserver2.core.config.TestFixtures.ALL_RESOURCES

class AllSingleConfigsTest extends Specification {
  def "find single resource"() {
    given: "I got all single resources"
      def resources = ALL_RESOURCES.single()

    when: "I find Person resource"
      def person = resources.find(Person.class)

    then: "I should have deserialized Person instance"
      person == new Person("hong gil-dong", 21)
  }

  @Canonical
  @SingleFileResource(filePath = "/single/person.yaml")
  static class Person {
    String name
    int age
  }
}
