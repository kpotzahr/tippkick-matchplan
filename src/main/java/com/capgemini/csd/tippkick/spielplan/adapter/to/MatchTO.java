package com.capgemini.csd.tippkick.spielplan.adapter.to;

import com.capgemini.csd.tippkick.spielplan.domain.Participant;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.Instant;

@Data
public class MatchTO {
    private Long matchId;
    @NotNull
    private Instant starttime;
    @NotNull
    private Participant hometeam;
    @NotNull
    private Participant foreignteam;
}
