package com.github.lette1394.mediaserver2.core.config.domain

import spock.lang.Specification

abstract class ResourceTest extends Specification {
  abstract Resource subject()

  def "resource contents should be non-null"() {
    expect:
      subject().contents() != null
  }
}
