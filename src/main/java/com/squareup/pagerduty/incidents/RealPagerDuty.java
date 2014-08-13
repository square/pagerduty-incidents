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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.squareup.pagerduty.incidents.Event.TYPE_RESOLVE;
import static com.squareup.pagerduty.incidents.Event.TYPE_TRIGGER;
import static com.squareup.pagerduty.incidents.Util.checkStringArgument;

final class RealPagerDuty extends PagerDuty {
  private static final int MAX_DESCRIPTION_LENGTH = 1024;

  private final String apiKey;
  private final EventService service;

  RealPagerDuty(String apiKey, EventService service) {
    this.apiKey = apiKey;
    this.service = service;
  }

  @Override public TriggerBuilder newTrigger(final String description) {
    checkStringArgument(description, "description");
    checkArgument(description.length() <= MAX_DESCRIPTION_LENGTH, "'description' length must be "
        + MAX_DESCRIPTION_LENGTH
        + " or less. Was: "
        + description.length());

    return new TriggerBuilder() {
      private String incidentKey;
      private ImmutableMap.Builder<String, String> details = ImmutableMap.builder();

      @Override public TriggerBuilder withIncidentKey(String incidentKey) {
        this.incidentKey = checkStringArgument(incidentKey, "incidentKey");
        return this;
      }

      @Override public TriggerBuilder addDetails(String name, String value) {
        details.put(checkStringArgument(name, "name"), value);
        return this;
      }

      @Override public TriggerBuilder addDetails(Map<String, String> details) {
        checkNotNull(details, "details");
        this.details.putAll(details);
        return this;
      }

      @Override public IncidentResult execute() {
        Event event = new Event(apiKey, incidentKey, TYPE_TRIGGER, description, details.build());
        return service.trigger(event);
      }
    };
  }

  public PagerDuty.ResolutionBuilder newResolution(final String incidentKey) {
    checkStringArgument(incidentKey, "incidentKey");

    return new PagerDuty.ResolutionBuilder() {
      private String description;
      private ImmutableMap.Builder<String, String> details = ImmutableMap.builder();

      @Override public PagerDuty.ResolutionBuilder withDescription(String description) {
        this.description = checkStringArgument(description, "description");
        return this;
      }

      @Override public PagerDuty.ResolutionBuilder addDetails(String name, String value) {
        details.put(checkStringArgument(name, "name"), value);
        return this;
      }

      @Override public PagerDuty.ResolutionBuilder addDetails(Map<String, String> details) {
        checkNotNull(details, "details");
        this.details.putAll(details);
        return this;
      }

      @Override public IncidentResult execute() {
        Event body = new Event(apiKey, incidentKey, TYPE_RESOLVE, description, details.build());
        return service.resolve(body);
      }
    };
  }
}
