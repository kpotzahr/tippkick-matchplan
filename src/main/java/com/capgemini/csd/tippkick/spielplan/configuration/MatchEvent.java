package com.capgemini.csd.tippkick.spielplan.configuration;

import java.time.Instant;

/**
 * Marker Interface for Kafka Events
 */
public interface MatchEvent {

    default Instant getCreationTimestamp() {
        return Instant.now();
    }

    String getTopic();

}
