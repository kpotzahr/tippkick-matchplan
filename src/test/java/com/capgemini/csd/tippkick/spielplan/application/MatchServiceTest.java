package com.capgemini.csd.tippkick.spielplan.application;

import com.capgemini.csd.tippkick.spielplan.configuration.MatchEvent;
import com.capgemini.csd.tippkick.spielplan.domain.Match;
import com.capgemini.csd.tippkick.spielplan.domain.MatchRepository;
import com.capgemini.csd.tippkick.spielplan.domain.events.MatchFinishedEvent;
import com.capgemini.csd.tippkick.spielplan.domain.events.MatchStartedEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.HashSet;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MatchServiceTest {

    @InjectMocks
    private MatchService matchService;
    @Mock
    private KafkaTemplate<Long, MatchEvent> tippkickSender;
    @Mock
    private MatchRepository matchRepository;
    @Mock
    private SpielplanScheduler spielplanScheduler;

    @Test
    void shouldPublishMatchStarted() {
        // given
        Match match = new Match();
        match.setId(1L);
        // when
        matchService.onMatchStarted(match);
        // then
        verify(tippkickSender).send(eq("match-started"), eq(1L), any(MatchStartedEvent.class));
    }

    @Test
    void shouldImportTurnierdataAndRefreshScheduler() {
        // given
        HashSet<Match> hashSet = new HashSet<>();
        Match match = new Match();
        match.setId(1L);
        hashSet.add(match);
        // when
        matchService.importTurnierdata(hashSet);
        // then
        verify(matchRepository).saveAll(any());
        verify(spielplanScheduler).refreshScheduler();
    }

    @Test
    void shouldSetResultForMatch() {
        // given
        Match match = new Match();
        match.setId(1L);
        when(matchRepository.findById(anyLong())).thenReturn(Optional.of(match));
        // when
        matchService.setResultForMatch(1L, 1, 1);
        // then
        verify(matchRepository).save(any(Match.class));
        verify(tippkickSender).send(eq("match-finished"), eq(1L), any(MatchFinishedEvent.class));
    }

    @Test
    void shouldForceStartGame() {
        // given
        Match match = new Match();
        match.setId(1L);
        when(matchRepository.findById(anyLong())).thenReturn(Optional.of(match));
        // when
        matchService.forceStartGame(1L);
        // then
        verify(tippkickSender).send(eq("match-started"), eq(1L), any(MatchStartedEvent.class));
    }
}