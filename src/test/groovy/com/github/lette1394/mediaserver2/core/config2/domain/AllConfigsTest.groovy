package com.github.lette1394.mediaserver2.core.config2.domain

import spock.lang.Specification

abstract class AllConfigsTest extends Specification {
  abstract AllConfigs subject()

  abstract Key existingKey()

  abstract Key missingKey()

  def "It should not return null"() {
    expect:
      subject().find(existingKey()) != null
  }

  def "It should throw exception if missing config"() {
    when:
      subject().find(missingKey())
    then:
      thrown CannotFindConfigException
  }
}
