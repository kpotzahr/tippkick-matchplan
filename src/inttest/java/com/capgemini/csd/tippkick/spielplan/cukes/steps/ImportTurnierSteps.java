package com.capgemini.csd.tippkick.spielplan.cukes.steps;

import com.capgemini.csd.tippkick.spielplan.cukes.common.StepVariables;
import com.capgemini.csd.tippkick.spielplan.cukes.to.MatchTestTO;
import com.capgemini.csd.tippkick.spielplan.cukes.to.TurnierdatenImportTestTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RequiredArgsConstructor
public class ImportTurnierSteps {
    private final static DateTimeFormatter START_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
    private final TestRestTemplate restTemplate;
    private final StepVariables stepVariables;
    private final ObjectMapper objectMapper;

    private List<MatchTestTO> importedMatches = new ArrayList<>();

    @When("^ich importiere ein minimales Turnier mit einem Spiel$")
    public void importTestturnier() throws IOException {
        importedMatches.add(MatchTestTO.builder()
                .hometeam("GER")
                .foreignteam("BRA")
                .starttime(Instant.now()
                        .plus(5, ChronoUnit.DAYS)
                        .atZone(ZoneId.of("UTC")).format(START_TIME_FORMAT))
                .build());

        importMatches();
    }

    @When("^ich importiere ein Spiel, das gleich startet$")
    public void ichImporiereEinSpielDasGleichStartet() throws IOException {
        importedMatches.add(MatchTestTO.builder()
                .hometeam("GER")
                .foreignteam("BRA")
                .starttime(Instant.now()
                        .plus(8, ChronoUnit.SECONDS)
                        .atZone(ZoneId.of("UTC")).format(START_TIME_FORMAT))
                .build());

        importMatches();
    }

    @Then("^der Spielplan besteht aus dem importierten Spiel$")
    public void spielplanContainsImportedMatch() throws IOException {
        ResponseEntity<String> response = restTemplate.getForEntity("/spielplan", String.class);
        assertThat(response.getStatusCodeValue()).isEqualTo(200);

        List<MatchTestTO> matches = objectMapper.readValue(
                response.getBody(), new TypeReference<List<MatchTestTO>>() {
                });
        assertThat(matches).containsExactlyElementsOf(importedMatches);
    }

    private void importMatches() throws IOException {
        TurnierdatenImportTestTO tournament = new TurnierdatenImportTestTO();
        tournament.setMatches(importedMatches);

        ResponseEntity<Void> response = restTemplate.postForEntity("/turnierdaten", tournament, Void.class);
        if (!tournament.getMatches().isEmpty()) {
            stepVariables.setCurrentMatch(tournament.getMatches().get(0));
            storeId();
        }
        log.info("Tournament Response: " + response.getBody() + " " + response.getStatusCodeValue());
    }

    private void storeId() throws IOException {
        ResponseEntity<String> response = restTemplate.getForEntity("/spielplan", String.class);

        List<MatchTestTO> matches = objectMapper.readValue(
                response.getBody(), new TypeReference<List<MatchTestTO>>() {
                });
        Optional<MatchTestTO> currentMatchOptional = matches.stream().filter(
                m -> m.hasSameValues(stepVariables.getCurrentMatch())).findFirst();
        currentMatchOptional.ifPresent(matchTestTO ->
                stepVariables.getCurrentMatch().setMatchId(matchTestTO.getMatchId()));
    }


}
