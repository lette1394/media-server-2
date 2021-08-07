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
public class PhoneConfig implements Phone {
  String country;
  String mobile;

  public PhoneConfig(String country, String mobile) {
    this.country = country;
    this.mobile = mobile;
  }

  @Override
  public String call() {
    return "%s %s".formatted(country, mobile.substring(1));
  }
}
