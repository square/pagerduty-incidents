

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

import com.google.gson.Gson;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LinkTest {

    private final Gson gson = new Gson();

    @Test
    public void withoutTextSerialization() {
        Link link = Link.withoutText("example.com/somefile.txt");
        String actual = gson.toJson(link);

        String expected = ""
                + "{"
                + "\"type\":\"link\","
                + "\"href\":\"example.com/somefile.txt\""
                + "}";
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void withTextSerialization() {
        Link link = Link.withText("example.com/somefile.txt", "Some text");
        String actual = gson.toJson(link);

        String expected = ""
                + "{"
                + "\"text\":\"Some text\","
                + "\"type\":\"link\","
                + "\"href\":\"example.com/somefile.txt\""
                + "}";
        assertThat(actual).isEqualTo(expected);
    }
}
