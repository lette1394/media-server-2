package com.github.lette1394.mediaserver2.core.resource.infra

import spock.lang.Specification

class FileConfigTest extends Specification {
  def "The path should be start with /"() {
    when: "I create file resource"
      def resource = new FileResource("hello.txt")
    then:
      thrown(IllegalArgumentException)
  }

  def "It should fail to load file if it doesn't exist"() {
    given: "not exist file"
      def path = "/not-exist.txt"

    when: "I create file resource"
      def resource = new FileResource(path)

    then: "Its contents should be 'world'"
      thrown(FileNotFoundException)
  }

  def "It should load any resources based on classpath"() {
    given: "a path of hello.txt"
      def path = "/core/resource/hello.txt"

    when: "I create file resource"
      def resource = new FileResource(path)

    then: "Its contents should be 'world'"
      resource.contents() == "world".getBytes()
  }
}
