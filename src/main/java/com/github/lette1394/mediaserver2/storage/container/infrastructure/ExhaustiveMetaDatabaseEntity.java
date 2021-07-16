package com.github.lette1394.mediaserver2.storage.container.infrastructure;


import com.github.lette1394.mediaserver2.storage.container.domain.ExhaustiveMeta;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ExhaustiveMetaDatabaseEntity {
  private final com.github.lette1394.mediaserver2.storage.persistence.domain.Meta objectMeta;
  private final com.github.lette1394.mediaserver2.media.image.domain.Meta imageMeta;
  private final com.github.lette1394.mediaserver2.media.video.domain.Meta videoMeta;

  public ExhaustiveMeta toModel() {
    return null;
  }
}
