/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import extenhash.ExtentHash;
import extenhash.Record;
import java.util.ArrayList;
import java.util.BitSet;
import model.Osoba;
import java.util.Calendar;

/**
 *
 * @author Jožko
 */
public class Main {

    public static int block = 50;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        boolean debg = false;
        if (debg) {
            for (int i = 1; i < 22; i++) {
                BitSet bs = BitSet.valueOf(String.valueOf(i).getBytes());
                System.out.println(i + " " + bs.toString() + " " + getIndex(bs, 1));
            }
        } else {
            new gui.Gui().setVisible(true);
        }
    }

    public static int getIndex(BitSet bs, int paHlbka) {

        int num = 0;
        int index = paHlbka - 1;
        for (int i = 0; i < paHlbka; i++) {
            if (bs.get(i)) {
                num += Math.pow(2, index);
            }
            index--;
        }
        return num;

    }

}
