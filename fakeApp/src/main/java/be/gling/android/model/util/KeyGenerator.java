package be.gling.android.model.util;

import java.util.Random;

public class KeyGenerator {

    private static final int PASSWORD_NB_LETTERS =8;

    private static final String LETTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

    private static final String LETTERS_LITTLE = "abcdefghijklmnopqrstuvwxyz";

    public static String generateRandomKey(final int nbLetter) {

        String result = "";

        final Random rand = new Random();
        for (int i = 0; i < nbLetter; i++) {
            result += LETTERS.charAt(rand.nextInt(LETTERS.length()));
        }

        return result;
    }


    public static String generateRandomPassword() {

        String result = "";

        final Random rand = new Random();

        int cycleNb = 0;
        int cycleNbExpected = 2;
        boolean letterCycle = true;

        while (result.length() < PASSWORD_NB_LETTERS) {
            if (cycleNb >= cycleNbExpected) {
                letterCycle = !letterCycle;
                cycleNb=0;
            }

            if(letterCycle){
                result += LETTERS_LITTLE.charAt(rand.nextInt(LETTERS_LITTLE.length()));
            }
            else{
                result+=rand.nextInt(9)+"";
            }
            cycleNb++;
        }

        return result;
    }

}
