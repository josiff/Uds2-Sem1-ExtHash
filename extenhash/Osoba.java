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
public class Osoba implements IData {

    private String meno;
    private int cis;
    private String key;

    public Osoba(String meno, String key) {

        this.meno = meno;
        this.key = key;
        this.cis = 5;

    }

    public Osoba() {

    }

    @Override
    public BitSet getHash(int pocet) {

        String str = Integer.toBinaryString(cis);
        BitSet bs = new BitSet();
        return bs;
    }

    @Override
    public byte[] getByteArray() {
        ByteArrayOutputStream hlpByteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream hlpOutStream = new DataOutputStream(hlpByteArrayOutputStream);

        try {
            hlpOutStream.writeInt(cis);
            hlpOutStream.writeUTF(String.format("%20s", this.meno));
            hlpOutStream.writeUTF(String.format("%10s", this.key));

            return hlpByteArrayOutputStream.toByteArray();

        } catch (IOException e) {
            throw new IllegalStateException("Error during conversion to byte array.");
        }
    }

    @Override
    public void fromByteArray(byte[] paArray) {
        ByteArrayInputStream hlpByteArrayInputStream = new ByteArrayInputStream(paArray);
        DataInputStream hlpInStream = new DataInputStream(hlpByteArrayInputStream);

        try {
            this.cis = hlpInStream.readInt();
            this.meno = hlpInStream.readUTF().trim();
            this.key = hlpInStream.readUTF().trim();

        } catch (IOException e) {
            throw new IllegalStateException("Error during conversion from byte array.");
        }
    }

    @Override
    public int getSize() {
        return 38; // 4 + (20+2) + (10+2)
    }

    @Override
    public boolean equals(Record record) {
        Osoba os = (Osoba) record.getData();
        return meno == os.getMeno();
    }

    public String getMeno() {
        return meno;
    }

    public int getCis() {
        return cis;
    }

    @Override
    public String toString() {
        return meno + " " + cis + " | " + key ;
    }

    @Override
    public String getHas() {
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

    @Override
    public IData newRecord() {
        return new Osoba();
    }

}
