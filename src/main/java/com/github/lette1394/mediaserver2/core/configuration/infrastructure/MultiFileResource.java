package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MultiFileResource {
  String directoryPath();

  ResourceType resourceType() default ResourceType.YAML;
}
