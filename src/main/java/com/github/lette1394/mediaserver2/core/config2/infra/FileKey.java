package com.github.lette1394.mediaserver2.core.config2.infra;

import com.github.lette1394.mediaserver2.core.config2.domain.Key;
import java.nio.file.Path;

record FileKey(Class<?> type, Path path) implements Key {
}
