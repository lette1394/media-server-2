package com.github.lette1394.mediaserver2.storage.persistence.usecase;

import com.github.lette1394.mediaserver2.storage.persistence.domain.MetaChange;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionStage;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SequentialMetaChange<T> implements MetaChange<T> {
  private final List<T> forAdd = new ArrayList<>();
  private final List<T> forUpdate = new ArrayList<>();
  private final List<T> forRemove = new ArrayList<>();

  @Override
  public void add(T entity) {
    // FIXME (jaeeun) 2021/07/17: id가 서로 같은 entity가
    //  중복으로 여러개가 들어오면 어떻게 하지?
    forAdd.add(entity);
  }

  @Override
  public void update(T entity) {
    forUpdate.add(entity);
  }

  @Override
  public void remove(T entity) {
    forRemove.add(entity);
  }

  @Override
  public CompletionStage<Void> flush() {


    return null;
  }
}
