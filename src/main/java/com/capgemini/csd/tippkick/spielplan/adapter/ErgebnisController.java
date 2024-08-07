package com.capgemini.csd.tippkick.spielplan.adapter;

import com.capgemini.csd.tippkick.spielplan.adapter.to.ResultTO;
import com.capgemini.csd.tippkick.spielplan.application.MatchService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Slf4j
public class ErgebnisController {
    @Autowired
    private MatchService matchService;

    @ApiOperation(value = "Import Ergebnis for a match.")
    @PostMapping("/ergebnis/{matchId}")
    public void setErgebnisForMatch(@PathVariable("matchId") Long matchId, @Valid @RequestBody ResultTO result) {
        log.info("Importing Ergebnis: {}", result);
        matchService.setResultForMatch(matchId, result.getHometeamScore(), result.getForeignteamScore());
    }
}
