package com.capgemini.csd.tippkick.spielplan.application;

import com.capgemini.csd.tippkick.spielplan.domain.Match;
import com.capgemini.csd.tippkick.spielplan.domain.MatchRepository;
import com.capgemini.csd.tippkick.spielplan.domain.Result;
import com.capgemini.csd.tippkick.spielplan.domain.events.MatchFinishedEvent;
import com.capgemini.csd.tippkick.spielplan.domain.events.MatchStartedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@Slf4j
public class MatchService {

    @Autowired
    private SpielplanScheduler spielplanScheduler;
    @Autowired
    private MatchRepository matchRepository;
    @Autowired
    private KafkaTemplate<Long, Object> sender;

    public void init() {
        log.info("Refreshing Spielplan Scheduler");
        spielplanScheduler.refreshScheduler();
    }

    void onMatchStarted(Match match) {
        log.info("Match started: {}", match);
        MatchStartedEvent matchStartedEvent = new MatchStartedEvent(match);
        sender.send(matchStartedEvent.getTopic(), match.getId(), matchStartedEvent);
    }

    public void importTurnierdata(Set<Match> matches) {
        matchRepository.saveAll(matches);
        spielplanScheduler.refreshScheduler();
        log.info("Imported Turnierdata {}", matches);
    }

    public void setResultForMatch(Long matchId, int hometeamScore, int foreignteamScore) {
        log.info("Setting result on match {} {}:{}", matchId, hometeamScore, foreignteamScore);
        Match match = matchRepository.findById(matchId).orElseThrow(MatchNotFoundException::new);
        match.setResult(new Result(hometeamScore, foreignteamScore));
        matchRepository.save(match);
        MatchFinishedEvent matchFinishedEvent = new MatchFinishedEvent(match);
        sender.send(matchFinishedEvent.getTopic(), matchId, matchFinishedEvent);
    }

    public void forceStartGame(Long id) {
        log.info("Force starting match with id: {}", id);
        Match match = matchRepository.findById(id).orElseThrow(MatchNotFoundException::new);
        onMatchStarted(match);
    }
}
