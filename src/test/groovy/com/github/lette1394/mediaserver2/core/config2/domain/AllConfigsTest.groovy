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

  def "methods should not return null"() {
    expect:
      subject().find(PersonConfig) != null
      subject().find(AnimalConfig, "dog-1.yaml") != null
      subject().find(AnimalConfig, "dog-2.yaml") != null
      subject().find(AnimalConfig, "cat.yaml") != null
  }

  def "methods should throw exception if the name is unknown"() {
    when:
      subject().find(PersonConfig, "unknown-name")
    then:
      thrown(ConfigException)
  }

  // 1. 성공적인 경우
  // 2. alias 선언된 타입과 실제로 매핑되는 타입이 다른 경우
  // 3. (테스트 작성 X) alias가 없는 경우에는 다른 케이스에서 커버됨
  // 4. alias 에 @Config를 붙여줘야 할까?
  def " @ConfigAlias"() {
    when:
      def phone = subject().find(Phone.class)
    then:
      phone.call() == "+82 10-1234-5678"
  }

  def " @ConfigAlias alis uncompatible"() {
    when:
      subject().find(ConfigAliasMisuse.class)
    then:
      def e = thrown(ConfigException)
      println(e)
  }
}
