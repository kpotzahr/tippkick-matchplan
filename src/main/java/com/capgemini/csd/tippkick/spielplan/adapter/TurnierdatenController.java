package com.capgemini.csd.tippkick.spielplan.adapter;

import com.capgemini.csd.tippkick.spielplan.adapter.to.MatchTO;
import com.capgemini.csd.tippkick.spielplan.adapter.to.TurnierdatenImportTO;
import com.capgemini.csd.tippkick.spielplan.application.MatchNotFoundException;
import com.capgemini.csd.tippkick.spielplan.application.MatchService;
import com.capgemini.csd.tippkick.spielplan.domain.Match;
import com.capgemini.csd.tippkick.spielplan.domain.MatchRepository;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@RestController
@Slf4j
class TurnierdatenController {
    @Autowired private MatchRepository matchRepository;
    @Autowired private MatchService matchService;

    @ApiOperation(value = "Import Turnierdaten and persist them in the Spielplan.")
    @PostMapping("/turnierdaten")
    void importTurnierdaten(@RequestBody @Valid TurnierdatenImportTO turnierdatenImportTO) {
        log.info("Importing Turnierdaten: {}", turnierdatenImportTO.getMatches());
        Set<Match> matches = turnierdatenImportTO.getMatches().stream()
                .map(this::mapToMatch)
                .collect(toSet());
        matchService.importTurnierdata(matches);
    }

    @ApiOperation("Lists all matches that have been imported to this Spielplan.")
    @GetMapping("/spielplan")
    List<MatchTO> getSpielplan() {
        log.info("Returning spielplan");
        return matchRepository.findAll().stream()
                .map(this::mapToMatchTo)
                .collect(toList());
    }

    @ApiOperation("Get match information for given id.")
    @GetMapping("/match/{matchId}")
    MatchTO getMatch(@PathVariable long matchId) {
        log.info("Reading match information");
        return matchRepository
                .findById(matchId)
                .map(this::mapToMatchTo)
                .orElseThrow(MatchNotFoundException::new);
    }

    private Match mapToMatch(MatchTO matchTO) {
        Match match = new Match();
        match.setHometeam(matchTO.getHometeam());
        match.setForeignteam(matchTO.getForeignteam());
        match.setStarttime(matchTO.getStarttime());
        return match;
    }

    private MatchTO mapToMatchTo(Match match) {
        MatchTO matchTO = new MatchTO();
        matchTO.setMatchId(match.getId());
        matchTO.setHometeam(match.getHometeam());
        matchTO.setForeignteam(match.getForeignteam());
        matchTO.setStarttime(match.getStarttime());
        return matchTO;
    }

}
