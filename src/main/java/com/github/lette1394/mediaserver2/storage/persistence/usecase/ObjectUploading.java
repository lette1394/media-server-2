package com.github.lette1394.mediaserver2.storage.persistence.usecase;

import static com.github.lette1394.mediaserver2.core.domain.FluentCompletionStage.start;
import static com.github.lette1394.mediaserver2.storage.hash.usecase.HashingBinaryPublisher.HASHER_ATTRIBUTE;

import com.github.lette1394.mediaserver2.core.domain.Payload;
import com.github.lette1394.mediaserver2.storage.hash.domain.Hash;
import com.github.lette1394.mediaserver2.storage.hash.domain.Hasher;
import com.github.lette1394.mediaserver2.storage.identification.domain.Id;
import com.github.lette1394.mediaserver2.storage.persistence.domain.AllBinaries;
import com.github.lette1394.mediaserver2.storage.persistence.domain.BinaryPublisher;
import com.github.lette1394.mediaserver2.storage.persistence.domain.Meta;
import com.github.lette1394.mediaserver2.storage.persistence.domain.MetaChange;
import com.github.lette1394.mediaserver2.storage.persistence.domain.Timestamp;
import com.github.lette1394.mediaserver2.storage.persistence.domain.Uploading;
import java.util.concurrent.CompletionStage;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ObjectUploading<P extends Payload> implements Uploading<P> {
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
      .getAttribute(HASHER_ATTRIBUTE)
      .map(Hasher::hash)
      .getOrElse(() -> new Hash(""));
  }
}