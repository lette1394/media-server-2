package com.github.lette1394.mediaserver2.core.config.infrastructure;

import com.github.lette1394.mediaserver2.core.config.domain.AllSingleResources;
import com.github.lette1394.mediaserver2.core.config.domain.AutoReloaded;
import io.vavr.control.Option;
import io.vavr.control.Try;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.atomic.AtomicReference;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
final class SingleAutoReloading implements AllSingleResources {
  private final AllSingleResources resources;

  @Override
  public <T> T find(Class<T> type) {
    return Option
      .of(type.getAnnotation(AutoReloaded.class))
      .map(__ -> autoReloaded(type, resources.find(type)))
      .getOrElse(() -> resources.find(type));
  }

  @SuppressWarnings("unchecked")
  private <T> T autoReloaded(Class<T> type, T loadedResource) {
    return (T) Proxy.newProxyInstance(
      type.getClassLoader(),
      new Class[]{type},
      new UseLastSucceedResourceIfReloadingFailedInvocationHandler<>(resources, type, new AtomicReference<>(loadedResource)));
  }

  @RequiredArgsConstructor
  private static class UseLastSucceedResourceIfReloadingFailedInvocationHandler<T> implements InvocationHandler {
    private final AllSingleResources resources;
    private final Class<T> type;
    private final AtomicReference<T> lastSucceedResourceReference;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
      return Try
        .of(() -> resources.find(type))
        .peek(lastSucceedResourceReference::set)
        .toTry()
        .mapTry(resource -> method.invoke(resource, args))
        .getOrElseTry(() -> method.invoke(lastSucceedResourceReference.get(), args));
    }
  }
}
