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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.squareup.pagerduty.incidents.Util.checkArgument;
import static com.squareup.pagerduty.incidents.Util.checkNotNull;
import static com.squareup.pagerduty.incidents.Util.checkStringArgument;

/** Report a new or ongoing problem. */
public final class Trigger extends Event {
  private static final int MAX_DESCRIPTION_LENGTH = 1024;
  private final List<Context> contexts;

  private Trigger(String incidentKey, String description, String client, String clientUrl,
      Map<String, String> details, List<Context> contexts) {
    super(null, incidentKey, TYPE_TRIGGER, description, client, clientUrl, details);
    this.contexts = contexts;
  }

  /**
   * Fluent interface for building trigger data.
   * <p>
   * Calling {@link #withIncidentKey} is required. All other data is optional.
   */

  public static final class Builder {
    private final String description;
    private String incidentKey;
    private String client;
    private String clientUrl;
    private Map<String, String> details = new LinkedHashMap<>();
    private List<Context> contexts = new ArrayList<Context>();

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

    /** The name of the monitoring client that is triggering this event.*/
    public Builder client(String client) {
      this.client = checkStringArgument(client, "client");
      return this;
    }

    /** The URL of the monitoring client that is triggering this event. */
    public Builder clientUrl(String clientUrl) {
      this.clientUrl = checkStringArgument(clientUrl, "clientUrl");
      return this;
    }

    /** An arbitrary name-value pair which will be included in incident the log.*/
    public Builder addDetails(String name, String value) {
      details.put(checkStringArgument(name, "name"), value);
      return this;
    }

    /** A link to be attached to the pager duty notification.*/
    public Builder addLink(Link link) {
      contexts.add(link);
      return this;
    }

    /** An image to be attached to the pager duty notification.*/
    public Builder addImage(Image image) {
      contexts.add(image);
      return this;
    }

    /** Arbitrary name-value pairs which will be included in incident the log. */
    public Builder addDetails(Map<String, String> details) {
      checkNotNull(details, "details");
      this.details.putAll(details);
      return this;
    }

    public Trigger build() {
      return new Trigger(incidentKey, description, client, clientUrl, details, contexts);
    }
  }
}
