package com.github.lette1394.mediaserver2.core.config2.infra

import com.github.lette1394.mediaserver2.core.config2.domain.AllConfigs
import com.github.lette1394.mediaserver2.core.config2.domain.AllConfigsTest

class ConfigLoaderAdapterTest extends AllConfigsTest {
  @Override
  AllConfigs subject() {
    return new ConfigLoaderAdapter(new Annotation(null), null)
  }
}
