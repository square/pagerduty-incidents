

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

public class ImageTest {

    private final Gson gson = new Gson();

    @Test
    public void withoutOptionalSerialization() {
        Image image = Image.withoutOptional ("example.com/image.jpg");
        String actual = gson.toJson(image);

        String expected = ""
                + "{"
                + "\"src\":\"example.com/image.jpg\","
                + "\"type\":\"image\""
                + "}";
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void withOptionalSerialization() {
        Image image = Image.withOptional ("example.com/image.jpg", "example.com/alt.jpg", "alternative text");
        String actual = gson.toJson(image);

        String expected = ""
                + "{"
                + "\"src\":\"example.com/image.jpg\","
                + "\"alt\":\"alternative text\","
                + "\"type\":\"image\","
                + "\"href\":\"example.com/alt.jpg\""
                + "}";
        assertThat(actual).isEqualTo(expected);
    }
}
