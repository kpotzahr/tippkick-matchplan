package com.capgemini.csd.tippkick.spielplan.cukes.to;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MatchTestTO {
    private String starttime;
    private String hometeam;
    private String foreignteam;
    private Long matchId;

    public boolean hasSameValues(MatchTestTO other) {
        return Objects.equals(starttime, other.starttime)
                && Objects.equals(hometeam, other.hometeam)
                && Objects.equals(foreignteam, other.foreignteam);
    }
}
