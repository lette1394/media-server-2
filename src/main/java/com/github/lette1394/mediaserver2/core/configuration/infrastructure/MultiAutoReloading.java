package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

import com.github.lette1394.mediaserver2.core.configuration.domain.AllMultipleResources;
import com.github.lette1394.mediaserver2.core.configuration.domain.AutoReloaded;
import io.vavr.control.Option;
import io.vavr.control.Try;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.atomic.AtomicReference;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
final class MultiAutoReloading implements AllMultipleResources {
  private final AllMultipleResources resources;

  @Override
  public <T> T find(Class<T> type, String name) {
    return Option
      .of(type.getAnnotation(AutoReloaded.class))
      .map(__ -> autoReloaded(type, name))
      .getOrElse(() -> resources.find(type, name));
  }

  @SuppressWarnings("unchecked")
  private <T> T autoReloaded(Class<T> type, String name) {
    final var loadedResource = (T) resources.find(type, name);
    return (T) Proxy.newProxyInstance(
      type.getClassLoader(),
      new Class[]{type},
      new UseLastSucceedResourceIfReloadingFailedInvocationHandler<>(resources, type, name, new AtomicReference<>(loadedResource)));
  }

  @RequiredArgsConstructor
  private static class UseLastSucceedResourceIfReloadingFailedInvocationHandler<T> implements InvocationHandler {
    private final AllMultipleResources resources;
    private final Class<T> type;
    private final String name;
    private final AtomicReference<T> lastSucceedResourceReference;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
      return Try
        .of(() -> resources.find(type, name))
        .peek(lastSucceedResourceReference::set)
        .toTry()
        .mapTry(resource -> method.invoke(resource, args))
        .getOrElseTry(() -> method.invoke(lastSucceedResourceReference.get(), args));
    }
  }
}
