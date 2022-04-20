/*
 * Copyright (C) 2014 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.squareup.pagerduty.incidents;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public final class EventTest {
  private final Gson gson = new Gson();

  @Test public void serialization() {
    Event event = new Event("e93facc04764012d7bfb002500d5d1a6", "srv01/HTTP", "trigger",
        "FAILURE for production/HTTP on machine srv01.acme.com", "Sample Monitoring Service",
        "https://monitoring.service.com", TestUtil.map("ping time", "1500ms", "load avg", "0.75"));
    String actual = gson.toJson(event);

    String expected = ""
        + "{"
        + "\"service_key\":\"e93facc04764012d7bfb002500d5d1a6\","
        + "\"incident_key\":\"srv01/HTTP\","
        + "\"event_type\":\"trigger\","
        + "\"description\":\"FAILURE for production/HTTP on machine srv01.acme.com\","
        + "\"client\":\"Sample Monitoring Service\","
        + "\"client_url\":\"https://monitoring.service.com\","
        + "\"details\":{"
        + "\"ping time\":\"1500ms\","
        + "\"load avg\":\"0.75\""
        + "}"
        + "}";
    JsonParser parser = new JsonParser();
    assertThat(parser.parse(actual)).isEqualTo(parser.parse(expected));
  }
}
