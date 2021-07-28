package com.github.lette1394.mediaserver2.core.resource.infra


import com.github.lette1394.mediaserver2.core.resource.domain.Loader
import spock.lang.Specification

import static com.github.lette1394.mediaserver2.TestFixtures.anyObject

class RootPathLoaderTest extends Specification {
  def "The root path should be start with /"() {
    when:
      new RootPathLoader(anyObject(), "directory/a/b")
    then:
      thrown(IllegalArgumentException)
  }

  def "The root path should be exist"() {
    when:
      new RootPathLoader(anyObject(), "/not-exist-path")
    then:
      thrown(FileNotFoundException)
  }

  def "It should load resource in relative root path"() {
    given:
      def loader = Mock(Loader)
      def path = new RootPathLoader(loader, "/core/resource")
    when:
      path.load("/hello.txt")
    then:
      1 * loader.load("/core/resource/hello.txt")
  }
}
