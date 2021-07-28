package com.github.lette1394.mediaserver2.core.config.domain

import com.github.lette1394.mediaserver2.core.config.infrastructure.ByteArrayResource

class ByteArrayResourceTest extends ResourceTest {
  @Override
  Resource subject() {
    return new ByteArrayResource("hello")
  }
}
