package com.github.lette1394.mediaserver2.core.resource.infra;

import static com.github.lette1394.mediaserver2.core.fluency.domain.Contracts.requires;

import com.github.lette1394.mediaserver2.core.resource.domain.Resource;
import io.vavr.control.Option;
import java.io.FileNotFoundException;
import org.apache.commons.io.IOUtils;

public class FileResource implements Resource {
  private final byte[] contents;

  public FileResource(String path) {
    requires(path.startsWith("/"), IllegalArgumentException::new);
    this.contents = getBytes(path);
  }

  private static byte[] getBytes(String path) {
    return Option
      .of(FileResource.class.getResourceAsStream(path))
      .toTry(() -> new FileNotFoundException("not found: [%s]".formatted(path)))
      .mapTry(IOUtils::toByteArray)
      .get();
  }

  @Override
  public byte[] contents() {
    return contents;
  }
}
