package com.capgemini.csd.tippkick.spielplan.adapter.to;

import lombok.Data;

import javax.validation.Valid;
import java.util.List;

@Data
public class TurnierdatenImportTO {
    private List<@Valid MatchTO> matches;
}
