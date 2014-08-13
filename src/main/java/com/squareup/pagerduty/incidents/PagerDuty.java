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

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableMap;
import java.util.Map;
import retrofit.Endpoints;
import retrofit.RestAdapter;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.squareup.pagerduty.incidents.Event.TYPE_RESOLVE;
import static com.squareup.pagerduty.incidents.Event.TYPE_TRIGGER;

/** Utility for triggering and resolving PagerDuty incidents. */
public final class PagerDuty {
  private static final String HOST = "https://events.pagerduty.com";
  private static final int MAX_DESCRIPTION_LENGTH = 1024;

  /** Create a new instance using the specified API key. */
  public static PagerDuty create(String apiKey) {
    checkStringArgument(apiKey, "apiKey");

    RestAdapter restAdapter =
        new RestAdapter.Builder().setEndpoint(Endpoints.newFixedEndpoint(HOST)).build();
    EventService service = restAdapter.create(EventService.class);

    return new PagerDuty(apiKey, service);
  }

  private final String apiKey;
  private final EventService service;

  @VisibleForTesting PagerDuty(String apiKey, EventService service) {
    this.apiKey = apiKey;
    this.service = service;
  }

  /**
   * Build data to trigger a new incident. You <strong>must</strong> call
   * {@link TriggerBuilder#execute() execute()} on the returned instance to complete the trigger.
   *
   * @param description A short description of the problem that led to this trigger. This field (or
   * a truncated version) will be used when generating phone calls, SMS messages and alert emails.
   * It will also appear on the incidents tables in the PagerDuty UI. The maximum length is 1024
   * characters.
   */
  public TriggerBuilder newTrigger(final String description) {
    checkStringArgument(description, "description");
    checkArgument(description.length() <= MAX_DESCRIPTION_LENGTH, "'description' length must be "
        + MAX_DESCRIPTION_LENGTH
        + " or less. Was: "
        + description.length());
    return new TriggerBuilder(description);
  }

  /** Fluent interface for building trigger data. Call {@link #newTrigger} to create. */
  public final class TriggerBuilder {
    private final String description;
    private String incidentKey;
    private ImmutableMap.Builder<String, String> details = ImmutableMap.builder();

    private TriggerBuilder(String description) {
      this.description = description;
    }

    /**
     * Identifies the incident to which this trigger event should be applied. If there's no open
     * (i.e. unresolved) incident with this key, a new one will be created. If there's already an
     * open incident with a matching key, this event will be appended to that incident's log. The
     * event key provides an easy way to "de-dup" problem reports. If this field isn't provided,
     * PagerDuty will automatically open a new incident with a unique key.
     */
    public TriggerBuilder withIncidentKey(String incidentKey) {
      this.incidentKey = checkStringArgument(incidentKey, "incidentKey");
      return this;
    }

    /** An arbitrary name-value pair which will be included in incident the log. */
    public TriggerBuilder addDetails(String name, String value) {
      details.put(checkStringArgument(name, "name"), value);
      return this;
    }

    /** Arbitrary name-value pairs which will be included in incident the log. */
    public TriggerBuilder addDetails(Map<String, String> details) {
      checkNotNull(details, "details");
      this.details.putAll(details);
      return this;
    }

    /** Send this incident trigger to PagerDuty. */
    public IncidentResult execute() {
      Event event = new Event(apiKey, incidentKey, TYPE_TRIGGER, description, details.build());
      return service.trigger(event);
    }
  }

  /**
   * Build data to resolve an incident with the specified {@code incidentKey}. You
   * <strong>must</strong> call {@link ResolutionBuilder#execute() execute()} on the returned
   * instance to complete the resolution.
   *
   * @param incidentKey Identifies the incident to resolve. This should be the incident_key you
   * received back when the incident was first opened by a trigger event. Resolve events referencing
   * resolved or nonexistent incidents will be discarded.
   */
  public ResolutionBuilder newResolution(final String incidentKey) {
    checkStringArgument(incidentKey, "incidentKey");
    return new ResolutionBuilder(incidentKey);
  }

  /** Fluent interface for building resolution data. Call {@link #newResolution} to create. */
  public final class ResolutionBuilder {
    private final String incidentKey;
    private String description;
    private ImmutableMap.Builder<String, String> details = ImmutableMap.builder();

    private ResolutionBuilder(String incidentKey) {
      this.incidentKey = incidentKey;
    }

    /** Text that will appear in the incident's log associated with this event. */
    public ResolutionBuilder withDescription(String description) {
      this.description = checkStringArgument(description, "description");
      return this;
    }

    /** An arbitrary name-value pair which will be included in incident the log. */
    public ResolutionBuilder addDetails(String name, String value) {
      details.put(checkStringArgument(name, "name"), value);
      return this;
    }

    /** Arbitrary name-value pairs which will be included in incident the log. */
    public ResolutionBuilder addDetails(Map<String, String> details) {
      checkNotNull(details, "details");
      this.details.putAll(details);
      return this;
    }

    /** Send this incident resolution to PagerDuty. */
    public IncidentResult execute() {
      Event body = new Event(apiKey, incidentKey, TYPE_RESOLVE, description, details.build());
      return service.resolve(body);
    }
  }

  /** Reject {@code null}, empty, and blank strings with a good exception type and message. */
  private static String checkStringArgument(String s, String name) {
    checkNotNull(s, name);
    checkArgument(!s.trim().isEmpty(), "'" + name + "' must not be blank. Was: '" + s + "'");
    return s;
  }
}
