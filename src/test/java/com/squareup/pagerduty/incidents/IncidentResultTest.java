package com.squareup.pagerduty.incidents;

import com.google.gson.Gson;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public final class IncidentResultTest {
  private final Gson gson = new Gson();

  @Test public void successDeserialization() {
    String json = ""
        + "{\n"
        + "  \"status\": \"success\",\n"
        + "  \"incident_key\": \"123456\",\n"
        + "  \"message\": \"Event processed\"\n"
        + "}";
    IncidentResult result = gson.fromJson(json, IncidentResult.class);
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
    IncidentResult result = gson.fromJson(json, IncidentResult.class);
    assertThat(result.status()).isEqualTo("invalid event");
    assertThat(result.incidentKey()).isEqualTo("123456");
    assertThat(result.message()).isEqualTo("Incident key not found.");
  }
}
