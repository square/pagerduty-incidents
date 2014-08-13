package com.squareup.pagerduty.incidents;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public final class EventTest {
  private final Gson gson = new Gson();

  @Test public void serialization() {
    Event event = new Event("e93facc04764012d7bfb002500d5d1a6", "srv01/HTTP", "trigger",
        "FAILURE for production/HTTP on machine srv01.acme.com",
        ImmutableMap.of("ping time", "1500ms", "load avg", "0.75"));
    String actual = gson.toJson(event);

    String expected = ""
        + "{"
        + "\"service_key\":\"e93facc04764012d7bfb002500d5d1a6\","
        + "\"incident_key\":\"srv01/HTTP\","
        + "\"event_type\":\"trigger\","
        + "\"description\":\"FAILURE for production/HTTP on machine srv01.acme.com\","
        + "\"details\":{"
        + "\"ping time\":\"1500ms\","
        + "\"load avg\":\"0.75\""
        + "}"
        + "}";
    assertThat(actual).isEqualTo(expected);
  }
}
