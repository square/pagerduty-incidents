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

import java.util.LinkedHashMap;
import java.util.Map;

import static com.squareup.pagerduty.incidents.Util.checkNotNull;
import static com.squareup.pagerduty.incidents.Util.checkStringArgument;

/** Resolve an existing incident. */
public final class Resolution extends Event {
  private Resolution(String serviceKey, String incidentKey, String description, Map<String, String> details) {
    super(serviceKey, incidentKey, TYPE_RESOLVE, description, null, null, details);
  }

  /** Fluent interface for building resolution data. */
  public static final class Builder {
    private final String incidentKey;
    private String serviceKey;
    private String description;
    private Map<String, String> details = new LinkedHashMap<>();

    /**
     * Build data to resolve an incident with the specified {@code incidentKey}.
     *
     * @param incidentKey Identifies the incident to resolve. This should be the incident_key you
     * received back when the incident was first opened by a trigger event. Resolve events
     * referencing resolved or nonexistent incidents will be discarded.
     */
    public Builder(String incidentKey) {
      this.incidentKey = checkStringArgument(incidentKey, "incidentKey");
    }

    /**
     * Build data to resolve an incident with the specified {@code serviceKey} and
     * {@code incidentKey}.
     *
     * @param serviceKey Identifies the service to which the incident belongs.  This should be the
     * Integration Key listed in the PagerDuty UI for the Service on its integrations tab.
     * @param incidentKey Identifies the incident to resolve. This should be the incident_key you
     * received back when the incident was first opened by a trigger event. Resolve events
     * referencing resolved or nonexistent incidents will be discarded.
     */
    public Builder(String serviceKey, String incidentKey) {
      this.serviceKey = checkStringArgument(serviceKey, "serviceKey");
      this.incidentKey = checkStringArgument(incidentKey, "incidentKey");
    }

    /** Text that will appear in the incident's log associated with this event. */
    public Builder withDescription(String description) {
      this.description = checkStringArgument(description, "description");
      return this;
    }

    /** An arbitrary name-value pair which will be included in incident the log. */
    public Builder addDetails(String name, String value) {
      details.put(checkStringArgument(name, "name"), value);
      return this;
    }

    /** Arbitrary name-value pairs which will be included in incident the log. */
    public Builder addDetails(Map<String, String> details) {
      checkNotNull(details, "details");
      this.details.putAll(details);
      return this;
    }

    public Resolution build() {
      return new Resolution(serviceKey, incidentKey, description, details);
    }
  }
}
