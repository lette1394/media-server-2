package com.github.lette1394.mediaserver2.storage.container.domain;

import static java.util.stream.Collectors.toList;

import com.github.lette1394.mediaserver2.storage.persistence.domain.Flusher;
import com.github.lette1394.mediaserver2.storage.persistence.domain.Meta;
import io.vavr.control.Option;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MergedFlusher implements Flusher {
  private final Flusher flusher;

  @Override
  public <T> CompletionStage<Void> flush(List<T> entities) {

//    API.For()

    // 같은 id로 mapping -> map<id, entity>
//    entities.stream()
//      .filter(entity -> entity instanceof )
//      .collect(Collectors.toMap(t -> ))

    // id가 똑같으면 ExhaustiveMeta 하나로 합친다
    // 애초에 MetaChange 에서 동일한 id를 가진 두 개 이상의 entity를 가져서는 안된다
    

    entities.stream().collect(Collectors.toMap(t -> t.getClass(), )) // id를 가져오는 interface가 필요할라나?

    final var exhaustiveMeta = new ExhaustiveMeta(
      cast(entities, Meta.class),
      cast(entities, com.github.lette1394.mediaserver2.media.image.domain.Meta.class),
      cast(entities, com.github.lette1394.mediaserver2.media.video.domain.Meta.class)
    );
    return flusher.flush(List.of(exhaustiveMeta)); // map이라서 두 개 이상 있을 수 있다
  }

  @Nullable
  @SuppressWarnings("unchecked")
  private <T, META> META cast(List<T> entities, Class<META> metaClass) {
    return entities
      .stream()
      .filter(metaClass::isInstance)
      .map(entity -> (META) entity)
      .collect(toList())
      .get(0);
  }
}
