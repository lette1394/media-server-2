package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

import com.github.lette1394.mediaserver2.core.configuration.domain.AllMultipleResources;
import com.github.lette1394.mediaserver2.core.configuration.domain.AllSingleResources;
import com.github.lette1394.mediaserver2.core.configuration.domain.Reloader;
import io.vavr.control.Try;
import java.util.List;

public class AutoReloading implements AllResourcesFactory {
  private final AllResourcesFactory factory;

  private final AllSingleResources single;
  private final AllMultipleResources multi;
  private final Reloader reloader;

  private AutoReloading(
    AllResourcesFactory factory,
    AllSingleResources single,
    AllMultipleResources multi,
    Reloader reloader) {

    this.factory = factory;
    this.single = single;
    this.multi = multi;
    this.reloader = reloader;
  }

  Try<AutoReloading> create(AllResourcesFactory factory) {
    return Try.of(() -> {
      final var singleReloading = new SingleReloading(factory::single, factory.single().get());
      final var single = new SingleAutoReloading(singleReloading);

      final var multiReloading = new MultiReloading(factory::multi, factory.multi().get());
      final var multi = new MultiAutoReloading(multiReloading);

      final var reloader = new Sequence(List.of(singleReloading, multiReloading));

      return new AutoReloading(factory, single, multi, reloader);
    });
  }

  @Override
  public Try<AllSingleResources> single() {
    return Try.of(() -> single);
  }

  @Override
  public Try<AllMultipleResources> multi() {
    return Try.of(() -> multi);
  }

  public Reloader reloader() {
    return null;
  }
}
