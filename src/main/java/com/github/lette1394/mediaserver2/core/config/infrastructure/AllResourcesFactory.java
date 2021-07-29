package com.github.lette1394.mediaserver2.core.config.infrastructure;

import com.github.lette1394.mediaserver2.core.config.domain.AllMultiConfigs;
import com.github.lette1394.mediaserver2.core.config.domain.AllSingleConfigs;
import io.vavr.control.Try;

public interface AllResourcesFactory {
  Try<AllSingleConfigs> single();

  Try<AllMultiConfigs> multi();
}
