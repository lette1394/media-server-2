package com.github.lette1394.mediaserver2.core.configuration.domain

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.github.lette1394.mediaserver2.core.configuration.infrastructure.AllResources
import com.github.lette1394.mediaserver2.core.configuration.infrastructure.SingleFileResource
import groovy.transform.Canonical
import groovy.transform.builder.Builder
import spock.lang.Specification

class AllSingleResourcesTest extends Specification {
  private final AllResources allResources = AllResources.builder()
    .rootResourceDirectory("/core/configuration/single")
    .scanningPackagePrefix("com.github.lette1394.mediaserver2.core.configuration.domain")
    .objectMapper(new ObjectMapper(new YAMLFactory()))
    .build()

  def "find single resource"() {
    given: 'I got all single resources'
      def resources = allResources.single()

    when: 'I find Person resource'
      def person = resources.find(Person.class)

    then: 'I should have deserialized Person instance'
      person == Person.builder()
        .name("hong gil-dong")
        .age(21)
        .build()
  }

  @Builder
  @Canonical
  @SingleFileResource(filePath = "/person.yaml")
  private static class Person {
    String name
    int age
  }
}
