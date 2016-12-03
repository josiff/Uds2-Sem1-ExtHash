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
import java.util.BitSet;

/**
 *
 * @author Jožko
 */
public class VozidloVIN implements IData {

    private String evc;   // 7+2
    private String vin;  // 17 +2

    public VozidloVIN() {
    }

    public VozidloVIN(String vin) {
        this.vin = vin;
    }

    public VozidloVIN(String evc, String vin) {
        this.evc = evc;
        this.vin = vin;
    }

    @Override
    public BitSet getHash() {
        return BitSet.valueOf(vin.getBytes());
    }

    @Override
    public byte[] getByteArray() {
        ByteArrayOutputStream hlpByteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream hlpOutStream = new DataOutputStream(hlpByteArrayOutputStream);

        try {

            hlpOutStream.writeUTF(String.format("%7s", this.evc));
            hlpOutStream.writeUTF(String.format("%17s", this.vin));

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

        } catch (IOException e) {
            throw new IllegalStateException("Error during conversion from byte array.");
        }
    }

    @Override
    public int getSize() {
        return 28;
    }

    @Override
    public boolean equals(Record record) {
        VozidloVIN vz = (VozidloVIN) record.getData();
        return this.vin.equals(vz.getVin());
    }

    @Override
    public IData newRecord() {
        return new VozidloVIN();
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

    @Override
    public String getTreeString() {
        return String.format("Evc: %s, VIN: %s", evc, vin);
    }

}
