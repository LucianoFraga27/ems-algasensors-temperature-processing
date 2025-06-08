package com.algaworks.algasensors.temperature.processing.api.controller;

import com.algaworks.algasensors.temperature.processing.api.model.TemperatureLogOutput;
import com.algaworks.algasensors.temperature.processing.common.IdGenerator;
import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;

import static com.algaworks.algasensors.temperature.processing.infrastructure.rabbitmq.RabbitMqConfig.FANOUT_EXCHEAGE_NAME;

@RestController
@RequestMapping("/api/sensors/{sensorId}/temperatures/data")
@Slf4j
@RequiredArgsConstructor
public class TemperatureProcessingController {

    private final RabbitTemplate rabbitTemplate;

    @PostMapping(consumes = MediaType.TEXT_PLAIN_VALUE)
    public void data(@PathVariable TSID sensorId, @RequestBody String input) {

        if (input == null || input.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        Double temperature;

        try {
            temperature = Double.parseDouble(input);
        } catch (NumberFormatException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        TemperatureLogOutput temperatureLogOutput = TemperatureLogOutput.builder()
                .id(IdGenerator.generateTimeBaseUUID())
                .sensorId(sensorId)
                .value(temperature)
                .registeredAt(OffsetDateTime.now()).build();

        log.info(temperatureLogOutput.toString());

        String exchange = FANOUT_EXCHEAGE_NAME;
        String routingKey = "";
        Object messagePayload = temperatureLogOutput;

        MessagePostProcessor messagePostProcessor =
                message -> {
                    message.getMessageProperties().setHeader("sensorId", temperatureLogOutput.getSensorId().toString());
                    message.getMessageProperties().setHeader("outro", "Outro qualquer");
                    return message;
                };

        rabbitTemplate.convertAndSend(exchange, routingKey, messagePayload, messagePostProcessor);

    }

}
