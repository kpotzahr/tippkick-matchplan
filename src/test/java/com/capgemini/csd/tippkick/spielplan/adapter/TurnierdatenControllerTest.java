package com.capgemini.csd.tippkick.spielplan.adapter;

import com.capgemini.csd.tippkick.spielplan.adapter.to.MatchTO;
import com.capgemini.csd.tippkick.spielplan.adapter.to.TurnierdatenImportTO;
import com.capgemini.csd.tippkick.spielplan.application.MatchService;
import com.capgemini.csd.tippkick.spielplan.domain.Match;
import com.capgemini.csd.tippkick.spielplan.domain.MatchRepository;
import com.capgemini.csd.tippkick.spielplan.domain.Participant;
import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


@ExtendWith(SpringExtension.class)
class TurnierdatenControllerTest {

    private static final Instant STARTTIME = LocalDateTime.of(2018, 1, 1, 1, 1).toInstant(ZoneOffset.UTC);
    @InjectMocks
    private TurnierdatenController turnierdatenController;
    @Mock
    private MatchRepository matchRepository;
    @Mock
    private MatchService matchService;
    @Captor
    private ArgumentCaptor<Set<Match>> matchArgumentCaptor;

    @Test
    void shouldImportTurnierdaten() {
        // given
        TurnierdatenImportTO importTO = constructTurnierdatenTo();
        // when
        turnierdatenController.importTurnierdaten(importTO);
        // then
        verify(matchService).importTurnierdata(matchArgumentCaptor.capture());
        verifyNoMoreInteractions(matchRepository);
        Set<Match> matches = matchArgumentCaptor.getValue();
        assertThat(matches).hasSize(1);
        Match match = matches.stream().findFirst().orElseThrow(NullPointerException::new);
        assertThat(match.getForeignteam()).isEqualTo(Participant.BRA);
        assertThat(match.getHometeam()).isEqualTo(Participant.GER);
        assertThat(match.getStarttime()).isEqualTo(STARTTIME);
    }

    @Test
    void shouldReturnSpielplan() {
        // given
        Match match = new Match();
        match.setStarttime(STARTTIME);
        match.setForeignteam(Participant.GER);
        match.setHometeam(Participant.BRA);
        when(matchRepository.findAll()).thenReturn(ImmutableList.of(match));
        // when
        List<MatchTO> spielplan = turnierdatenController.getSpielplan();
        // then
        assertThat(spielplan).hasSize(1);
        assertThat(spielplan.get(0).getForeignteam()).isEqualTo(Participant.GER);
        assertThat(spielplan.get(0).getHometeam()).isEqualTo(Participant.BRA);
        assertThat(spielplan.get(0).getStarttime()).isEqualTo(STARTTIME);
    }

    private TurnierdatenImportTO constructTurnierdatenTo() {
        TurnierdatenImportTO importTO = new TurnierdatenImportTO();
        MatchTO matchTO = new MatchTO();
        matchTO.setStarttime(STARTTIME);
        matchTO.setForeignteam(Participant.BRA);
        matchTO.setHometeam(Participant.GER);
        importTO.setMatches(Collections.singletonList(matchTO));
        return importTO;
    }

}