package com.github.lette1394.mediaserver2.storage.persistence.usecase;

import com.github.lette1394.mediaserver2.storage.persistence.domain.Flusher;
import com.github.lette1394.mediaserver2.storage.persistence.domain.MetaChange;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionStage;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SequentialMetaChange<T> implements MetaChange<T> {
  private final Flusher flusher;

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
    // FIXME (jaeeun) 2021/07/17:
    //  이것도 add랑 마찬가지.
    //  "그냥 덮어쓰면 되는거 아니냐?" 라고 할 수 있겠지만, 애초에 그건 entity의 상태를 변경하는 우회적인 방법이다
    //  이런 일이 발생한다는 건 같은 meta를 다루는 caller side 코드가 응집력이 떨어진다는 소리
    //  MetaChange 인터페이스에서는 이를 허용하지 않는다.
    forUpdate.add(entity);
  }

  @Override
  public void remove(T entity) {
    forRemove.add(entity);
  }

  @Override
  public CompletionStage<Void> flush() {
    flusher.flush(forAdd);
    flusher.flush(forUpdate);
    flusher.flush(forRemove);

    // db 연산
    return null;
  }
}
