package com.github.lette1394.mediaserver2.core.config.domain

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.github.lette1394.mediaserver2.core.configuration.infrastructure.AllResources
import com.github.lette1394.mediaserver2.core.configuration.infrastructure.MultiFileResource
import groovy.transform.Canonical
import spock.lang.Specification

import static com.github.lette1394.mediaserver2.core.configuration.domain.AllMultipleResourcesTest.Animal.Type.CAT
import static com.github.lette1394.mediaserver2.core.configuration.domain.AllMultipleResourcesTest.Animal.Type.DOG

class AllMultipleResourcesTest extends Specification {
  private final AllResources allResources = AllResources.builder()
    .rootResourceDirectory("/core/configuration")
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
