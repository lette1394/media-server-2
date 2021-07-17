package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

import com.github.lette1394.mediaserver2.core.configuration.domain.AllSingleResources;
import com.github.lette1394.mediaserver2.core.configuration.domain.AutoReload;
import io.vavr.control.Option;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AutoReloadSingleResource implements AllSingleResources {
  private final AllSingleResources resources;

  @Override
  public <T> Option<T> find(Class<T> type) {
    return Option
      .of(type.getAnnotation(AutoReload.class))
      .flatMap(__ -> autoReloaded(type))
      .orElse(() -> resources.find(type));
  }

  private <T> Option<T> autoReloaded(Class<T> type) {
    return resources
      .find(type)
      .map(__ -> createProxyFor(type));
  }

  @SuppressWarnings("unchecked")
  private <T> T createProxyFor(Class<T> type) {
    return (T) Proxy.newProxyInstance(
      type.getClassLoader(),
      new Class[]{type},
      new AutoReloadHandler<>(resources, type));
  }

  @RequiredArgsConstructor
  private static class AutoReloadHandler<T> implements InvocationHandler {
    private final AllSingleResources resources;
    private final Class<T> type;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
      final var resource = resources.find(type);
      if (resource.isDefined()) {
//        System.out.println("proxy invoking: %s".formatted(method.toString()));
        return method.invoke(resource.get(), args);
      }
      throw new CannotInvokeAutoReloadResourceMethod("리소스가 없다");
    }
  }

  private static class CannotInvokeAutoReloadResourceMethod extends RuntimeException {
    public CannotInvokeAutoReloadResourceMethod(String message) {
      super(message);
    }
  }
}
