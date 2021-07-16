package com.github.lette1394.mediaserver2.media.video.domain;

import java.time.Duration;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Meta {
  private final long width;
  private final long height;
  private final Duration duration;
  private final Format format;
}
