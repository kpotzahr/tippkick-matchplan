package com.capgemini.csd.tippkick.spielplan.cukes.common;

import com.capgemini.csd.tippkick.spielplan.SpielplanApplication;
import cucumber.api.java.Before;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ContextConfiguration;

import java.sql.SQLException;

@RequiredArgsConstructor
@ContextConfiguration(classes = {CucumberConfiguration.class})
public class CommonHooks {
    private static boolean isStarted = false;

    @Value("${application.url:http://localhost:7080}")
    private String spielplanUrl;
    
    private final DbAccess dbAccess;
    private final KafkaReceiver kafkaReceiver;
    private final StepVariables stepVariables;

    @Before("@cleanData")
    public void cleanupData() throws SQLException {
        dbAccess.cleanupData();
    }

    @Before("@event")
    public void startListening() {
        kafkaReceiver.start();
    }

    @Before(order = 10)
    public void startApp() {
        stepVariables.clear();
        if (!isStarted) {
            try {
                // check if application is started
                TestRestTemplate restTemplate = new TestRestTemplate();
                restTemplate.getForEntity(spielplanUrl + "/swagger-ui.html", Void.class);
            } catch (Exception exc) {
                SpielplanApplication.main(new String[]{});
            }
            isStarted = true;
        }
    }
}
