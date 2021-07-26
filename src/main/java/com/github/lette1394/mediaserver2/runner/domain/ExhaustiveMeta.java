package com.github.lette1394.mediaserver2.runner.domain;


import com.github.lette1394.mediaserver2.media.image.domain.ImageMeta;
import com.github.lette1394.mediaserver2.media.video.domain.VideoMeta;
import com.github.lette1394.mediaserver2.storage.identification.domain.Id;
import com.github.lette1394.mediaserver2.storage.persistence.domain.Meta;
import com.github.lette1394.mediaserver2.storage.persistence.domain.ObjectMeta;

public record ExhaustiveMeta(
  Id id,
  ObjectMeta objectMeta,
  ImageMeta imageMeta,
  VideoMeta videoMeta
) implements Meta {
}
