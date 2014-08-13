package com.squareup.pagerduty.incidents;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import static com.squareup.pagerduty.incidents.EventAssert.assertThat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.MapEntry.entry;
import static org.junit.Assert.fail;

public final class PagerDutyTest {
  private RecordingEventService service = new RecordingEventService();
  private PagerDuty pagerDuty = new PagerDuty("123456", service);

  @Test public void basicTrigger() {
    pagerDuty.newTrigger("Paper cut").execute();
    Event event = service.takeEvent();
    assertThat(event).hasServiceKey("123456")
        .hasDescription("Paper cut")
        .hasIncidentKey(null)
        .hasEventType("trigger")
        .hasNoDetails();
  }

  @Test public void triggerWithBellsAndWhistles() {
    pagerDuty.newTrigger("Paper cut")
        .withIncidentKey("ouch")
        .addDetails("Location", "Left index finger")
        .addDetails(ImmutableMap.of("Foo", "Bar", "Kit", "Kat"))
        .execute();
    Event event = service.takeEvent();
    assertThat(event).hasServiceKey("123456")
        .hasDescription("Paper cut")
        .hasIncidentKey("ouch")
        .hasEventType("trigger")
        .hasDetails(entry("Location", "Left index finger"), entry("Foo", "Bar"),
            entry("Kit", "Kat"));
  }

  @Test public void basicResolve() {
    pagerDuty.newResolution("ouch").execute();
    Event event = service.takeEvent();
    assertThat(event).hasServiceKey("123456")
        .hasIncidentKey("ouch")
        .hasDescription(null)
        .hasEventType("resolve")
        .hasNoDetails();
  }

  @Test public void resolveWithBellsAndWhistles() {
    pagerDuty.newResolution("ouch")
        .withDescription("Band Aid was applied")
        .addDetails("Location", "Left index finger")
        .addDetails(ImmutableMap.of("Foo", "Bar", "Kit", "Kat"))
        .execute();
    Event event = service.takeEvent();
    assertThat(event).hasServiceKey("123456")
        .hasIncidentKey("ouch")
        .hasEventType("resolve")
        .hasDescription("Band Aid was applied")
        .hasDetails(entry("Location", "Left index finger"), entry("Foo", "Bar"),
            entry("Kit", "Kat"));
  }

  @Test public void apiKeyRequired() {
    try {
      PagerDuty.create(null);
    } catch (NullPointerException e) {
      assertThat(e).hasMessage("apiKey");
    }
    try {
      PagerDuty.create("  ");
    } catch (IllegalArgumentException e) {
      assertThat(e).hasMessage("'apiKey' must not be blank. Was: '  '");
    }
  }

  @Test public void triggerDescriptionRequired() {
    try {
      pagerDuty.newTrigger(null);
      fail();
    } catch (NullPointerException e) {
      assertThat(e).hasMessage("description");
    }
    try {
      pagerDuty.newTrigger("  ");
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
      pagerDuty.newTrigger(builder.toString());
      fail();
    } catch (IllegalArgumentException e) {
      assertThat(e).hasMessage("'description' length must be 1024 or less. Was: 1025");
    }
  }

  @Test public void triggerKeyRequiredIfSpecified() {
    try {
      pagerDuty.newTrigger("Hi").withIncidentKey(null);
      fail();
    } catch (NullPointerException e) {
      assertThat(e).hasMessage("incidentKey");
    }
    try {
      pagerDuty.newTrigger("Hi").withIncidentKey("  ");
      fail();
    } catch (IllegalArgumentException e) {
      assertThat(e).hasMessage("'incidentKey' must not be blank. Was: '  '");
    }
  }

  @Test public void triggerDetailsNameRequiredIfSpecified() {
    try {
      pagerDuty.newTrigger("Hi").addDetails(null, "Hi");
      fail();
    } catch (NullPointerException e) {
      assertThat(e).hasMessage("name");
    }
    try {
      pagerDuty.newTrigger("Hi").addDetails("  ", "Hi");
      fail();
    } catch (IllegalArgumentException e) {
      assertThat(e).hasMessage("'name' must not be blank. Was: '  '");
    }
  }

  @Test public void triggerDetailsRequiredIfSpecified() {
    try {
      pagerDuty.newTrigger("Hi").addDetails(null);
      fail();
    } catch (NullPointerException e) {
      assertThat(e).hasMessage("details");
    }
  }

  @Test public void resolveKeyRequired() {
    try {
      pagerDuty.newResolution(null);
      fail();
    } catch (NullPointerException e) {
      assertThat(e).hasMessage("incidentKey");
    }
    try {
      pagerDuty.newResolution("  ");
      fail();
    } catch (IllegalArgumentException e) {
      assertThat(e).hasMessage("'incidentKey' must not be blank. Was: '  '");
    }
  }

  @Test public void resolveDescriptionRequiredIfSpecified() {
    try {
      pagerDuty.newResolution("Hi").withDescription(null);
      fail();
    } catch (NullPointerException e) {
      assertThat(e).hasMessage("description");
    }
    try {
      pagerDuty.newResolution("Hi").withDescription("  ");
      fail();
    } catch (IllegalArgumentException e) {
      assertThat(e).hasMessage("'description' must not be blank. Was: '  '");
    }
  }

  @Test public void resolveDetailsNameRequiredIfSpecified() {
    try {
      pagerDuty.newResolution("Hi").addDetails(null, "Hi");
      fail();
    } catch (NullPointerException e) {
      assertThat(e).hasMessage("name");
    }
    try {
      pagerDuty.newResolution("Hi").addDetails("  ", "Hi");
      fail();
    } catch (IllegalArgumentException e) {
      assertThat(e).hasMessage("'name' must not be blank. Was: '  '");
    }
  }

  @Test public void resolveDetailsRequiredIfSpecified() {
    try {
      pagerDuty.newResolution("Hi").addDetails(null);
      fail();
    } catch (NullPointerException e) {
      assertThat(e).hasMessage("details");
    }
  }
}
