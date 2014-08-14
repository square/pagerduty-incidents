/**
 * Utility for programmatically triggering and resolving PagerDuty incidents.
 *
 * <h3>Usage</h3>
 *
 * Create an instance of {@link com.squareup.pagerduty.incidents.PagerDuty PagerDuty} with your
 * service's API key:
 * <pre>{@code
 * PagerDuty pagerDuty = PagerDuty.create("API key");
 * }</pre>
 *
 * Triggering an incident requires only a description of the problem:
 * <pre>{@code
 * Trigger trigger = new Trigger.Builder("Sync responded with code: " + code).build();
 * NotifyResult result = pagerDuty.notify(trigger);
 * }</pre>
 * The returned {@link com.squareup.pagerduty.incidents.NotifyResult NotifyResult} object will
 * contain a generated incident key.
 * <p>
 * You can also specify a custom incident key as well as additional name-value details:
 * <pre>{@code
 * Trigger trigger = new Trigger.Builder("Sync responded with code: " + code)
 *     .withKey("feed-sync-12")
 *     .addDetails("Foo", "Bar")
 *     .addDetails(ImmutableMap.of("Ping", "Pong", "Kit", "Kat"))
 *     .build();
 * pagerDuty.notify(trigger);
 * }</pre>
 *
 * Resolving an incident requires its key:
 * <pre>{@code
 * Resolution resolution = new Resolution.Builder("feed-sync-12").build();
 * pagerDuty.notify(resolution);
 * }</pre>
 *
 * A description and additional name-value details can also be specified on resolutions:
 * <pre>{@code
 * Resolution resolution = new Resolution.Builder("feed-sync-12")
 *     .withDescription("Sync healthy with code: " + code)
 *     .addDetails("Foo", "Bar")
 *     .addDetails(ImmutableMap.of("Ping", "Pong", "Kit", "Kat"))
 *     .build()
 * pagerDuty.notify(resolution);
 * }</pre>
 * A {@link com.squareup.pagerduty.incidents.FakePagerDuty FakePagerDuty} class is provided for
 * testing purposes which behaves similarly to a real PagerDuty backend.
 */
package com.squareup.pagerduty.incidents;
