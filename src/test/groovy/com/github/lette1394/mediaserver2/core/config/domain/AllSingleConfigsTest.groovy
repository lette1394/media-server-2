package com.github.lette1394.mediaserver2.core.config.domain

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.github.lette1394.mediaserver2.core.config.infrastructure.AllResources
import com.github.lette1394.mediaserver2.core.config.infrastructure.SingleFileResource
import groovy.transform.Canonical
import spock.lang.Specification

import static com.github.lette1394.mediaserver2.core.config.domain.TestFixtures.CURRENT_PACKAGE

class AllSingleConfigsTest extends Specification {
  private final AllResources allResources = AllResources.builder()
    .rootResourceDirectory("/core/config")
    .rootScanningPackage(CURRENT_PACKAGE)
    .objectMapper(new ObjectMapper(new YAMLFactory()))
    .build()

  def "find single resource"() {
    given: "I got all single resources"
      def resources = allResources.single()

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

  class AAAService {
    private final Person person

    public void doSomething() {
      // business logics;;
      println(person.age)

    }
  }
}
