package com.github.lette1394.mediaserver2.core.config2.infra

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.github.lette1394.mediaserver2.core.config2.domain.AllConfigs
import com.github.lette1394.mediaserver2.core.config2.domain.AllConfigsTest

class ConfigLoaderAdapterTest extends AllConfigsTest {
  @Override
  AllConfigs subject() {
    def aa = new Cached(new Annotation())
    return new ConfigLoaderAdapter(aa, new Jackson(new ObjectMapper(new YAMLFactory())))
  }
}
