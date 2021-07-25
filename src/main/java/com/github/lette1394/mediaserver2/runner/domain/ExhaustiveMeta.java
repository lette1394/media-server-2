package com.github.lette1394.mediaserver2.runner.domain;


import com.github.lette1394.mediaserver2.storage.identification.domain.Id;
import com.github.lette1394.mediaserver2.storage.persistence.domain.Entity;

public record ExhaustiveMeta(
  Id id,
  com.github.lette1394.mediaserver2.storage.persistence.domain.Meta objectMeta,
  com.github.lette1394.mediaserver2.media.image.domain.Meta imageMeta,
  com.github.lette1394.mediaserver2.media.video.domain.Meta videoMeta
) implements Entity {
}
