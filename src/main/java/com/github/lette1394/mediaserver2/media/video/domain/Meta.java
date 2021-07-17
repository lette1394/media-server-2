package com.github.lette1394.mediaserver2.media.video.domain;

import com.github.lette1394.mediaserver2.storage.identification.domain.Id;
import com.github.lette1394.mediaserver2.storage.persistence.domain.Entity;
import java.time.Duration;

public record Meta(
  Id id,
  long width,
  long height,
  Duration duration,
  Format format) implements Entity {

}
