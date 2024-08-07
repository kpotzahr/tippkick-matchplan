package com.capgemini.csd.tippkick.spielplan.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Result {
    private Integer hometeamScore;
    private Integer foreignteamScore;
}
