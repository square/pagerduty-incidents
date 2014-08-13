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
import retrofit.Endpoints;
import retrofit.RestAdapter;

import static com.squareup.pagerduty.incidents.Util.checkStringArgument;

/** Utility for triggering and resolving PagerDuty incidents. */
public abstract class PagerDuty {
  private static final String HOST = "https://events.pagerduty.com";

  /** Create a new instance using the specified API key. */
  public static PagerDuty create(String apiKey) {
    checkStringArgument(apiKey, "apiKey");

    RestAdapter restAdapter =
        new RestAdapter.Builder().setEndpoint(Endpoints.newFixedEndpoint(HOST)).build();
    EventService service = restAdapter.create(EventService.class);

    return new RealPagerDuty(apiKey, service);
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
  public abstract TriggerBuilder newTrigger(String description);

  /** Fluent interface for building trigger data. Call {@link #newTrigger} to create. */
  public interface TriggerBuilder {
    /**
     * Identifies the incident to which this trigger event should be applied. If there's no open
     * (i.e. unresolved) incident with this key, a new one will be created. If there's already an
     * open incident with a matching key, this event will be appended to that incident's log. The
     * event key provides an easy way to "de-dup" problem reports. If this field isn't provided,
     * PagerDuty will automatically open a new incident with a unique key.
     */
    TriggerBuilder withIncidentKey(String incidentKey);

    /** An arbitrary name-value pair which will be included in incident the log. */
    TriggerBuilder addDetails(String name, String value);

    /** Arbitrary name-value pairs which will be included in incident the log. */
    TriggerBuilder addDetails(Map<String, String> details);

    /** Send this incident trigger to PagerDuty. */
    IncidentResult execute();
  }

  /**
   * Build data to resolve an incident with the specified {@code incidentKey}. You
   * <strong>must</strong> call {@link ResolutionBuilder#execute() execute()} on the returned
   * instance to complete the resolution.
   *
   * @param incidentKey Identifies the incident to resolve. This should be the incident_key you
   * received back when the incident was first opened by a trigger event. Resolve events
   * referencing resolved or nonexistent incidents will be discarded.
   */
  public abstract ResolutionBuilder newResolution(String incidentKey);

  /** Fluent interface for building resolution data. Call {@link #newResolution} to create. */
  public interface ResolutionBuilder {
    /** Text that will appear in the incident's log associated with this event. */
    ResolutionBuilder withDescription(String description);

    /** An arbitrary name-value pair which will be included in incident the log. */
    ResolutionBuilder addDetails(String name, String value);

    /** Arbitrary name-value pairs which will be included in incident the log. */
    ResolutionBuilder addDetails(Map<String, String> details);

    /** Send this incident resolution to PagerDuty. */
    IncidentResult execute();
  }
}
