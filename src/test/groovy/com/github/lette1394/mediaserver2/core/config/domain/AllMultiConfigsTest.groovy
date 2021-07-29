package com.github.lette1394.mediaserver2.core.config.domain


import com.github.lette1394.mediaserver2.core.config.infrastructure.MultiFileResource
import groovy.transform.Canonical
import spock.lang.Specification

import static com.github.lette1394.mediaserver2.core.config.TestFixtures.ALL_RESOURCES
import static com.github.lette1394.mediaserver2.core.config.domain.AllMultiConfigsTest.Animal.Type.CAT
import static com.github.lette1394.mediaserver2.core.config.domain.AllMultiConfigsTest.Animal.Type.DOG

class AllMultiConfigsTest extends Specification {
  def "find multi resource"() {
    given: "I got all multi resources"
      def resources = ALL_RESOURCES.multi()

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
