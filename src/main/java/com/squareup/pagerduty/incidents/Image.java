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


final class Image extends Context {
    final String src;
    final String alt;

    private Image(String href, String src, String alt) {
        super("image", href);
        this.src = src;
        this.alt = alt;
    }

    static Image withOptional(String src, String href, String alt) {
        return new Image(href, src, alt);
    }

    static Image withoutOptional(String src) {
        return new Image(null, src, null);
    }

}
