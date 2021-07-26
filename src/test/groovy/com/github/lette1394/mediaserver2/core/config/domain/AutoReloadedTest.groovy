package com.github.lette1394.mediaserver2.core.config.domain

import com.github.lette1394.mediaserver2.core.configuration.infrastructure.AutoReloadingResources
import groovy.transform.Canonical
import spock.lang.Specification

import static com.github.lette1394.mediaserver2.core.configuration.domain.TestFixtures.CURRENT_PACKAGE

class AutoReloadedTest extends Specification {

  def "find resource"() {
    given: "two Address resources, one is 'seoul', the other is 'busan'"
      def seoul =
        """
        cityName: seoul
        zipCode: 100
        """
      def busan =
        """
        cityName: busan
        zipCode: 200
        """

    and: "single resources initialized with 'seoul'"
      def resources = AutoReloadingResources.builder()
        .initialText(seoul)
        .rootScanningPackage(CURRENT_PACKAGE)
        .build()


    when: "I find 'seoul' instance"
      def autoReloadedAddress = resources.single().find(Address.class)

    then: "It should be 'seoul'"
      autoReloadedAddress == createAddress("seoul", 100)


    when: "I reload the resources with busan"
      resources.setText(busan)
      resources.reload()

    then: """
          Previously found Address instance should be AUTOMATICALLY reloaded to 'seoul'->'busan' 
          WITHOUT having to find them again
          """
      autoReloadedAddress == createAddress("busan", 200)
  }

  private static AddressYamlResource createAddress(String cityName, int zipCode) {
    return new AddressYamlResource(cityName, zipCode)
  }

  @AutoReloaded
  interface Address {
  }

  @Canonical
  private static class AddressYamlResource implements MappedResource<Address>, Address {
    String cityName
    int zipCode

    @Override
    Address toMapped() {
      return this
    }
  }
}
