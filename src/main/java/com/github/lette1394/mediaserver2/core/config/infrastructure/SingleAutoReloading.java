package com.github.lette1394.mediaserver2.core.config.infrastructure;

import com.github.lette1394.mediaserver2.core.config.domain.AllSingleConfigs;
import com.github.lette1394.mediaserver2.core.config.domain.AutoReload;
import io.vavr.control.Option;
import io.vavr.control.Try;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.atomic.AtomicReference;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
final class SingleAutoReloading implements AllSingleConfigs {
  private final AllSingleConfigs resources;

  @Override
  public <T> T find(Class<T> type) {
    return Option
      .of(type.getAnnotation(AutoReload.class))
      .map(__ -> createAutoReload(type, resources.find(type)))
      .getOrElse(() -> resources.find(type));
  }

  @SuppressWarnings("unchecked")
  private <T> T createAutoReload(Class<T> type, T loadedResource) {
    return (T) Proxy.newProxyInstance(
      type.getClassLoader(),
      new Class[]{type},
      new UseLastValidConfigIfReloadingFailed<>(resources, type, new AtomicReference<>(loadedResource)));
  }

  @RequiredArgsConstructor
  private static class UseLastValidConfigIfReloadingFailed<T> implements InvocationHandler {
    private final AllSingleConfigs resources;
    private final Class<T> type;
    private final AtomicReference<T> lastValidConfigs;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
      return Try
        .of(() -> resources.find(type))
        .peek(lastValidConfigs::set)
        .toTry()
        .mapTry(resource -> method.invoke(resource, args))
        .getOrElseTry(() -> method.invoke(lastValidConfigs.get(), args));
    }
  }
}
