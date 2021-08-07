package com.github.lette1394.mediaserver2.core.config2.domain


import spock.lang.Specification

abstract class AllConfigsTest extends Specification {
  abstract AllConfigs subject()

  def "config class should have @Config annotation"() {
    when:
      subject().find(NoConfig)
    then:
      def e = thrown(ConfigException)
      println(e)
  }

  def "config class should have at least one @ConfigLocation annotation"() {
    when:
      subject().find(NoConfigLocation)
    then:
      thrown(ConfigException)
  }

  def "config class should support deserialization: not pojo"() {
    when:
      subject().find(NoDeserializationSupport_NotPojo)
    then:
      thrown(ConfigException)
  }

  def "config class should support deserialization: wrong field name"() {
    when:
      subject().find(NoDeserializationSupport_WrongFieldName)
    then:
      thrown(ConfigException)
  }

  def "find() should not return null"() {
    expect:
      subject().find(PersonConfig) != null
      subject().find(AnimalConfig, "dog-1.yaml") != null
      subject().find(AnimalConfig, "dog-2.yaml") != null
      subject().find(AnimalConfig, "cat.yaml") != null
  }

  def "find() should throw exception if the name is unknown"() {
    when:
      subject().find(PersonConfig, "unknown-name")
    then:
      thrown(ConfigException)
  }
}
