package com.github.lette1394.mediaserver2.storage.persistence.usecase;

import static com.github.lette1394.mediaserver2.core.fluency.domain.FluentCompletionStage.start;
import static com.github.lette1394.mediaserver2.storage.hash.usecase.HashingBinaryPublisher.HASHER_ATTRIBUTE;

import com.github.lette1394.mediaserver2.core.stream.domain.Payload;
import com.github.lette1394.mediaserver2.storage.hash.domain.Hash;
import com.github.lette1394.mediaserver2.storage.hash.domain.Hasher;
import com.github.lette1394.mediaserver2.storage.identification.domain.Id;
import com.github.lette1394.mediaserver2.storage.persistence.domain.AllBinaries;
import com.github.lette1394.mediaserver2.core.stream.domain.BinaryPublisher;
import com.github.lette1394.mediaserver2.storage.persistence.domain.Meta;
import com.github.lette1394.mediaserver2.storage.persistence.domain.MetaChange;
import com.github.lette1394.mediaserver2.storage.persistence.domain.Timestamp;
import com.github.lette1394.mediaserver2.storage.persistence.domain.Uploader;
import com.github.lette1394.mediaserver2.storage.persistence.domain.UploadingCommand;
import java.util.concurrent.CompletionStage;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ObjectUploader<P extends Payload> implements Uploader<P> {
  private final MetaChange<Meta> metaChange;
  private final AllBinaries<P> allBinaries;

  @Override
  public CompletionStage<Void> upload(UploadingCommand<P> command) {
    final var id = command.id();
    final var publisher = command.binaryPublisher();

    return start()
      .thenCompose(__ -> allBinaries.save(id, publisher))
      .thenRun(() -> metaChange.add(meta(id, publisher)));
  }

  private Meta meta(Id id, BinaryPublisher<P> publisher) {
    final var hashCode = hashCode(publisher);
    final var timestamp = Timestamp.now();
    final var size = publisher.length();

    return new Meta(id, size, timestamp, hashCode);
  }

  private Hash hashCode(BinaryPublisher<P> publisher) {
    return publisher
      .attributes()
      .get(HASHER_ATTRIBUTE)
      .map(Hasher::hash)
      .getOrNull();
  }
}
