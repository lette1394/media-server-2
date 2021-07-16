package com.github.lette1394.mediaserver2.storage.container.domain;


import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ExhaustiveMeta {
  private final com.github.lette1394.mediaserver2.storage.persistence.domain.Meta objectMeta;
  private final com.github.lette1394.mediaserver2.media.image.domain.Meta imageMeta;
  private final com.github.lette1394.mediaserver2.media.video.domain.Meta videoMeta;

  
}
