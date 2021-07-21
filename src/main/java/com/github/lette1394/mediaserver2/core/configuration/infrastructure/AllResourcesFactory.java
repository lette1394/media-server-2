package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

import com.github.lette1394.mediaserver2.core.configuration.domain.AllMultipleResources;
import com.github.lette1394.mediaserver2.core.configuration.domain.AllSingleResources;
import io.vavr.control.Try;

public interface AllResourcesFactory {
  Try<AllSingleResources> single();

  Try<AllMultipleResources> multi();
}
