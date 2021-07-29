package com.github.lette1394.mediaserver2.core.config.infrastructure;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.github.lette1394.mediaserver2.core.config.domain.TypeAlias;
import io.vavr.control.Option;
import java.util.Map;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

@JsonTypeInfo(use = Id.NAME, property = "type")
@JsonSubTypes(value = {
  @Type(value = SingleRegion.class, name = "single"),
  @Type(value = MultiRegion.class, name = "multi")
})
@TypeAlias(Endpoint.class)
@MultiFileResource(directoryPath = "/polymorphism")
public abstract class EndpointConfig implements Endpoint {

}

@Builder
@Jacksonized
class SingleRegion extends EndpointConfig {
  String endpoint;

  @Override
  public String endpoint(Region region) {
    return endpoint;
  }
}

@Builder
@Jacksonized
class MultiRegion extends EndpointConfig {
  Map<Region, String> endpoints;
  Region fallback;

  @Override
  public String endpoint(Region region) {
    return Option
      .of(endpoints.get(region))
      .getOrElse(endpoints.get(fallback));
  }
}