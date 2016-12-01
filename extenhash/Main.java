/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package extenhash;

import java.nio.charset.Charset;

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

        Record os = new Record(new Osoba("Zilina", "00000000"));
        ex.insert(os);

        os = new Record(new Osoba("Kosice", "01100100"));
        ex.insert(os);
        os = new Record(new Osoba("Martin", "10010101"));
        ex.insert(os);

        os = new Record(new Osoba("Levice", "10111011"));
        ex.insert(os);

        os = new Record(new Osoba("Trnava", "10100101"));
        ex.insert(os);
        os = new Record(new Osoba("Snina", "10110110"));
        ex.insert(os);

        os = new Record(new Osoba("Senica", "10100000"));
        ex.insert(os);

        os = new Record(new Osoba("Nitra", "01101100"));

        ex.insert(os);
        os = new Record(new Osoba("Poprad", "00000000"));

        ex.insert(os);
        os = new Record(new Osoba("Lucenec", "01100100"));

        ex.insert(os);

        os = new Record(new Osoba("Zvolen", "11101001"));
        ex.insert(os);
        os = new Record(new Osoba("Presov", "11110000"));
        ex.insert(os);

        os = new Record(new Osoba("Puchov", "10110111"));
        ex.insert(os);
        

        os = new Record(new Osoba("Ilava", "00001111"));
        
        ex.insert(os);
        os = new Record(new Osoba("Brezno", "00111100"));
       
        ex.insert(os);
        
        ex.getAktStav();

    }

}
