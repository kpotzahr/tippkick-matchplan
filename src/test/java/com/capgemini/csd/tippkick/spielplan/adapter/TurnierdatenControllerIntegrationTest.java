package com.capgemini.csd.tippkick.spielplan.adapter;

import com.capgemini.csd.tippkick.spielplan.application.MatchService;
import com.capgemini.csd.tippkick.spielplan.domain.Match;
import com.capgemini.csd.tippkick.spielplan.domain.MatchRepository;
import com.capgemini.csd.tippkick.spielplan.domain.Participant;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TurnierdatenController.class)
class TurnierdatenControllerIntegrationTest {

    private static final Participant MATCH1_HOMETEAM = Participant.GER;
    private static final Participant MATCH1_FOREIGNTEAM = Participant.BRA;
    private static final Instant MATCH1_STARTTIME = Instant.parse("2018-06-30T03:00:00Z");

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MatchRepository matchRepository;

    @MockBean
    private MatchService matchService;

    @Value("classpath:test-turnierdaten.json")
    private Resource res;

    @Captor
    private ArgumentCaptor<Set<Match>> captor;

    private static byte[] getBytesFromInputStream(InputStream is) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buffer = new byte[0xFFFF];
        for (int len = is.read(buffer); len != -1; len = is.read(buffer)) {
            os.write(buffer, 0, len);
        }
        return os.toByteArray();
    }

    @Test
    void shouldUploadTurnierdaten() throws Exception {
        // given
        MockHttpServletRequestBuilder requestBuilder = post("/turnierdaten");
        requestBuilder.contentType(MediaType.APPLICATION_JSON);
        byte[] content = getBytesFromInputStream(res.getInputStream());
        requestBuilder.content(content);
        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andExpect(status().isOk());
        verify(matchService, times(1)).importTurnierdata(captor.capture());
        Set<Match> matchesToStore = captor.getValue();
        assertThat(matchesToStore).isNotNull();
        assertThat(matchesToStore).hasSize(2);
        Match match1 = matchesToStore.stream().filter(m -> Participant.GER.equals(m.getHometeam())).findFirst().orElse(null);
        assertThat(match1).isNotNull();
        assertThat(match1.getHometeam()).isEqualTo(MATCH1_HOMETEAM);
        assertThat(match1.getForeignteam()).isEqualTo(MATCH1_FOREIGNTEAM);
        assertThat(match1.getStarttime()).isEqualTo(MATCH1_STARTTIME);
    }

    @Test
    void shouldReturnMatchData() throws Exception {
        // given
        MockHttpServletRequestBuilder requestBuilder = get("/spielplan");
        requestBuilder.contentType(MediaType.APPLICATION_JSON);
        Match match = new Match();
        match.setForeignteam(MATCH1_FOREIGNTEAM);
        match.setHometeam(MATCH1_HOMETEAM);
        match.setStarttime(MATCH1_STARTTIME);
        when(matchRepository.findAll()).thenReturn(Collections.singletonList(match));
        String expectedJsonContent = String.format("[{\"starttime\":\"%s\",\"hometeam\":\"%s\",\"foreignteam\":\"%s\"}]", MATCH1_STARTTIME, MATCH1_HOMETEAM, MATCH1_FOREIGNTEAM);
        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJsonContent));
    }

    @Test
    void shouldReturnMatchDataOnGet() throws Exception {
        // given
        MockHttpServletRequestBuilder requestBuilder = get("/match/{matchId}", 1L);
        requestBuilder.contentType(MediaType.APPLICATION_JSON);
        Match match = new Match();
        match.setForeignteam(MATCH1_FOREIGNTEAM);
        match.setHometeam(MATCH1_HOMETEAM);
        match.setStarttime(MATCH1_STARTTIME);
        when(matchRepository.findById(1L)).thenReturn(Optional.of(match));
        String expectedJsonContent = String.format("{\"starttime\":\"%s\",\"hometeam\":\"%s\",\"foreignteam\":\"%s\"}", MATCH1_STARTTIME, MATCH1_HOMETEAM, MATCH1_FOREIGNTEAM);
        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJsonContent));
    }
}
