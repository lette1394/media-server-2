package com.github.lette1394;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;

class AppTest {
  @Test
  void test() {

    assertThat("1 = 1", 1, is(1));
  }
}
