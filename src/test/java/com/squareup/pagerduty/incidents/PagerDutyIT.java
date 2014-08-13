package com.squareup.pagerduty.incidents;

public final class PagerDutyIT {
  private static final String API_KEY = "YOUR API KEY";

  public static void main(String... args) {
    PagerDuty pagerDuty = PagerDuty.create(API_KEY);

    IncidentResult trigger1 = pagerDuty.newTrigger("Client IT trigger #1").execute();

    pagerDuty.newTrigger("Client IT trigger #2")
        .withIncidentKey("it-trigger-2")
        .addDetails("Type", "Integration test")
        .execute();

    pagerDuty.newResolution(trigger1.incidentKey())
        .withDescription("Resolving #1")
        .execute();

    IncidentResult trigger3 = pagerDuty.newTrigger("Client ID trigger #3").execute();

    pagerDuty.newTrigger("Client IT trigger #2 update 2")
        .withIncidentKey("it-trigger-2")
        .addDetails("Hey", "Hey")
        .execute();

    pagerDuty.newResolution(trigger3.incidentKey())
        .addDetails("Hello", "World!")
        .execute();

    pagerDuty.newResolution("it-trigger-2").execute();
  }
}
