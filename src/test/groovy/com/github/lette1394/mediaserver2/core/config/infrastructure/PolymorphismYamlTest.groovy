package com.github.lette1394.mediaserver2.core.config.infrastructure


import spock.lang.Specification

import static com.github.lette1394.mediaserver2.core.config.TestFixtures.ALL_RESOURCES

class PolymorphismYamlTest extends Specification {
  def "polymorphism test"() {
    given:
      def multi = ALL_RESOURCES.multi()
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
