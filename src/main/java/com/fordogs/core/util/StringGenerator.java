package com.fordogs.core.util;

import com.fasterxml.uuid.Generators;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Random;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StringGenerator {

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

    public static String generate4DigitString() {
        Random random = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();

        for (int i = 0; i < 4; i++) {
            int randomOption = random.nextInt(2);
            if (randomOption == 0) {
                int randomNumber = random.nextInt(10);
                randomStringBuilder.append(randomNumber);
            } else {
                char randomLetter = (char) (random.nextInt(26) + 'A');
                randomStringBuilder.append(randomLetter);
            }
        }

        return randomStringBuilder.toString();
    }
}
