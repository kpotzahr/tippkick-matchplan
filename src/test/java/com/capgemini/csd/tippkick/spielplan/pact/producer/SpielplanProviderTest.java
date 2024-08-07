package com.capgemini.csd.tippkick.spielplan.pact.producer;

import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.State;
import au.com.dius.pact.provider.junit.loader.PactFolder;
import au.com.dius.pact.provider.junit5.HttpTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;
import com.capgemini.csd.tippkick.spielplan.domain.Match;
import com.capgemini.csd.tippkick.spielplan.domain.MatchRepository;
import com.capgemini.csd.tippkick.spielplan.domain.Participant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Instant;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@Provider("spielplan")
@PactFolder("pacts")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        properties = {"h2.tcp.enabled=false", "server.port=7088"})
public class SpielplanProviderTest {
    @MockBean
    private MatchRepository matchRepository;

    @BeforeEach
    void setupTestTarget(PactVerificationContext context) {
        context.setTarget(new HttpTestTarget("localhost", 7088));
    }

    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider.class)
    void pactVerificationTestTemplate(PactVerificationContext context) {
        context.verifyInteraction();
    }

    @State("match with id 1 exists")
    public void match1Exists() {
        Match match = new Match();
        match.setForeignteam(Participant.BEL);
        match.setHometeam(Participant.GER);
        match.setStarttime(Instant.now());
        match.setId(1L);

        Mockito.when(matchRepository.findById(1L)).thenReturn(Optional.of(match));
    }

    @State("match with id 2 does not exist")
    public void match2doesNotExist() {
        Mockito.when(matchRepository.findById(2L)).thenReturn(Optional.empty());
    }

}
