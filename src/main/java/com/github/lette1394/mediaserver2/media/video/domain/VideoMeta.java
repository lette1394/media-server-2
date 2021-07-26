package com.github.lette1394.mediaserver2.media.video.domain;

import com.github.lette1394.mediaserver2.storage.identification.domain.Id;
import com.github.lette1394.mediaserver2.storage.persistence.domain.Meta;
import java.time.Duration;

public record VideoMeta(
  Id id,
  long width,
  long height,
  Duration duration,
  Format format) implements Meta {
}
