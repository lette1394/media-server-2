package com.github.lette1394.mediaserver2.core.config.infrastructure

import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import spock.lang.Specification

class PolymorphismYamlTest extends Specification {
  def "polymorphism test"() {
    given:
      def resources = AllResources.builder()
        .rootScanningPackage("com.github.lette1394.mediaserver2.core.config.infrastructure")
        .rootResourceDirectory("/core/config/single")
        .objectMapper(new ObjectMapper(new YAMLFactory()).enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS))
        .build()
      def multi = resources.multi()
    when:
      def s = multi.find(Endpoint.class, "single-region")
      def m = multi.find(Endpoint.class, "multi-region")
    then:
      for(def region : Region.values()) {
        println("" + region + " - " + s.endpoint(region))
        println("" + region + " - " + m.endpoint(region))
      }
  }
}
