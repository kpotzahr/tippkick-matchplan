package com.capgemini.csd.tippkick.spielplan.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Entity
@Getter
@Setter
public class Match {
    @Id
    @GeneratedValue
    private Long id;
    @NotNull
    private Instant starttime;
    @NotNull
    @Enumerated(EnumType.STRING)
    private Participant hometeam;
    @NotNull
    @Enumerated(EnumType.STRING)
    private Participant foreignteam;
    @Embedded
    private Result result;
}
