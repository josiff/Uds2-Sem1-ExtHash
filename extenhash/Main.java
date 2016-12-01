/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package extenhash;

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

        
        ExtentHash ex = new ExtentHash("Osoby.txt", new Osoba(),5);

        Record os = new Osoba('Z', "00000000");
        ex.insert(os);
        os = new Osoba('K', "01100100");
        System.out.println(os.toString());
        ex.insert(os);
        os = new Osoba('M', "10010101");
        System.out.println(os.toString());
        ex.insert(os);
        os = new Osoba('L', "10111011");
        System.out.println(os.toString());
        ex.insert(os);
        os = new Osoba('T', "10100101");
        System.out.println(os.toString());
        ex.insert(os);
        os = new Osoba('S', "10110110");
        System.out.println(os.toString());
        ex.insert(os);
        os = new Osoba('E', "10100000");
        System.out.println(os.toString());
        ex.insert(os);

        os = new Osoba('N', "01101100");
        System.out.println(os.toString());
        ex.insert(os);
        os = new Osoba('P', "00000000");
        System.out.println(os.toString());
        ex.insert(os);
        os = new Osoba('L', "01100100");
        System.out.println(os.toString());
        ex.insert(os);
        os = new Osoba('Z', "11101001");
        System.out.println(os.toString());
       ex.insert(os);
        os = new Osoba('P', "11110000");
        System.out.println(os.toString());
        ex.insert(os);
        os = new Osoba('U', "10110111");
        System.out.println(os.toString());
        ex.insert(os);
        os = new Osoba('I', "00001111");
        System.out.println(os.toString());
        ex.insert(os);
        os = new Osoba('B', "00111100");
        System.out.println(os.toString());
        ex.insert(os);

    }

}
