/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import extenhash.IData;
import extenhash.Record;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.BitSet;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jožko
 */
public class Vozidlo implements IData {

    private String evc;   // 7+2
    private String vin;  // 17 +2
    private int napravy; // 4
    private int hmotnost; //4
    private boolean hladane; //1
    private Calendar endStk; // 10+2
    private Calendar endEk; // 10+2

    SimpleDateFormat sf;

    public Vozidlo(String evc, String vin, int napravy, int hmotnost, boolean hladane, Calendar endStk, Calendar endEk) {
        this.evc = evc;
        this.vin = vin;
        this.napravy = napravy;
        this.hmotnost = hmotnost;
        this.hladane = hladane;
        this.endStk = endStk;
        this.endEk = endEk;

        this.sf = new SimpleDateFormat("dd.MM.yyyy");
    }

    public Vozidlo() {
    }

    public Vozidlo(String evc) {
        this.evc = evc;
    }

    @Override
    public BitSet getHash() {

        return BitSet.valueOf(evc.getBytes());

    }

    @Override
    public byte[] getByteArray() {

        ByteArrayOutputStream hlpByteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream hlpOutStream = new DataOutputStream(hlpByteArrayOutputStream);

        try {

            hlpOutStream.writeUTF(String.format("%7s", this.evc));
            hlpOutStream.writeUTF(String.format("%17s", this.vin));
            hlpOutStream.writeInt(this.napravy);
            hlpOutStream.writeInt(this.hmotnost);
            hlpOutStream.writeBoolean(this.hladane);
            hlpOutStream.writeUTF(sf.format(endStk.getTime()));
            hlpOutStream.writeUTF(sf.format(endEk.getTime()));

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
            this.evc = hlpInStream.readUTF().trim();
            this.vin = hlpInStream.readUTF().trim();
            this.napravy = hlpInStream.readInt();
            this.hmotnost = hlpInStream.readInt();
            this.hladane = hlpInStream.readBoolean();
            this.endStk = Calendar.getInstance();
            this.endStk.setTime(sf.parse(hlpInStream.readUTF()));

           /* this.endEk = Calendar.getInstance();
            this.endEk.setTime(sf.parse(hlpInStream.readUTF()));*/

        } catch (IOException e) {
            throw new IllegalStateException("Error during conversion from byte array.");
        } catch (ParseException ex) {
            Logger.getLogger(Osoba.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public int getSize() {
        return 61;
    }

    @Override
    public boolean equals(Record record) {
        Vozidlo vz = (Vozidlo) record.getData();
        return this.evc.equals(vz.getEvc());
    }

    @Override
    public IData newRecord() {
        return new Vozidlo();
    }

    public String getEvc() {
        return evc;
    }

    public void setEvc(String evc) {
        this.evc = evc;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public int getNapravy() {
        return napravy;
    }

    public void setNapravy(int napravy) {
        this.napravy = napravy;
    }

    public int getHmotnost() {
        return hmotnost;
    }

    public void setHmotnost(int hmotnost) {
        this.hmotnost = hmotnost;
    }

    public boolean isHladane() {
        return hladane;
    }

    public void setHladane(boolean hladane) {
        this.hladane = hladane;
    }

    public Calendar getEndStk() {
        return endStk;
    }

    public void setEndStk(Calendar endStk) {
        this.endStk = endStk;
    }

    public Calendar getEndEk() {
        return endEk;
    }

    public void setEndEk(Calendar endEk) {
        this.endEk = endEk;
    }

    @Override
    public String getTreeString() {
        return String.format("Evc: %s, VIN: %s", evc, vin);
    }

    @Override
    public String toString() {
        String txt = "Vozidlo EVČ: " + evc + " VIN: " + vin + "\n";
        txt += "Koniec platnosti STK: " + sf.format(endStk.getTime()) + "\n";
        txt += "Koniec platnosti EK: " + sf.format(endEk.getTime()) + "\n";
        txt += "Hladané: " + (hladane == true ? "áno" : "nie") + "\n";
        txt += "Hmotnoť:" + hmotnost + " Počet náprav: " + napravy;

        return txt;
    }

}
