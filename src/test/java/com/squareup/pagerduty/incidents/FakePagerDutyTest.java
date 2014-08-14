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

import java.util.Map;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

public final class FakePagerDutyTest {
  private final FakePagerDuty pagerDuty = new FakePagerDuty();

  @Test public void triggerCreatesIncidentKeys() {
    NotifyResult one = pagerDuty.notify(new Trigger.Builder("One").build());
    assertThat(one.incidentKey()).isNotNull();
    NotifyResult two = pagerDuty.notify(new Trigger.Builder("Two").build());
    assertThat(two.incidentKey()).isNotNull();

    Map<String, String> open = pagerDuty.openIncidents();
    Map<String, String> closed = pagerDuty.closedIncidents();
    assertThat(open).containsExactly(entry(one.incidentKey(), "One"),
        entry(two.incidentKey(), "Two"));
    assertThat(closed).isEmpty();
  }

  @Test public void triggerPropagatesSpecifiedKeys() {
    NotifyResult one =
        pagerDuty.notify(new Trigger.Builder("One").withIncidentKey("incident-one").build());
    assertThat(one.incidentKey()).isEqualTo("incident-one");

    NotifyResult two =
        pagerDuty.notify(new Trigger.Builder("Two").withIncidentKey("incident-two").build());
    assertThat(two.incidentKey()).isEqualTo("incident-two");

    Map<String, String> open = pagerDuty.openIncidents();
    Map<String, String> closed = pagerDuty.closedIncidents();
    assertThat(open).containsExactly(entry("incident-one", "One"), entry("incident-two", "Two"));
    assertThat(closed).isEmpty();
  }

  @Test public void resolvingClosesIncident() {
    pagerDuty.notify(new Trigger.Builder("One").withIncidentKey("incident-one").build());
    pagerDuty.notify(new Resolution.Builder("incident-one").build());

    Map<String, String> open = pagerDuty.openIncidents();
    Map<String, String> closed = pagerDuty.closedIncidents();
    assertThat(open).isEmpty();
    assertThat(closed).containsExactly(entry("incident-one", "One"));
  }

  @Test public void triggerReOpensResolvedIncident() {
    pagerDuty.notify(new Trigger.Builder("One").withIncidentKey("incident-one").build());
    pagerDuty.notify(new Resolution.Builder("incident-one").build());
    pagerDuty.notify(new Trigger.Builder("One").withIncidentKey("incident-one").build());

    Map<String, String> open = pagerDuty.openIncidents();
    Map<String, String> closed = pagerDuty.closedIncidents();
    assertThat(open).containsExactly(entry("incident-one", "One"));
    assertThat(closed).isEmpty();
  }

  @Test public void clearRemovesOpenAndClosedIncidents() {
    pagerDuty.notify(new Trigger.Builder("One").withIncidentKey("incident-one").build());
    pagerDuty.notify(new Resolution.Builder("incident-one").build());
    pagerDuty.notify(new Trigger.Builder("Two").withIncidentKey("incident-two").build());
    pagerDuty.clearIncidents();

    Map<String, String> open = pagerDuty.openIncidents();
    Map<String, String> closed = pagerDuty.closedIncidents();
    assertThat(open).isEmpty();
    assertThat(closed).isEmpty();
  }
}
