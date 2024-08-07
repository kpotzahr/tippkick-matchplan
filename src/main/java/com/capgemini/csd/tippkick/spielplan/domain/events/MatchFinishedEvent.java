package com.capgemini.csd.tippkick.spielplan.domain.events;

import com.capgemini.csd.tippkick.spielplan.configuration.MatchEvent;
import com.capgemini.csd.tippkick.spielplan.domain.Match;
import com.capgemini.csd.tippkick.spielplan.domain.Result;
import lombok.Getter;

@Getter
public class MatchFinishedEvent implements MatchEvent {

    private static final String TOPIC = "match-finished";

    private final long matchId;
    private final Result result;

    public MatchFinishedEvent(Match match) {
        this.matchId = match.getId();
        this.result = match.getResult();
    }

    @Override
    public String getTopic() {
        return TOPIC;
    }
}
