package com.github.lette1394.mediaserver2.core.config2.domain;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
@Config
@ConfigAlias(Phone.class)
@ConfigLocation("/core/config2/phone.yaml")
public class PhoneConfig_WrongInterface {
  String country;
  String mobile;
}
