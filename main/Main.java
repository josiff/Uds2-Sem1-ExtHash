/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import extenhash.ExtentHash;
import extenhash.Record;
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

        ExtentHash ex = new ExtentHash("Osoby.txt", new Record(new Osoba()), 5);
        /*
         Record os = new Record(new Mesto("Zilina", "00000000"));
         ex.insert(os);
         os = new Record(new Mesto("Kosice", "01100100"));
         ex.insert(os);
         os = new Record(new Mesto("Martin", "10010101"));
         ex.insert(os);
         os = new Record(new Mesto("Levice", "10111011"));
         ex.insert(os);
         os = new Record(new Mesto("Trnava", "10100101"));
         ex.insert(os);
         os = new Record(new Mesto("Snina", "10110110"));
         ex.insert(os);
         os = new Record(new Mesto("Senica", "10100000"));
         ex.insert(os);
         os = new Record(new Mesto("Nitra", "01101100"));
         ex.insert(os);
         os = new Record(new Mesto("Poprad", "00000000"));
         ex.insert(os);
         os = new Record(new Mesto("Lucenec", "01100100"));
         ex.insert(os);
         os = new Record(new Mesto("Zvolen", "11101001"));
         ex.insert(os);
         os = new Record(new Mesto("Presov", "11110000"));
         ex.insert(os);
         os = new Record(new Mesto("Puchov", "10110111"));
         ex.insert(os);
         os = new Record(new Mesto("Ilava", "00001111"));
         ex.insert(os);   
         os = new Record(new Mesto("Brezno", "00111100"));
         ex.insert(os);
         ex.getAktStav();*/

        new gui.Gui().setVisible(true);

    }

}
