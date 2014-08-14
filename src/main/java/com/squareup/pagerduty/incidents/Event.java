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

class Event {
  static final String TYPE_TRIGGER = "trigger";
  static final String TYPE_RESOLVE = "resolve";

  final String service_key;
  final String incident_key;
  final String event_type;
  final String description;
  final Map<String, String> details;

  Event(String serviceKey, String incidentKey, String eventType, String description,
      Map<String, String> details) {
    this.service_key = serviceKey;
    this.incident_key = incidentKey;
    this.event_type = eventType;
    this.description = description;
    this.details = ImmutableMap.copyOf(details);
  }

  Event withApiKey(String apiKey) {
    return new Event(apiKey, incident_key, event_type, description, details);
  }
}
