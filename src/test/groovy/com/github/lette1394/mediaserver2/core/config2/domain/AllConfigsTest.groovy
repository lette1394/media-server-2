package com.github.lette1394.mediaserver2.core.config2.domain

import spock.lang.Specification

abstract class AllConfigsTest<T> extends Specification {
  abstract AllConfigs subject()

  abstract Key<T> existingKey()

  abstract Key<T> missingKey()

  def "It should not return null"() {
    when:
      def config = subject().find(existingKey())
    then:
      config != null
  }

  def "It should throw exception if missing config"() {
    when:
      subject().find(missingKey())
    then:
      thrown ConfigException
  }
}
