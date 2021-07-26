package com.github.lette1394.mediaserver2.runner.infrastructure;


import com.github.lette1394.mediaserver2.media.image.domain.ImageMeta;
import com.github.lette1394.mediaserver2.media.video.domain.VideoMeta;
import com.github.lette1394.mediaserver2.runner.domain.ExhaustiveMeta;
import com.github.lette1394.mediaserver2.storage.persistence.domain.ObjectMeta;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ExhaustiveMetaDatabaseEntity {
  private final ObjectMeta objectMeta;
  private final ImageMeta imageMeta;
  private final VideoMeta videoMeta;

  public ExhaustiveMeta toModel() {
    return null;
  }
}
