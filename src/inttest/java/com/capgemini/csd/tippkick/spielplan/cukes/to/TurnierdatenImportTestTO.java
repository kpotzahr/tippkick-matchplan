package com.capgemini.csd.tippkick.spielplan.cukes.to;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TurnierdatenImportTestTO {
    private List<MatchTestTO> matches;
}
