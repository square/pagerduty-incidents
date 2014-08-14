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
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public final class NotifyResultTest {
  private final Gson gson = new Gson();

  @Test public void successDeserialization() {
    String json = ""
        + "{\n"
        + "  \"status\": \"success\",\n"
        + "  \"incident_key\": \"123456\",\n"
        + "  \"message\": \"Event processed\"\n"
        + "}";
    NotifyResult result = gson.fromJson(json, NotifyResult.class);
    assertThat(result.status()).isEqualTo("success");
    assertThat(result.incidentKey()).isEqualTo("123456");
    assertThat(result.message()).isEqualTo("Event processed");
  }

  @Test public void badRequestDeserialization() {
    String json = ""
        + "{\n"
        + "  \"status\": \"invalid event\",\n"
        + "  \"incident_key\": \"123456\",\n"
        + "  \"message\": \"Incident key not found.\"\n"
        + "}";
    NotifyResult result = gson.fromJson(json, NotifyResult.class);
    assertThat(result.status()).isEqualTo("invalid event");
    assertThat(result.incidentKey()).isEqualTo("123456");
    assertThat(result.message()).isEqualTo("Incident key not found.");
  }
}
