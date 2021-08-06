package com.github.lette1394.mediaserver2.runner.infrastructure.spring;

import static com.github.lette1394.mediaserver2.core.fluency.domain.Contracts.requires;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.github.lette1394.mediaserver2.core.config.infrastructure.AllResources;
import com.github.lette1394.mediaserver2.core.stream.domain.AdaptedBinaryPublisher;
import com.github.lette1394.mediaserver2.core.stream.domain.Attributes;
import com.github.lette1394.mediaserver2.core.stream.domain.BinaryPublishers;
import com.github.lette1394.mediaserver2.core.stream.infrastructure.DataBufferPayload;
import com.github.lette1394.mediaserver2.core.trace.domain.Trace;
import com.github.lette1394.mediaserver2.core.trace.domain.TraceFactory;
import com.github.lette1394.mediaserver2.core.trace.infra.UuidTraceFactory;
import com.github.lette1394.mediaserver2.runner.domain.ExhaustiveMeta;
import com.github.lette1394.mediaserver2.runner.domain.ExhaustiveMetaFlusher;
import com.github.lette1394.mediaserver2.storage.hash.domain.Hasher;
import com.github.lette1394.mediaserver2.storage.hash.infrastructure.GuavaHasher;
import com.github.lette1394.mediaserver2.storage.hash.usecase.HashingBinaryPublisher;
import com.github.lette1394.mediaserver2.storage.lock.domain.Locker;
import com.github.lette1394.mediaserver2.storage.lock.domain.Lockers;
import com.github.lette1394.mediaserver2.storage.lock.infrastructure.LoggedLocker;
import com.github.lette1394.mediaserver2.storage.lock.infrastructure.NoOpLocker;
import com.github.lette1394.mediaserver2.storage.persistence.domain.MetaChanges;
import com.github.lette1394.mediaserver2.storage.persistence.domain.ObjectMeta;
import com.github.lette1394.mediaserver2.storage.persistence.domain.Uploaders;
import com.github.lette1394.mediaserver2.storage.persistence.infrastructure.DrainingAllBinaries;
import com.github.lette1394.mediaserver2.storage.persistence.infrastructure.LoggedBinaryPublisher;
import com.github.lette1394.mediaserver2.storage.persistence.infrastructure.LoggedMetaChanges;
import com.github.lette1394.mediaserver2.storage.persistence.infrastructure.LoggedUploader;
import com.github.lette1394.mediaserver2.storage.persistence.infrastructure.StandardOutputFlusher;
import com.github.lette1394.mediaserver2.storage.persistence.usecase.FlushingUploader;
import com.github.lette1394.mediaserver2.storage.persistence.usecase.LockedUploader;
import com.github.lette1394.mediaserver2.storage.persistence.usecase.ObjectUploader;
import com.github.lette1394.mediaserver2.storage.persistence.usecase.SequentialMetaChanges;
import com.google.common.hash.Hashing;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.util.AttributeKey;
import java.net.SocketAddress;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.boot.web.embedded.netty.NettyServerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.netty.ConnectionObserver.State;
import reactor.netty.http.server.HttpServer;

@Configuration
@SuppressWarnings("UnnecessaryLocalVariable")
public class BeanConfiguration {
  private static final AttributeKey<Trace> TRACE_ATTRIBUTE_KEY = AttributeKey
    .valueOf(Trace.class, "TRACE");
  private final AllResources allResources = new AllResources(
    "/plugins",
    "com.github.lette1394.mediaserver2",
    configurationObjectMapper());
  private final TraceFactory traceFactory = new UuidTraceFactory();

  @Bean
  public ConfigurationApi configurationApi() {
    return new ConfigurationApi(allResources);
  }

  @Bean
  public PersistenceApi persistenceApi() {
    return new PersistenceApi(traceFactory, uploaders(), binaryPublishers());
  }

  @Bean
  public Lockers lockers() {
    return this::locker;
  }

  public Locker locker(Trace trace) {
    final var noOp = new NoOpLocker();
    final var logged = new LoggedLocker(noOp, trace);
    return logged;
  }

  @Bean
  public Uploaders<DataBufferPayload> uploaders() {
    return trace -> {
      final var metaChange = metaChange(trace);
      final var object = new ObjectUploader<>(metaChange,
        new DrainingAllBinaries<DataBufferPayload>());
      final var flushed = new FlushingUploader<>(object, metaChange);
      final var locked = new LockedUploader<>(flushed, locker(trace));
      final var logged = new LoggedUploader<>(locked, trace);

      return logged;
    };
  }

  private MetaChanges<ObjectMeta> metaChange(Trace trace) {
    final var flusher = new StandardOutputFlusher<ExhaustiveMeta>();
    final var exhaustiveFlusher = new ExhaustiveMetaFlusher<ObjectMeta>(flusher);

    final var metaChange = new SequentialMetaChanges<>(exhaustiveFlusher);
    final var metaLogged = new LoggedMetaChanges<>(metaChange, trace);
    return metaLogged;
  }

  @Bean
  public BinaryPublishers<DataBufferPayload> binaryPublishers() {
    return (trace, publisher, length) -> {
      final var adapted = new AdaptedBinaryPublisher<>(publisher, Attributes.createEmpty(), length);
      final var hashing = new HashingBinaryPublisher<>(adapted, hasher());
      final var logged = new LoggedBinaryPublisher<>(hashing, trace);
      return logged;
    };
  }

