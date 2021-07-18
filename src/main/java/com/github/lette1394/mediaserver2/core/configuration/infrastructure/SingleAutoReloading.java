package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

import com.github.lette1394.mediaserver2.core.configuration.domain.AllSingleResources;
import com.github.lette1394.mediaserver2.core.configuration.domain.AutoReloaded;
import io.vavr.control.Option;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.atomic.AtomicReference;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class SingleAutoReloading implements AllSingleResources {
  private final AllSingleResources resources;

  @Override
  public <T> Option<T> find(Class<T> type) {
    return Option
      .of(type.getAnnotation(AutoReloaded.class))
      .flatMap(__ -> autoReloaded(type))
      .orElse(() -> resources.find(type));
  }

  private <T> Option<T> autoReloaded(Class<T> type) {
    return resources
      .find(type)
      .map(resource -> createProxyFor(type, resource));
  }

  @SuppressWarnings("unchecked")
  private <T> T createProxyFor(Class<T> type, T loadedResource) {
    return (T) Proxy.newProxyInstance(
      type.getClassLoader(),
      new Class[] { type },
      new AutoReloadHandler<>(resources, type, new AtomicReference<>(loadedResource)));
  }

  @RequiredArgsConstructor
  private static class AutoReloadHandler<T> implements InvocationHandler {
    private final AllSingleResources resources;
    private final Class<T> type;
    private final AtomicReference<T> fallbackResourceRef;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
      return resources
        .find(type)
        .peek(fallbackResourceRef::set)
        .toTry()
        .mapTry(resource -> method.invoke(resource, args))
        .getOrElseTry(() -> method.invoke(fallbackResourceRef.get(), args));
    }
  }
}
