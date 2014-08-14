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

import static com.squareup.pagerduty.incidents.Util.checkArgument;
import static com.squareup.pagerduty.incidents.Util.checkNotNull;
import static com.squareup.pagerduty.incidents.Util.checkStringArgument;

/** Report a new or ongoing problem. */
public final class Trigger extends Event {
  private static final int MAX_DESCRIPTION_LENGTH = 1024;

  private Trigger(String incidentKey, String description, Map<String, String> details) {
    super(null, incidentKey, TYPE_TRIGGER, description, details);
  }

  /** Fluent interface for building trigger data. */
  public static final class Builder {
    private final String description;
    private String incidentKey;
    private Map<String, String> details = new LinkedHashMap<>();

    /**
     * Build data to trigger a new incident.
     *
     * @param description A short description of the problem that led to this trigger. This field
     * (or a truncated version) will be used when generating phone calls, SMS messages and alert
     * emails. It will also appear on the incidents tables in the PagerDuty UI. The maximum length
     * is 1024 characters.
     */
    public Builder(String description) {
      checkStringArgument(description, "description");
      checkArgument(description.length() <= MAX_DESCRIPTION_LENGTH, "'description' length must be "
          + MAX_DESCRIPTION_LENGTH
          + " or less. Was: "
          + description.length());

      this.description = description;
    }

    /**
     * Identifies the incident to which this trigger event should be applied. If there's no open
     * (i.e. unresolved) incident with this key, a new one will be created. If there's already an
     * open incident with a matching key, this event will be appended to that incident's log. The
     * event key provides an easy way to "de-dup" problem reports. If this field isn't provided,
     * PagerDuty will automatically open a new incident with a unique key.
     */
    public Builder withIncidentKey(String incidentKey) {
      this.incidentKey = checkStringArgument(incidentKey, "incidentKey");
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

    public Trigger build() {
      return new Trigger(incidentKey, description, details);
    }
  }
}
