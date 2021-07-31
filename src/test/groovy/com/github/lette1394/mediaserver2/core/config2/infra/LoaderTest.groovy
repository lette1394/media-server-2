package com.github.lette1394.mediaserver2.core.config2.infra

import com.github.lette1394.mediaserver2.core.config2.domain.ConfigException
import spock.lang.Specification

abstract class LoaderTest<T> extends Specification {
  abstract Loader subject()

  abstract Key<T> existingKey()

  abstract Key<T> missingKey()

  def "It should not return null"() {
    when:
      def config = subject().load(existingKey())
    then:
      config != null
  }

  def "It should throw exception if missing config"() {
    when:
      subject().load(missingKey())
    then:
      thrown ConfigException
  }
}