  private Hasher hasher() {
    return new GuavaHasher(Hashing.sha256().newHasher());
  }

  private ObjectMapper configurationObjectMapper() {
    return new ObjectMapper(new YAMLFactory())
      .enable(DeserializationFeature.WRAP_EXCEPTIONS)
      .enable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)
      .enable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES)
      .enable(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)
      .enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
      .enable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS)
      .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
      .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
      .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_VALUES)
      .setPropertyNamingStrategy(PropertyNamingStrategies.KEBAB_CASE);
  }

  @Bean
  public NettyReactiveWebServerFactory nettyReactiveWebServerFactory() {
    NettyReactiveWebServerFactory webServerFactory = new NettyReactiveWebServerFactory();
    webServerFactory.addServerCustomizers(new Custom(traceFactory));
    return webServerFactory;
  }

  @RequiredArgsConstructor
  static class Prefix extends TraceFactory {
    private final TraceFactory traceFactory;
    private final String prefix;

    @Override
    public Trace newTrace() {
      return traceConstructor("%s____%s".formatted(prefix, traceFactory.newTrace().toString()));
    }
  }

  @RequiredArgsConstructor
  static class Custom implements NettyServerCustomizer {
    private final TraceFactory traceFactory;

    @Override
    public HttpServer apply(HttpServer httpServer) {
      final var httpServer1 = httpServer
        .doOnChannelInit((connectionObserver, channel, remoteAddress) -> {
          final var channelId = channel.id().asShortText();
          final var factory = new Prefix(traceFactory, channelId);

          channel.pipeline()
            .addFirst(new TraceHandler(factory.newTrace()), new CustomLoggingHandler());
        })
        .mapHandle((voidMono, connection) -> {
          final var trace = connection.channel().attr(TRACE_ATTRIBUTE_KEY).get();
          requires(trace != null, "trace != null");
          return voidMono.contextWrite(context -> context.put("TRACE", trace));
        })
        .childObserve((connection, newState) -> {
          if (newState == State.CONFIGURED) {
            final var oldTrace = connection.channel().attr(TRACE_ATTRIBUTE_KEY).get();
            final var factory = new Prefix(traceFactory, connection.channel().id().asShortText());
            connection.channel().attr(TRACE_ATTRIBUTE_KEY).compareAndSet(oldTrace, factory.newTrace());
          }
        });
      httpServer1.warmup().block();
      return httpServer1;
    }
  }

  @RequiredArgsConstructor
  static class TraceHandler extends ChannelInboundHandlerAdapter {
    private final Trace trace;

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
      ctx.channel().attr(TRACE_ATTRIBUTE_KEY).set(trace);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
      super.channelActive(ctx);
    }
  }

  @Slf4j
  static class CustomLoggingHandler extends ChannelDuplexHandler {
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
      log.info("{} > channelRegistered", trace(ctx));
      ctx.fireChannelRegistered();
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
      log.info("{} > channelUnregistered", trace(ctx));
      ctx.fireChannelUnregistered();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
      log.info("{} > client >>> server channelActive", trace(ctx));
      ctx.fireChannelActive();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
      log.info("{} > channelInactive", trace(ctx));
      ctx.fireChannelInactive();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
      log.info("{} > exceptionCaught, {}", trace(ctx), cause);
      ctx.fireExceptionCaught(cause);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
      log.info("{} > userEventTriggered, {}", trace(ctx), evt);
      ctx.fireUserEventTriggered(evt);
    }

    @Override
    public void bind(ChannelHandlerContext ctx, SocketAddress localAddress, ChannelPromise promise) throws Exception {
      log.info("{}> bind {}", trace(ctx), localAddress);
      ctx.bind(localAddress, promise);
    }

    @Override
    public void connect(
      ChannelHandlerContext ctx,
      SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise)
      throws Exception {
      log.info("{} > connect, {}", trace(ctx), remoteAddress);
      ctx.connect(remoteAddress, localAddress, promise);
    }

    @Override
    public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
      log.info("{} > disconnect", trace(ctx));
      ctx.disconnect(promise);
    }

    @Override
    public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
      log.info("{} > close", trace(ctx));
      ctx.close(promise);
    }

    @Override
    public void deregister(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
      ctx.deregister(promise);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
      log.info("{} > client <<< server write", trace(ctx));
      super.write(ctx, msg, promise);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
      log.info("{} > client >>> server channelRead", trace(ctx));
      super.channelRead(ctx, msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
      log.info("{} > client >>> server channelReadComplete", trace(ctx));
      super.channelReadComplete(ctx);
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
      log.info("{} > channelWritabilityChanged", trace(ctx));
      super.channelWritabilityChanged(ctx);
    }

    @Override
    public void flush(ChannelHandlerContext ctx) throws Exception {
      log.info("{}> client <<< server flush", trace(ctx));
      ctx.flush();
    }

    private Trace trace(ChannelHandlerContext ctx) {
      final var trace = ctx.channel().attr(TRACE_ATTRIBUTE_KEY).get();
      requires(trace != null, "trace != null");
      return trace;
    }
  }
}
