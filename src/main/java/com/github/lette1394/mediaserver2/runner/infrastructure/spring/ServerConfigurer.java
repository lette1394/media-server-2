package com.github.lette1394.mediaserver2.runner.infrastructure.spring;

import com.github.lette1394.mediaserver2.core.trace.domain.Trace;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.web.reactive.BindingContext;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolver;
import org.springframework.web.reactive.result.method.annotation.ArgumentResolverConfigurer;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Configuration
public class ServerConfigurer implements WebFluxConfigurer {
  @Override
  public void configureArgumentResolvers(ArgumentResolverConfigurer configurer) {
    configurer.addCustomResolver(new HandlerMethodArgumentResolver() {
      @Override
      public boolean supportsParameter(MethodParameter parameter) {
        return Trace.class.isAssignableFrom(parameter.getParameterType());
      }

      @Override
      public Mono<Object> resolveArgument(
        MethodParameter parameter,
        BindingContext bindingContext,
        ServerWebExchange exchange) {

        return Mono.deferContextual(context -> Mono.just(context.get("TRACE")));
      }
    });
  }
}
