/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package system;

import java.util.Calendar;
import java.util.Random;

/**
 *
 * @author Jožko
 */
public class Generator {

    private final static String MP = "abcdefghijklmnopqrstuvxyz";
    private final static String VP = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private Random rnd;

    private static final int CHAR_LENGTH = 7;

    public Generator() {
        this.rnd = new Random();

    }

    public void generujData(Core ct, int pocOsob, int pocVoz) {

        for (int i = 1; i <= pocOsob; i++) {
            String str = randomString(CHAR_LENGTH);

            ct.addOsobu(randomString(CHAR_LENGTH),
                    randomString(CHAR_LENGTH),
                    i,
                    Calendar.getInstance(),
                    getBool(),
                    rndCis(0, 20));
            

        }

    }

    public String randomString(int len) {
        StringBuilder sb = new StringBuilder(len);

        sb.append(VP.charAt(rnd.nextInt(VP.length())));
        for (int i = 0; i < len; i++) {
            sb.append(MP.charAt(rnd.nextInt(MP.length())));
        }
        return sb.toString();
    }

    public boolean getBool() {

        return rnd.nextBoolean();

    }

    public static int rndCis(int min, int max) {
        Random rnd = new Random();
        return rnd.nextInt(max - min) + min;

    }

}
