package com.github.lette1394.mediaserver2.storage.identification.domain;

import java.util.concurrent.CompletionStage;

@FunctionalInterface
public interface ObjectIdFactory {
  CompletionStage<ObjectId> newObjectId();
}
