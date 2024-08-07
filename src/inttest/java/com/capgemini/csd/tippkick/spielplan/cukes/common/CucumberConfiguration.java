package com.capgemini.csd.tippkick.spielplan.cukes.common;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
@ComponentScan("com.capgemini.csd.tippkick.spielplan.cukes.*")
public class CucumberConfiguration {
    @Value("${spring.embedded.kafka.brokers:localhost:9092}")
    private String kafkaUrl;

    @Bean
    public TestRestTemplate restTemplate(@Value("${spielplan.url:http://localhost:7080}") String appUrl) {
        TestRestTemplate restTemplate = new TestRestTemplate();
        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(appUrl));
        return restTemplate;
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JavaTimeModule module = new JavaTimeModule();
        objectMapper.registerModule(module);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        return objectMapper;
    }


    @Bean
    public KafkaReceiver kafkaReceiver() {
        return new KafkaReceiver(kafkaUrl, "match-started", "match-finished");
    }
}
