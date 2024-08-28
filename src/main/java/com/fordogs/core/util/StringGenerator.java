package com.fordogs.core.util;

import com.fasterxml.uuid.Generators;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

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

    public static String generatePassword() {
        int minPasswordLength = 10;
        int maxPasswordLength = 16;
        int passwordLength = (int) (Math.random() * (maxPasswordLength - minPasswordLength + 1)) + minPasswordLength;
        SecureRandom random = new SecureRandom();

        StringBuilder password = new StringBuilder();

        password.append(generateCharacter(random, 0));
        password.append(generateCharacter(random, 1));
        password.append(generateCharacter(random, 2));
        password.append(generateCharacter(random, 3));

        for (int i = 4; i < passwordLength; i++) {
            password.append(generateCharacter(random));
        }

        List<Character> passwordList = password.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.toList());
        Collections.shuffle(passwordList);

        return passwordList.stream()
                .map(String::valueOf)
                .collect(Collectors.joining());
    }

    private static char generateCharacter(SecureRandom random) {
        int charType = random.nextInt(4);
        return generateCharacter(random, charType);
    }

    private static char generateCharacter(SecureRandom random, int charType) {
        return switch (charType) {
            case 0 -> (char) (random.nextInt(26) + 'a');
            case 1 -> (char) (random.nextInt(26) + 'A');
            case 2 -> (char) (random.nextInt(10) + '0');
            default -> "!@#$%^&*()_+".charAt(random.nextInt("!@#$%^&*()_+".length()));
        };
    }
}
