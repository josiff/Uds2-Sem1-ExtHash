/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package system;

import extenhash.Record;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import model.Osoba;

/**
 *
 * @author Jožko
 */
public class Generator {

    private final static String MP = "abcdefghijklmnopqrstuvxyz";
    private final static String VP = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private Random rnd;

    private List<IProgresUi> progresListener;

    private ArrayList<Osoba> genData;
    private ArrayList<Osoba> inserData;

    private static final int CHAR_LENGTH = 7;

    public Generator() {
        /* long seed = System.currentTimeMillis();        
         System.out.println(seed);*/
        this.rnd = new Random(1);
        genData = new ArrayList<>();
        inserData = new ArrayList<>();
        progresListener = new ArrayList<>();

    }

    public void addProgListener(IProgresUi prg) {

        progresListener.add(prg);

    }

    public void changeProgres(int count) {

        for (IProgresUi item : progresListener) {
            item.viewProgres(count);
        }
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

    public void testujData(Core ct, int pocOsob) {
        int count = 0;
        for (int i = 0; i <= 100002; i++) {
            Osoba os = new Osoba(randomString(CHAR_LENGTH),
                    randomString(CHAR_LENGTH),
                    i,
                    Calendar.getInstance(),
                    getBool(),
                    rndCis(0, 20));
            genData.add(os);

        }

        for (int i = 1; i <= pocOsob; i++) {
            String str = randomString(CHAR_LENGTH);
            Osoba os = genData.remove(rnd.nextInt(genData.size()));

            if (ct.getOsoby().insert(new Record(os))) {
                inserData.add(os);
                count++;
            }
            changeProgres(getStav(pocOsob, i));
        }

        System.out.println("Pocet " + count);

        for (Osoba os : inserData) {
            if (ct.getOsoby().find(new Record(os)) == null) {
                System.out.println("nenasiel som " + os.toString());
                break;
            }
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

    private int getStav(int pocIter, int aktIter) {

        double vys = 100.0 * aktIter / pocIter;
        return (int) vys;

    }

}
