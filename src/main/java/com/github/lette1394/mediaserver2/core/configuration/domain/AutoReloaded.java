package com.github.lette1394.mediaserver2.core.configuration.domain;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


// 제약 조건, java lang Proxy 제약 조건과 동일하다
// 요약: 1. interface, 2. ....
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoReloaded {
}
