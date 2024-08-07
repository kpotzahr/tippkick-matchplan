package com.capgemini.csd.tippkick.spielplan.adapter;

import com.capgemini.csd.tippkick.spielplan.application.MatchService;
import com.capgemini.csd.tippkick.spielplan.domain.MatchRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ErgebnisController.class)
class ErgebnisControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private MatchRepository matchRepository;
    @MockBean
    private MatchService matchService;
    @Captor
    private ArgumentCaptor<Long> captor;

    @Test
    void shouldImportErgebnis() throws Exception {
        // given
        MockHttpServletRequestBuilder requestBuilder = post("/ergebnis/{matchId}", 100);
        requestBuilder.contentType(MediaType.APPLICATION_JSON);
        byte[] content = "{\"hometeamScore\": 1, \"foreignteamScore\": 2}".getBytes();
        requestBuilder.content(content);
        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andExpect(status().isOk());
        verify(matchService, times(1)).setResultForMatch(captor.capture(), anyInt(), anyInt());
        Long matchId = captor.getValue();
        assertThat(matchId).isNotNull();
        assertThat(matchId).isEqualTo(100);
    }

}