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
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * A fake implementation of {@link PagerDuty} that keeps track of open and closed incidents in
 * memory.
 */
public final class FakePagerDuty extends PagerDuty {
  public static FakePagerDuty create() {
    return new FakePagerDuty();
  }

  private final Map<String, String> openIncidents;
  private final Map<String, String> closedIncidents;
  private final PagerDuty proxy;

  private FakePagerDuty() {
    openIncidents = new LinkedHashMap<>();
    closedIncidents = new LinkedHashMap<>();
    proxy = new RealPagerDuty("fake", new FakeEventService());
  }

  @Override public TriggerBuilder newTrigger(String description) {
    return proxy.newTrigger(description);
  }

  @Override public ResolutionBuilder newResolution(String incidentKey) {
    return proxy.newResolution(incidentKey);
  }

  /** A snapshot of the current open incidents and their descriptions. */
  public Map<String, String> openIncidents() {
    synchronized (this) {
      return ImmutableMap.copyOf(openIncidents);
    }
  }

  /** A snapshot of the current closed incidents and their descriptions. */
  public Map<String, String> closedIncidents() {
    synchronized (this) {
      return ImmutableMap.copyOf(closedIncidents);
    }
  }

  /** Clear open and closed incidents. */
  public void clearIncidents() {
    synchronized (this) {
      openIncidents.clear();
      closedIncidents.clear();
    }
  }

  private class FakeEventService implements EventService {
    @Override public IncidentResult trigger(Event event) {
      String incidentKey = event.incident_key;
      if (incidentKey == null) {
        incidentKey = UUID.randomUUID().toString();
      }
      synchronized (FakePagerDuty.this) {
        closedIncidents.remove(incidentKey);
        if (!openIncidents.containsKey(incidentKey)) {
          openIncidents.put(incidentKey, event.description);
        }
      }
      return new IncidentResult("success", "Event recorded", incidentKey);
    }

    @Override public IncidentResult resolve(Event event) {
      String incidentKey = event.incident_key;
      synchronized (FakePagerDuty.this) {
        String description = openIncidents.remove(incidentKey);
        if (description != null) {
          closedIncidents.put(incidentKey, description);
        }
      }
      return new IncidentResult("success", "Event recorded", incidentKey);
    }
  }
}
