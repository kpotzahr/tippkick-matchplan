package com.capgemini.csd.tippkick.spielplan.cukes.steps;

import com.capgemini.csd.tippkick.spielplan.cukes.common.KafkaReceiver;
import com.capgemini.csd.tippkick.spielplan.cukes.common.StepVariables;
import com.fasterxml.jackson.databind.ObjectMapper;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import lombok.*;
import org.springframework.boot.test.web.client.TestRestTemplate;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
public class MatchEventSteps {
    private final KafkaReceiver kafkaReceiver;
    private final StepVariables stepVariables;
    private final TestRestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    private Result result;

    @Then("^es wird ein korrektes Start-Event gesendet$")
    public void esWirdEinStartEventGesendet() throws IOException {
        List<String> events = kafkaReceiver.getReceivedEventsWithTimeout(1);
        assertThat(events).hasSize(1);

        StartEvent event = objectMapper.readValue(events.get(0), StartEvent.class);
        assertThat(event.getForeignteam()).isEqualTo(stepVariables.getCurrentMatch().getForeignteam());
        assertThat(event.getHometeam()).isEqualTo(stepVariables.getCurrentMatch().getHometeam());
        assertThat(event.getStarttime()).isEqualTo(stepVariables.getCurrentMatch().getStarttime());
    }

    @When("^ich setze das Resultat auf (\\d+):(\\d+)$")
    public void ichSetzeDasResultatAuf(int hometeamScore, int foreignteamScore)  {
        result = Result.builder()
                .hometeamScore(hometeamScore)
                .foreignteamScore(foreignteamScore)
                .build();
        Long matchId = stepVariables.getCurrentMatch().getMatchId();
        restTemplate.postForEntity(
                "/ergebnis/" + matchId, result, Void.class);
    }

    @Then("^das Ergbnis wird über ein Event bekanntgegeben$")
    public void resultPublished() throws Throwable {
        List<String> events = kafkaReceiver.getReceivedEventsWithTimeout(1);
        assertThat(events).hasSize(1);

        FinishEvent event = objectMapper.readValue(events.get(0), FinishEvent.class);
        assertThat(event.getResult()).isEqualTo(result);
        assertThat(event.getMatchId()).isEqualTo(stepVariables.getCurrentMatch().getMatchId());
    }

    @Data
    private static class StartEvent {
        private long matchId;
        private String hometeam;
        private String foreignteam;
        private Instant starttime;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    private static class Result {
        private int foreignteamScore;
        private int hometeamScore;
    }


    @Data
    @NoArgsConstructor
    private static class FinishEvent {
        private long matchId;
        private Result result;
    }


}
