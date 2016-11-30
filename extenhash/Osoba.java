/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package extenhash;

import java.util.BitSet;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 *
 * @author Jožko
 */
public class Osoba extends Record {

    private char meno;
    private int cis;
    private String key;

    public Osoba(char meno, String key) {

        this.meno = meno;
        this.key = key;
        this.cis = 5;

    }

    public Osoba() {

    }

    @Override
    protected BitSet getHash(int pocet) {

        String str = Integer.toBinaryString(cis);
        BitSet bs = new BitSet();
        return bs;
    }

    @Override
    protected byte[] getByteArray() {
        ByteArrayOutputStream hlpByteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream hlpOutStream = new DataOutputStream(hlpByteArrayOutputStream);

        try {
            hlpOutStream.writeChar(meno);
            hlpOutStream.writeInt(cis);

            return hlpByteArrayOutputStream.toByteArray();

        } catch (IOException e) {
            throw new IllegalStateException("Error during conversion to byte array.");
        }
    }

    @Override
    protected void fromByteArray(byte[] paArray) {
        ByteArrayInputStream hlpByteArrayInputStream = new ByteArrayInputStream(paArray);
        DataInputStream hlpInStream = new DataInputStream(hlpByteArrayInputStream);

        try {
            this.meno = hlpInStream.readChar();
            this.cis = hlpInStream.readInt();

        } catch (IOException e) {
            throw new IllegalStateException("Error during conversion from byte array.");
        }
    }

    @Override
    public int getSize() {
        return 6;
    }

    @Override
    protected boolean equals(Record record) {
        Osoba os = (Osoba) record;
        return meno == os.getMeno();
    }

    public char getMeno() {
        return meno;
    }

    public int getCis() {
        return cis;
    }

    @Override
    public String toString() {
        return meno + " " + cis;
    }

    @Override
    String getHas() {
        /*String str = Integer.toBinaryString(cis);
        if (str.length() >= 8) {

            return str;
        } else {

            int pom = 8 - str.length();
            String s = "00000000";
            s = s.substring(0, pom);
            return s + str;
        }*/
        return key;
    }
    
    
    

}
