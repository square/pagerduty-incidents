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

import java.io.IOException;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.squareup.pagerduty.incidents.Util.checkNotNull;
import static com.squareup.pagerduty.incidents.Util.checkStringArgument;

/** Utility for triggering and resolving PagerDuty incidents. */
public abstract class PagerDuty {
  public static final String HOST = "https://events.pagerduty.com";

  /** Create a new instance using the specified API key. */
  public static PagerDuty create(String apiKey) {
    Retrofit retrofit = new Retrofit.Builder() //
        .baseUrl(HOST) //
        .addConverterFactory(GsonConverterFactory.create())
        .build();
    return create(apiKey, retrofit);
  }

  /** Create a new instance using the specified API key and configured {@link Retrofit}. */
  public static PagerDuty create(String apiKey, Retrofit retrofit) {
    checkStringArgument(apiKey, "apiKey");
    checkNotNull(retrofit, "retrofit");

    return realPagerDuty(apiKey, retrofit.create(EventService.class));
  }

  static PagerDuty realPagerDuty(final String apiKey, final EventService service) {
    return new PagerDuty() {
      @Override public NotifyResult notify(Trigger trigger) throws IOException {
        if (trigger.service_key == null) {
          return service.notify(trigger.withApiKey(apiKey)).execute().body();
        } else {
          return service.notify(trigger).execute().body();
        }
      }

      @Override public NotifyResult notify(Resolution resolution) throws IOException {
        if (resolution.service_key == null) {
          return service.notify(resolution.withApiKey(apiKey)).execute().body();
        } else {
          return service.notify(resolution).execute().body();
        }
      }
    };
  }

  /** Send an incident trigger notification to PagerDuty. */
  public abstract NotifyResult notify(Trigger trigger) throws IOException;

  /** Send an incident resolution notification to PagerDuty. */
  public abstract NotifyResult notify(Resolution resolution) throws IOException;
}
