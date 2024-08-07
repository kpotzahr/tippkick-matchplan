package com.capgemini.csd.tippkick.spielplan.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.LongSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConfig {

    @Autowired
    private KafkaProperties kafkaProperties;
    @Autowired
    private ObjectMapper objectMapper;

    @Bean
    public DefaultKafkaProducerFactory<?, ?> defaultKafkaProducerFactory() {
        Map<String, Object> producerConfig = kafkaProperties.buildProducerProperties();
        return new DefaultKafkaProducerFactory<>(
                producerConfig,
                new LongSerializer(),
                new JsonSerializer<>(objectMapper)
        );
    }
}
