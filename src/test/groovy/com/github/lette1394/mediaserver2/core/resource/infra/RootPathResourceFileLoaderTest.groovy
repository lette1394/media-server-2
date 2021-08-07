package com.github.lette1394.mediaserver2.core.resource.infra


import com.github.lette1394.mediaserver2.core.resource.domain.ResourceLoader
import spock.lang.Specification

import static com.github.lette1394.mediaserver2.TestFixtures.anyObject

class RootPathResourceFileLoaderTest extends Specification {
  def "The root path should be start with /"() {
    when:
      new RootPathResourceLoader(anyObject(), "directory/a/b")
    then:
      thrown(IllegalArgumentException)
  }

  def "The root path should be exist"() {
    when:
      new RootPathResourceLoader(anyObject(), "/not-exist-path")
    then:
      thrown(FileNotFoundException)
  }

  def "It should load resource in relative root path"() {
    given:
      def loader = Mock(ResourceLoader)
      def path = new RootPathResourceLoader(loader, "/core/resource")
    when:
      path.load("/hello.txt")
    then:
      1 * loader.load("/core/resource/hello.txt")
  }
}
