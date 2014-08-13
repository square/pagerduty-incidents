package com.squareup.pagerduty.incidents;

import java.util.Map;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

public final class FakePagerDutyTest {
  private final FakePagerDuty pagerDuty = FakePagerDuty.create();

  @Test public void triggerCreatesIncidentKeys() {
    IncidentResult one = pagerDuty.newTrigger("One").execute();
    assertThat(one.incidentKey()).isNotNull();
    IncidentResult two = pagerDuty.newTrigger("Two").execute();
    assertThat(two.incidentKey()).isNotNull();

    Map<String, String> open = pagerDuty.openIncidents();
    Map<String, String> closed = pagerDuty.closedIncidents();
    assertThat(open).containsExactly(entry(one.incidentKey(), "One"),
        entry(two.incidentKey(), "Two"));
    assertThat(closed).isEmpty();
  }

  @Test public void triggerPropagatesSpecifiedKeys() {
    IncidentResult one = pagerDuty.newTrigger("One")
        .withIncidentKey("incident-one")
        .execute();
    assertThat(one.incidentKey()).isEqualTo("incident-one");
    IncidentResult two = pagerDuty.newTrigger("Two")
        .withIncidentKey("incident-two")
        .execute();
    assertThat(two.incidentKey()).isEqualTo("incident-two");

    Map<String, String> open = pagerDuty.openIncidents();
    Map<String, String> closed = pagerDuty.closedIncidents();
    assertThat(open).containsExactly(entry("incident-one", "One"),
        entry("incident-two", "Two"));
    assertThat(closed).isEmpty();
  }

  @Test public void resolvingClosesIncident() {
    pagerDuty.newTrigger("One").withIncidentKey("incident-one").execute();
    pagerDuty.newResolution("incident-one").execute();

    Map<String, String> open = pagerDuty.openIncidents();
    Map<String, String> closed = pagerDuty.closedIncidents();
    assertThat(open).isEmpty();
    assertThat(closed).containsExactly(entry("incident-one", "One"));
  }

  @Test public void triggerReOpensResolvedIncident() {
    pagerDuty.newTrigger("One").withIncidentKey("incident-one").execute();
    pagerDuty.newResolution("incident-one").execute();
    pagerDuty.newTrigger("One").withIncidentKey("incident-one").execute();

    Map<String, String> open = pagerDuty.openIncidents();
    Map<String, String> closed = pagerDuty.closedIncidents();
    assertThat(open).containsExactly(entry("incident-one", "One"));
    assertThat(closed).isEmpty();
  }

  @Test public void clearRemovesOpenAndClosedIncidents() {
    pagerDuty.newTrigger("One").withIncidentKey("incident-one").execute();
    pagerDuty.newResolution("incident-one").execute();
    pagerDuty.newTrigger("Two").withIncidentKey("incident-two").execute();
    pagerDuty.clearIncidents();

    Map<String, String> open = pagerDuty.openIncidents();
    Map<String, String> closed = pagerDuty.closedIncidents();
    assertThat(open).isEmpty();
    assertThat(closed).isEmpty();
  }
}
