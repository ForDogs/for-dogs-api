package com.fordogs.core.util;

import com.fasterxml.uuid.Generators;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UUIDGenerator {

    private static final String UUID_SPLIT = "-";

    public static UUID generateSequentialUUID() {
        UUID uuid = Generators.timeBasedGenerator().generate();
        String[] uuidSplitArray = uuid.toString().split(UUID_SPLIT);
        String sequentialUUIDString = uuidSplitArray[2] + uuidSplitArray[1] + uuidSplitArray[0] + uuidSplitArray[3] + uuidSplitArray[4];

        return UUID.fromString(
                new StringBuilder(sequentialUUIDString)
                        .insert(8, UUID_SPLIT)
                        .insert(13, UUID_SPLIT)
                        .insert(18, UUID_SPLIT)
                        .insert(23, UUID_SPLIT)
                        .toString()
        );
    }
}
