package com.algaworks.algasensors.temperature.processing;

import com.fasterxml.uuid.Generators;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class UUIDv7Test {

    @Test
    void shouldGenerateUUIDv7Example () {
        UUID uuid1 = IdGenerator.generateTimeBaseUUID();
        UUID uuid2 = IdGenerator.generateTimeBaseUUID();
        UUID uuid3 = IdGenerator.generateTimeBaseUUID();
        UUID uuid4 = IdGenerator.generateTimeBaseUUID();

        System.out.println(UUIDv7Utils.extractOffsetDateTime(uuid1));
        System.out.println(UUIDv7Utils.extractOffsetDateTime(uuid2));
        System.out.println(UUIDv7Utils.extractOffsetDateTime(uuid3));
        System.out.println(UUIDv7Utils.extractOffsetDateTime(uuid4));
    }

    @Test
    void shouldGenerateUUIDv7 () {
        UUID uuid1 = IdGenerator.generateTimeBaseUUID();
        OffsetDateTime uuidDateTime = UUIDv7Utils.extractOffsetDateTime(uuid1).truncatedTo(ChronoUnit.MINUTES);
        OffsetDateTime currentDateTime = OffsetDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        System.out.println(uuidDateTime);
        System.out.println(currentDateTime);
        Assertions.assertThat(uuidDateTime).isEqualTo(currentDateTime);
    }


}
