package com.algaworks.algasensors.temperature.processing.common;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

public class UUIDv7Utils
{

    private UUIDv7Utils(){}

    public static OffsetDateTime extractOffsetDateTime(UUID uuid) {
        if (uuid == null) return null;

        // getMostSignificantBits() retorna os 64 bits mais significativos do UUID
        long msb = uuid.getMostSignificantBits();

        // >>> é o operador de shift à direita sem sinal. Aqui, deslocamos 16 bits para descartar:
        // - os 4 bits da versão (posições 12 a 15)
        // - os 12 bits menos significativos que não fazem parte do timestamp
        long timestampMs = msb >>> 16;

        // Converte milissegundos desde a época UNIX para OffsetDateTime com UTC
        Instant instant = Instant.ofEpochMilli(timestampMs);
        return OffsetDateTime.ofInstant(instant, ZoneOffset.systemDefault());

        // ex: uuid = 018f14f2-5b00-7cc0-8000-93b33fb1e5a5
        // uuid.getMostSignificantBits() => 018f14f2-5b00-7cc0
        //  hexadecimal dos valores:
        //  018f14f2 = 0x018F14F2
        //  5b00     = 0x5B00
        //  7cc0     = 0x7CC0
        // juntandos todos os bits:
        // 0x018F14F25B007CC0  = 111000111110000010110110000000111110001100000000 (binário simplificado)
        // msb >>> 16 : Esse deslocamento pega os 48 bits mais significativos, que representam o timestamp em milissegundos.
    }


}
