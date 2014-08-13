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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

final class Util {
  private Util() {
    throw new AssertionError("No instances.");
  }

  /** Reject {@code null}, empty, and blank strings with a good exception type and message. */
  static String checkStringArgument(String s, String name) {
    checkNotNull(s, name);
    checkArgument(!s.trim().isEmpty(), "'" + name + "' must not be blank. Was: '" + s + "'");
    return s;
  }
}
