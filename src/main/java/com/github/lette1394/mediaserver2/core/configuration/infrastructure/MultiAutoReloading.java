package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

import com.github.lette1394.mediaserver2.core.configuration.domain.AllMultipleResources;
import com.github.lette1394.mediaserver2.core.configuration.domain.AutoReloaded;
import io.vavr.control.Option;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.atomic.AtomicReference;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class MultiAutoReloading implements AllMultipleResources {
  private final AllMultipleResources resources;

  @Override
  public <T> Option<T> find(Class<T> type, String name) {
    return Option
      .of(type.getAnnotation(AutoReloaded.class))
      .flatMap(__ -> autoReloaded(type, name))
      .orElse(() -> resources.find(type, name));
  }

  private <T> Option<T> autoReloaded(Class<T> type, String name) {
    return resources
      .find(type, name)
      .map(resource -> createProxyFor(type, resource, name));
  }

  @SuppressWarnings("unchecked")
  private <T> T createProxyFor(Class<T> type, T loadedResource, String name) {
    return (T) Proxy.newProxyInstance(
      type.getClassLoader(),
      new Class[] { type },
      new AutoReloadHandler<>(resources, type, name, new AtomicReference<>(loadedResource)));
  }

  @RequiredArgsConstructor
  private static class AutoReloadHandler<T> implements InvocationHandler {
    private final AllMultipleResources resources;
    private final Class<T> type;
    private final String name;
    private final AtomicReference<T> fallbackResourceRef;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
      return resources
        .find(type, name)
        .peek(fallbackResourceRef::set)
        .toTry()
        .mapTry(resource -> method.invoke(resource, args))
        .getOrElseTry(() -> method.invoke(fallbackResourceRef.get(), args));
    }
  }
}
