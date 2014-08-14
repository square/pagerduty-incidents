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

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

public final class TriggerTest {
  @Test public void triggerDescriptionRequired() {
    try {
      new Trigger.Builder(null);
      fail();
    } catch (NullPointerException e) {
      assertThat(e).hasMessage("description");
    }
    try {
      new Trigger.Builder("  ");
      fail();
    } catch (IllegalArgumentException e) {
      assertThat(e).hasMessage("'description' must not be blank. Was: '  '");
    }
  }

  @Test public void triggerDescriptionLength() {
    StringBuilder builder = new StringBuilder(1025);
    for (int i = 0; i < 1025; i++) {
      builder.append(Integer.toString(i % 10));
    }
    try {
      new Trigger.Builder(builder.toString());
      fail();
    } catch (IllegalArgumentException e) {
      assertThat(e).hasMessage("'description' length must be 1024 or less. Was: 1025");
    }
  }

  @Test public void triggerKeyRequiredIfSpecified() {
    try {
      new Trigger.Builder("Hi").withIncidentKey(null);
      fail();
    } catch (NullPointerException e) {
      assertThat(e).hasMessage("incidentKey");
    }
    try {
      new Trigger.Builder("Hi").withIncidentKey("  ");
      fail();
    } catch (IllegalArgumentException e) {
      assertThat(e).hasMessage("'incidentKey' must not be blank. Was: '  '");
    }
  }

  @Test public void triggerDetailsNameRequiredIfSpecified() {
    try {
      new Trigger.Builder("Hi").addDetails(null, "Hi");
      fail();
    } catch (NullPointerException e) {
      assertThat(e).hasMessage("name");
    }
    try {
      new Trigger.Builder("Hi").addDetails("  ", "Hi");
      fail();
    } catch (IllegalArgumentException e) {
      assertThat(e).hasMessage("'name' must not be blank. Was: '  '");
    }
  }

  @Test public void triggerDetailsRequiredIfSpecified() {
    try {
      new Trigger.Builder("Hi").addDetails(null);
      fail();
    } catch (NullPointerException e) {
      assertThat(e).hasMessage("details");
    }
  }
}
