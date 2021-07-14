package com.github.lette1394.mediaserver2.storage.usecase;

import com.github.lette1394.mediaserver2.core.infrastructure.DataBufferPayload;
import com.github.lette1394.mediaserver2.core.infrastructure.UuidTraceFactory;
import com.github.lette1394.mediaserver2.storage.persistence.domain.BinaryPublisher;
import com.github.lette1394.mediaserver2.storage.domain.BrokenBinaryPublisher;
import com.github.lette1394.mediaserver2.storage.identification.domain.Id;
import com.github.lette1394.mediaserver2.storage.persistence.usecase.UploadingCommand;
import io.netty.buffer.ByteBufAllocator;
import java.nio.file.Paths;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.NettyDataBufferFactory;

class UploadingTest {

  @Test
  @SneakyThrows
  @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
  void test1() {
    final var useCases = new TestUseCases();
    final var traceFactory = new UuidTraceFactory();

    final var path = Paths.get(getClass().getResource("/hello-world.txt").toURI());
    final var publisher = DataBufferUtils
      .read(path, new NettyDataBufferFactory(ByteBufAllocator.DEFAULT), 1)
      .map(DataBufferPayload::new);

    final var binaryPublisher = BinaryPublisher.adapt(
      path.toFile().length(),
      publisher);

    final var brokenPublisher = new BrokenBinaryPublisher<>(binaryPublisher, 11);

    useCases
      .uploading(traceFactory.create())
      .upload(new UploadingCommand<>(new Id(), brokenPublisher))
      .toCompletableFuture()
      .exceptionally(throwable -> null)
      .join();
  }
}
