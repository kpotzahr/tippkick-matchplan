package com.capgemini.csd.tippkick.spielplan.cukes.common;

import com.capgemini.csd.tippkick.spielplan.cukes.to.MatchTestTO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class StepVariables {
    private MatchTestTO currentMatch;

    public void clear() {
        currentMatch = null;
    }
}
