package com.github.lette1394.mediaserver2.core.config2.infra

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.github.lette1394.mediaserver2.core.config2.domain.AllConfigs
import com.github.lette1394.mediaserver2.core.config2.domain.AllConfigsTest

class AllFileConfigsTest extends AllConfigsTest {
  @Override
  AllConfigs subject() {
    def key = new Cached(new Aliased(new Annotated()))
    def loader = new Jackson(new ObjectMapper(new YAMLFactory()))
    return new AllFileConfigs(key, loader)
  }
}
