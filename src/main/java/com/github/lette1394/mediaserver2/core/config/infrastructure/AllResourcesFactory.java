package com.github.lette1394.mediaserver2.core.config.infrastructure;

import com.github.lette1394.mediaserver2.core.config.domain.AllMultipleResources;
import com.github.lette1394.mediaserver2.core.config.domain.AllSingleResources;
import io.vavr.control.Try;

public interface AllResourcesFactory {
  Try<AllSingleResources> single();

  Try<AllMultipleResources> multi();
}
