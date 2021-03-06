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

import java.io.IOException;

public final class PagerDutyIT {
  private static final String API_KEY = "YOUR API KEY";

  public static void main(String... args) throws IOException {
    PagerDuty pagerDuty = PagerDuty.create(API_KEY);

    NotifyResult trigger1 = pagerDuty.notify(new Trigger.Builder("Client IT trigger #1").build());

    pagerDuty.notify(new Trigger.Builder("Client IT trigger #2")
        .withIncidentKey("it-trigger-2")
        .addDetails("Type", "Integration test")
        .build());

    pagerDuty.notify(new Resolution.Builder(trigger1.incidentKey())
        .withDescription("Resolving #1")
        .build());

    NotifyResult trigger3 = pagerDuty.notify(new Trigger.Builder("Client ID trigger #3").build());

    pagerDuty.notify(new Trigger.Builder("Client IT trigger #2 update 2")
        .withIncidentKey("it-trigger-2")
        .addDetails("Hey", "Hey")
        .build());

    pagerDuty.notify(new Resolution.Builder(trigger3.incidentKey())
        .addDetails("Hello", "World!")
        .build());

    pagerDuty.notify(new Resolution.Builder("it-trigger-2").build());
  }
}
