package com.github.lette1394.mediaserver2.core.config.domain

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.github.lette1394.mediaserver2.core.config.infrastructure.AllResources
import com.github.lette1394.mediaserver2.core.config.infrastructure.MultiFileResource
import groovy.transform.Canonical
import spock.lang.Specification

import static com.github.lette1394.mediaserver2.core.config.domain.AllMultipleConfigsTest.Animal.Type.CAT
import static com.github.lette1394.mediaserver2.core.config.domain.AllMultipleConfigsTest.Animal.Type.DOG

class AllMultipleConfigsTest extends Specification {
  private final AllResources allResources = AllResources.builder()
    .rootResourceDirectory("/core/config")
    .rootScanningPackage(TestFixtures.CURRENT_PACKAGE)
    .objectMapper(new ObjectMapper(new YAMLFactory()))
    .build()

  def "find multi resource"() {
    given: "I got all multi resources"
      def resources = allResources.multi()

    when: "I find Animal resources"
      def animal = resources.find(Animal.class, name)

    then: "I should have deserialized Animal instance"
      animal == resource

    where:
      name  || resource
      "cat" || new Animal("kitty", CAT)
      "dog" || new Animal("charlie", DOG)
  }

  @Canonical
  @MultiFileResource(directoryPath = "/multi/animal")
  static class Animal {
    String name
    Type type

    enum Type {
      DOG, CAT
    }
  }
}