package org.rinha;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.UUID;

@QuarkusTestResource(H2DatabaseTestResource.class)
public class TestResources
{

    /**
     * Converte uma string UUID para binary (16).
     * Implementação da funcção do mysql UUID_TO_BIN para o h2.
     *
     * @param uuidStr UUID
     * @return um binary (16) do uuid
     */
    public static byte[] uuidToBin(String uuidStr)
    {
        UUID uuid = UUID.fromString(uuidStr);
        byte[] uuidBin = new byte[16];
        ByteBuffer.wrap(uuidBin)
                .order(ByteOrder.BIG_ENDIAN)
                .putLong(uuid.getMostSignificantBits())
                .putLong(uuid.getLeastSignificantBits());

        return uuidBin;
    }

    public static String binToUuid(byte[] bytes)
    {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        return new UUID(buffer.getLong(), buffer.getLong()).toString();
    }
}
