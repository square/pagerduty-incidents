package com.squareup.pagerduty.incidents;

import java.util.ArrayDeque;
import java.util.Deque;

final class RecordingEventService implements EventService {
  private final Deque<Event> events = new ArrayDeque<>();

  @Override public IncidentResult trigger(Event event) {
    events.add(event);
    return null;
  }

  @Override public IncidentResult resolve(Event event) {
    events.add(event);
    return null;
  }

  public Event takeEvent() {
    return events.removeFirst();
  }
}
