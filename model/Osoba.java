/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import extenhash.IData;
import extenhash.Record;
import java.util.BitSet;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jožko
 */
public class Osoba implements IData {

    private String meno; // 35 +2
    private String przv; // 35 +2
    private int evc; // 4
    private Calendar endPlatnost; //10 +2
    private boolean zakaz; //1
    private int priestupky; //4
    SimpleDateFormat sf;

    public Osoba() {

    }

    public Osoba(int evc) {
        this.evc = evc;
    }

    public Osoba(String meno, String przv, int evc, Calendar endPlatnost, boolean zakaz, int priestupky) {
        this.meno = meno;
        this.przv = przv;
        this.evc = evc;
        this.endPlatnost = endPlatnost;
        this.zakaz = zakaz;
        this.priestupky = priestupky;
        this.sf = new SimpleDateFormat("dd.MM.yyyy");
    }

    @Override
    public BitSet getHash() {
        //todo
        byte[] bytes = String.valueOf(evc).getBytes();
        BitSet bs = BitSet.valueOf(bytes);
        

        return bs;
    }

    @Override
    public byte[] getByteArray() {
        ByteArrayOutputStream hlpByteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream hlpOutStream = new DataOutputStream(hlpByteArrayOutputStream);

        try {

            hlpOutStream.writeUTF(String.format("%35s", this.meno));
            hlpOutStream.writeUTF(String.format("%35s", this.przv));
            hlpOutStream.writeInt(evc);
            hlpOutStream.writeUTF(sf.format(endPlatnost.getTime()));
            hlpOutStream.writeBoolean(this.zakaz);
            hlpOutStream.writeInt(this.priestupky);

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
            this.meno = hlpInStream.readUTF().trim();
            this.przv = hlpInStream.readUTF().trim();
            this.evc = hlpInStream.readInt();
            this.endPlatnost = Calendar.getInstance();
            this.endPlatnost.setTime(sf.parse(hlpInStream.readUTF().trim()));
            this.zakaz = hlpInStream.readBoolean();
            this.priestupky = hlpInStream.readInt();

        } catch (IOException e) {
            throw new IllegalStateException("Error during conversion from byte array.");
        } catch (ParseException ex) {
            Logger.getLogger(Osoba.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public int getSize() {
        return 95;
    }

    @Override
    public boolean equals(Record record) {
        Osoba os = (Osoba) record.getData();
        return this.evc == os.getEvc();
    }

    public String getMeno() {
        return meno;
    }

    public int getEvc() {
        return evc;
    }

    @Override
    public String toString() {
        String txt = "Meno: " + meno + " " + przv + "\n";
        txt += "EVČ: " + evc + "\n";
        txt += "Dátum platnosti VP: " + sf.format(endPlatnost.getTime()) + "\n";
        txt += "Zákaz šoférovania: " + (zakaz == true ? "áno" : "nie") + "\n";
        txt += "Počet priestupkov:" + priestupky;

        return txt;

    }

    @Override
    public IData newRecord() {
        return new Osoba();
    }

    public String getHas() {

        StringBuilder sb = new StringBuilder();
        BitSet bs = getHash();
        int size = bs.size() - 1;
        for (int i = 0; i < bs.size(); i++) {
            sb.append(bs.get(size - i) == true ? "1" : "0");
        }
        return sb.toString();
    }

    @Override
    public String getTreeString() {
        return meno + " " + przv + " " + evc;
        //return getHas();
    }

}
