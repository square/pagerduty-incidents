package com.squareup.pagerduty.incidents;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import org.assertj.core.data.MapEntry;

final class EventAssert extends AbstractAssert<EventAssert, Event> {
  static EventAssert assertThat(Event event) {
    return new EventAssert(event);
  }

  private EventAssert(Event actual) {
    super(actual, EventAssert.class);
  }

  public EventAssert hasServiceKey(String serviceKey) {
    isNotNull();
    String actualServiceKey = actual.service_key;
    Assertions.assertThat(actualServiceKey) //
        .overridingErrorMessage("Expected service key <%s> but was <%s>.", serviceKey,
            actualServiceKey) //
        .isEqualTo(serviceKey);
    return this;
  }

  public EventAssert hasIncidentKey(String incidentKey) {
    isNotNull();
    String actualIncidentKey = actual.incident_key;
    Assertions.assertThat(actualIncidentKey) //
        .overridingErrorMessage("Expected incident key <%s> but was <%s>.", incidentKey,
            actualIncidentKey) //
        .isEqualTo(incidentKey);
    return this;
  }

  public EventAssert hasEventType(String eventType) {
    isNotNull();
    String actualEventType = actual.event_type;
    Assertions.assertThat(actualEventType) //
        .overridingErrorMessage("Expected event type <%s> but was <%s>.", eventType,
            actualEventType) //
        .isEqualTo(eventType);
    return this;
  }

  public EventAssert hasDescription(String description) {
    isNotNull();
    String actualDescription = actual.description;
    Assertions.assertThat(actualDescription) //
        .overridingErrorMessage("Expected description <%s> but was <%s>.", description,
            actualDescription) //
        .isEqualTo(description);
    return this;
  }

  public EventAssert hasNoDetails() {
    isNotNull();
    Assertions.assertThat(actual.details).isNull();
    return this;
  }

  public EventAssert hasDetails(MapEntry... entries) {
    isNotNull();
    Assertions.assertThat(actual.details).containsExactly(entries);
    return this;
  }
}
