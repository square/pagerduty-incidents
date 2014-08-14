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

import com.google.common.collect.ImmutableMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.squareup.pagerduty.incidents.Util.checkStringArgument;

/** Resolve an existing incident. */
public final class Resolution extends Event {
  private Resolution(String incidentKey, String description, Map<String, String> details) {
    super(null, incidentKey, TYPE_RESOLVE, description, details);
  }

  /** Fluent interface for building resolution data. */
  public static final class Builder {
    private final String incidentKey;
    private String description;
    private ImmutableMap.Builder<String, String> details = ImmutableMap.builder();

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
      return new Resolution(incidentKey, description, details.build());
    }
  }
}
