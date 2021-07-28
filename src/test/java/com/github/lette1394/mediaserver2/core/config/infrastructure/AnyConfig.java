package com.github.lette1394.mediaserver2.core.config.infrastructure;

import com.github.lette1394.mediaserver2.core.config.domain.AllSingleConfigs;
import com.github.lette1394.mediaserver2.core.config.domain.CannotFindConfigException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AnyConfig implements AllSingleConfigs {
  private Object object;

  @Override
  public <T> T find(Class<T> type) throws CannotFindConfigException {
    return type.cast(object);
  }

  public void update(Object object) {
    this.object = object;
  }
}
