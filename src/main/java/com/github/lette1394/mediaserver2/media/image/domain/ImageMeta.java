package com.github.lette1394.mediaserver2.media.image.domain;

import com.github.lette1394.mediaserver2.storage.identification.domain.Id;
import com.github.lette1394.mediaserver2.storage.persistence.domain.Meta;

public record ImageMeta(
  Id id,
  long width,
  long height,
  Format format) implements Meta {
}
