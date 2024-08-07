package com.capgemini.csd.tippkick.spielplan.adapter;

import com.capgemini.csd.tippkick.spielplan.application.MatchService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {
    @Autowired
    private MatchService matchService;

    @ApiOperation(value = "Force start given match even if the start time has not been reached.")
    @PostMapping("/admin/start/{matchId}")
    public void forceStartGame(@PathVariable("matchId") Long matchId) {
        matchService.forceStartGame(matchId);
    }
}
