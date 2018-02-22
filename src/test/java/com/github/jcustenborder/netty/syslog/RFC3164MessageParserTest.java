/**
 * Copyright © 2018 Jeremy Custenborder (jcustenborder@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.jcustenborder.netty.syslog;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

public class RFC3164MessageParserTest extends MessageParserTest<SyslogMessage, RFC3164MessageParser> {
  private static final Logger log = LoggerFactory.getLogger(RFC3164MessageParserTest.class);

  @Override
  protected void assertMessage(SyslogMessage expected, SyslogMessage actual) {
    MessageAssertions.assertMessage(expected, actual);
  }

  @TestFactory
  public Stream<DynamicTest> parse() {
    final File testsPath = new File("src/test/resources/com/github/jcustenborder/netty/syslog/rfc3164");
    return Arrays.stream(testsPath.listFiles()).map(file -> dynamicTest(file.getName(), () -> {
      final TestCase testCase = this.mapper.readValue(file, TestCase.class);
      List<Object> output = new ArrayList<>();
      parse(output, testCase.input);
      assertFalse(output.isEmpty());
      SyslogMessage actual = (SyslogMessage) output.get(0);
      assertNotNull(actual, "actual should not be null.");
      assertMessage(testCase.expected, actual);
    }));
  }



  @Override
  protected RFC3164MessageParser createParser() {
    return new RFC3164MessageParser();
  }

  public static class TestCase {
    public String input;
    public SyslogMessage expected;
  }
}