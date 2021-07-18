package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.github.lette1394.mediaserver2.core.configuration.domain.AllSingleResources;
import com.github.lette1394.mediaserver2.core.configuration.domain.Person;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
  public static void main(String[] args) {
    final var resourcesFactory = new ResourcesFactory();
    final var single = resourcesFactory.singleResources();

    final var people = single.find(Person.class).get();
    single.find(Person.class).get();
    single.find(Person.class).get();
    single.find(Person.class).get();
    single.find(Person.class).get();
    single.find(Person.class).get();

    // 그니까 reload를 지원하기 위해서는 뭐가 필요하냐면
    //  1. @SingleResource, @MultipleResource를 전체 스캐닝하는 scanner
    //    이건 기존 scanner 구현을 재사용 할 수 있을 듯. annotation만 변경해서.
    //  2. WarmUpLoader : 1.에서 만든 스캐너를 통해서 한 번씩 미리 호출해두는거
    //  3. Reloader: reload(). 때리면
    //    WarmUpLoader까지 합성된 loader를 만드는 팩토리를 통해서 새로운 resource loader 생성
    //    이후 자기가 들고 있는 atomic reference 갈아끼우기. 그러면 끝.
    //  더 생각해 볼 것: GC에 문제없나? 특히 auto reloaded 설정들.

//    final var executor = Executors.newScheduledThreadPool(4);
//    final var evictor = Executors.newScheduledThreadPool(1);
//    evictor.scheduleWithFixedDelay(() -> {
//      cached.updateWith();
//    }, 0, 1000, TimeUnit.MILLISECONDS);
//
//    executor.scheduleWithFixedDelay(() -> {
//      System.out.println(people.isKim());
//      System.out.println(people.hello());
//    }, 0, 1, TimeUnit.MILLISECONDS);
  }
}
