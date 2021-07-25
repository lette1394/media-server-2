package com.github.lette1394.mediaserver2.core.configuration.domain;


// TODO:
//  1. composed resource, single이랑 multi 각각
//  2. composed resource는 메모리에서 바로 올라간다고 생각해야할 듯
//  3. 테스트, 그러니까 이걸 쓰는 입장에서 먼저 고민해보자
//  -
//  대충 아래와 같은 형식이면 될 거 같은데...
interface SingleComposedResource {
  <T> T register(AllSingleResources single, AllMultipleResources multi);
}

record Multi<T>(T instance, String name) {
}
interface MultiComposedResource {
  <T> Multi<T> register(AllSingleResources single, AllMultipleResources multi);
}

@AutoReloaded
public interface Person {
  boolean isKim();

  String hello();
}
