package com.capgemini.csd.tippkick.spielplan.adapter.to;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ResultTO {
    @NotNull
    private int hometeamScore;
    @NotNull
    private int foreignteamScore;
}
