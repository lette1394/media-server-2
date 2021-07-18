package com.github.lette1394.mediaserver2.runner.spring;

import com.github.lette1394.mediaserver2.core.configuration.infrastructure.AllResources;
import com.github.lette1394.mediaserver2.core.domain.Trace;
import com.github.lette1394.mediaserver2.core.domain.TraceFactory;
import com.github.lette1394.mediaserver2.core.infrastructure.DataBufferPayload;
import com.github.lette1394.mediaserver2.core.infrastructure.UuidTraceFactory;
import com.github.lette1394.mediaserver2.storage.container.domain.ExhaustiveMeta;
import com.github.lette1394.mediaserver2.storage.container.domain.ExhaustiveMetaFlusher;
import com.github.lette1394.mediaserver2.storage.hash.domain.Hasher;
import com.github.lette1394.mediaserver2.storage.hash.infrastructure.GuavaHasher;
import com.github.lette1394.mediaserver2.storage.hash.usecase.HashingBinaryPublisher;
import com.github.lette1394.mediaserver2.storage.lock.domain.Locker;
import com.github.lette1394.mediaserver2.storage.lock.domain.Lockers;
import com.github.lette1394.mediaserver2.storage.lock.infrastructure.LoggedLocker;
import com.github.lette1394.mediaserver2.storage.lock.infrastructure.NoOpLocker;
import com.github.lette1394.mediaserver2.storage.persistence.domain.AdaptedBinaryPublisher;
import com.github.lette1394.mediaserver2.storage.persistence.domain.BinaryPublishers;
import com.github.lette1394.mediaserver2.storage.persistence.domain.Meta;
import com.github.lette1394.mediaserver2.storage.persistence.domain.MetaChange;
import com.github.lette1394.mediaserver2.storage.persistence.domain.Uploaders;
import com.github.lette1394.mediaserver2.storage.persistence.infrastructure.DrainingAllBinaries;
import com.github.lette1394.mediaserver2.storage.persistence.infrastructure.LoggedBinaryPublisher;
import com.github.lette1394.mediaserver2.storage.persistence.infrastructure.LoggedMetaChange;
import com.github.lette1394.mediaserver2.storage.persistence.infrastructure.LoggedUploader;
import com.github.lette1394.mediaserver2.storage.persistence.infrastructure.StandardOutputFlusher;
import com.github.lette1394.mediaserver2.storage.persistence.usecase.FlushingUploader;
import com.github.lette1394.mediaserver2.storage.persistence.usecase.LockedUploader;
import com.github.lette1394.mediaserver2.storage.persistence.usecase.ObjectUploader;
import com.github.lette1394.mediaserver2.storage.persistence.usecase.SequentialMetaChange;
import com.google.common.hash.Hashing;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SuppressWarnings("UnnecessaryLocalVariable")
public class BeanConfiguration {
  private final AllResources allResources = new AllResources();
  private final TraceFactory traceFactory = new UuidTraceFactory();

  @Bean
  public ConfigurationApi configurationApi() {
    return new ConfigurationApi(allResources, traceFactory);
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
      final var object = new ObjectUploader<>(metaChange, new DrainingAllBinaries<DataBufferPayload>());
      final var flushed = new FlushingUploader<>(object, metaChange);
      final var locked = new LockedUploader<>(flushed, locker(trace));
      final var logged = new LoggedUploader<>(locked, trace);

      return logged;
    };
  }

  private MetaChange<Meta> metaChange(Trace trace) {
    final var flusher = new StandardOutputFlusher<ExhaustiveMeta>();
    final var exhaustiveFlusher = new ExhaustiveMetaFlusher<Meta>(flusher);

    final var metaChange = new SequentialMetaChange<>(exhaustiveFlusher);
    final var metaLogged = new LoggedMetaChange<>(metaChange, trace);
    return metaLogged;
  }

  @Bean
  public BinaryPublishers<DataBufferPayload> binaryPublishers() {
    return (trace, publisher, length) -> {
      final var adapted = new AdaptedBinaryPublisher<>(publisher, length);
      final var hashing = new HashingBinaryPublisher<>(adapted, hasher());
      final var logged = new LoggedBinaryPublisher<>(hashing, trace);
      return logged;
    };
  }

  private Hasher hasher() {
    return new GuavaHasher(Hashing.sha256().newHasher());
  }
}
