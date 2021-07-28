package com.github.lette1394.mediaserver2.core.config.infrastructure


import com.github.lette1394.mediaserver2.core.config.domain.AutoReload
import spock.lang.Specification

class SingleAutoReloadingTest extends Specification {
  def "It should create an auto-reloading config"() {
    given: "dog config with @AutoReload annotation"
      def config = new AnyConfig(new Dog())
      def configs = new SingleAutoReloading(config)

    when: "I find an animal"
      def animal = configs.find(Animal.class)
    then: "It should be a dog"
      animal.call() == "bark"

    when: "I update config [dog -> cat]"
      config.update(new Cat())
    then: "It should be a cat, not a dog anymore WITHOUT FINDING AN ANIMAL AGAIN"
      animal.call() == "meow"
  }


  @AutoReload
  private interface Animal {
    String call()
  }

  private static class Dog implements Animal {
    @Override
    String call() {
      return "bark"
    }
  }

  private static class Cat implements Animal {
    @Override
    String call() {
      return "meow"
    }
  }
}
