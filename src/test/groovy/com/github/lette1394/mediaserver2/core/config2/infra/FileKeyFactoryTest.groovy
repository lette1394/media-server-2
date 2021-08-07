package com.github.lette1394.mediaserver2.core.config2.infra

import spock.lang.Specification

abstract class FileKeyFactoryTest extends Specification {
  abstract FileKeyFactory subject()

  def "create() should not return null"() {
    subject().create()
  }


}
