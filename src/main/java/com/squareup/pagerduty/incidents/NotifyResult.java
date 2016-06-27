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

/** Response data from triggering or resolving an incident. */
public final class NotifyResult {
  private final String status;
  private final String message;
  private final String incident_key;

  NotifyResult() {
    this.status = null;
    this.message = null;
    this.incident_key = null;
  }

  NotifyResult(String status, String message, String incidentKey) {
    this.status = status;
    this.message = message;
    this.incident_key = incidentKey;
  }

  /** {@code "success"} for well-formed requests. A short status description otherwise. */
  public String status() {
    return status;
  }

  /**
   * Human-readable description of the status. Only relevant when {@link #status()} is not
   * {@code "success"}.
   */
  public String message() {
    return message;
  }

  /**
   * The incident key on which the request acted upon. If no key was specified when triggering a
   * new incident, this is an automatically generated value.
   */
  public String incidentKey() {
    return incident_key;
  }
}
