package com.capgemini.csd.tippkick.spielplan.domain.events;

import com.capgemini.csd.tippkick.spielplan.configuration.MatchEvent;
import com.capgemini.csd.tippkick.spielplan.domain.Match;
import com.capgemini.csd.tippkick.spielplan.domain.Participant;
import lombok.Getter;

import java.time.Instant;

@Getter
public class MatchStartedEvent implements MatchEvent {

    private static final String TOPIC = "match-started";

    private final long matchId;
    private final Participant hometeam;
    private final Participant foreignteam;
    private final Instant starttime;

    public MatchStartedEvent(Match match) {
        this.matchId = match.getId();
        this.hometeam = match.getHometeam();
        this.foreignteam = match.getForeignteam();
        this.starttime = match.getStarttime();
    }

    @Override
    public String getTopic() {
        return TOPIC;
    }
}
