package com.github.lette1394.mediaserver2.storage.identification.infrastructure.service;

import com.github.lette1394.mediaserver2.storage.auth.domain.Authorizer;
import com.github.lette1394.mediaserver2.storage.auth.domain.Permission;
import com.github.lette1394.mediaserver2.storage.auth.infrastructure.Talk_M_Credential;
import com.github.lette1394.mediaserver2.storage.identification.domain.ObjectId;
import com.github.lette1394.mediaserver2.storage.identification.domain.ObjectIdFactory;
import java.util.concurrent.CompletionStage;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class TalkM implements ObjectIdFactory {
  private final Authorizer<Talk_M_Credential> authorizer;

  @Override
  public CompletionStage<ObjectId> newObjectId() {
    return authorizer
      .authorize(Permission.WRITE)
      .thenApply(Talk_M_Credential::objectId)
      .thenApply(ObjectId::new);
  }
}
