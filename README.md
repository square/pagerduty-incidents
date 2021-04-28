PagerDuty Incidents for Java
=============================

Utility for programmatically triggering and resolving PagerDuty incidents.



Usage
-----

Create an instance with your service's API key:
```java
PagerDuty pagerDuty = PagerDuty.create("API key");
```

Triggering an incident requires only a description of the problem:
```java
Trigger trigger = new Trigger.Builder("Sync responded with code: " + code).build();
NotifyResult result = pagerDuty.notify(trigger);
```
The returned `NotifyResult` object will contain a generated incident key.

You can also specify a custom incident key as well as additional name-value details:
```java
Trigger trigger = new Trigger.Builder("Sync responded with code: " + code)
    .withIncidentKey("feed-sync-12")
    .addDetails("Foo", "Bar")
    .addDetails(ImmutableMap.of("Ping", "Pong", "Kit", "Kat"))
    .build();
pagerDuty.notify(trigger);
```

Trigger an incident with images:
```java
Trigger trigger = new Trigger.Builder("Sync responded with code: " + code)
    .addImage(Image.withoutOptional("example.com/image.jpg"));
    .addImage(Image.withOptional("example.com/image.jpg", "example.com/alt.jpg", "alternative text"));
    .build();
pagerDuty.notify(trigger);
```

Trigger an incident with links:
```java
Trigger trigger = new Trigger.Builder("Sync responded with code: " + code)
    .addLink(Link.withoutText("example.com/somefile.txt"));
    .addLink(Link.withText("example.com/somefile.txt", "Some text"));
    .build();
pagerDuty.notify(trigger);
```


Resolving an incident requires its key:
```java
Resolution resolution = new Resolution.Builder("feed-sync-12").build();
pagerDuty.notify(resolution);
```

A description and additional name-value details can also be specified on resolutions:
```java
Resolution resolution = new Resolution.Builder("feed-sync-12")
    .withDescription("Sync healthy with code: " + code)
    .addDetails("Foo", "Bar")
    .addDetails(ImmutableMap.of("Ping", "Pong", "Kit", "Kat"))
    .build()
pagerDuty.notify(resolution);
```

A `FakePagerDuty` class is provided for testing purposes which behaves similarly to a real
PagerDuty backend.



Download
--------

Download [the latest .jar][dl] or depend via Maven:
```xml
<dependency>
  <groupId>com.squareup.pagerduty</groupId>
  <artifactId>pagerduty-incidents</artifactId>
  <version>2.0.0</version>
</dependency>
```
or Gradle:
```groovy
compile 'com.squareup.pagerduty:pagerduty-incidents:2.0.0'
```

Snapshots of the development version are available in [Sonatype's `snapshots` repository][snap].



License
-------

    Copyright 2014 Square, Inc.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.



 [dl]: https://search.maven.org/remote_content?g=com.squareup.pagerduty&a=pagerduty-incidents&v=LATEST
 [snap]: https://oss.sonatype.org/content/repositories/snapshots/
