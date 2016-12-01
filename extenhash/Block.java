/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package extenhash;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 *
 * @author Jožko
 */
public class Block {

    private Record[] record;
    private int countRec;
    private int size;
    private boolean platny;
    private int recSize;
    private int hlbka;

    private final int STORE = 8;

    public Block(int pocet, Record record) {
        this.record = new Record[pocet];
        countRec = 0;
        size = (pocet * record.getSize()) + STORE;
        recSize = record.getSize(); 

        for (int i = 0; i < this.record.length; i++) {
            this.record[i] = record.newRecord();
        }

    }

    public Block() {
        

    }

    public void add(Record r) {
        for (int i = 0; i < record.length; i++) {
            if (record[i] == null) {
                record[i] = r;
                countRec++;
                break;
            }
        }

    }

    public void remove(int index) {
        record[index] = null;
        countRec--;

    }

    public void clearRec() {
        countRec = 0;
        for (int i = 0; i < record.length; i++) {
            record[i] = null;
        }

    }

    public Record[] getRecord() {
        return record;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void fromArray(byte[] b) {

        
        int position = 0;
        int pocRecord = 0;
        byte[] pom = new byte[STORE];
        System.arraycopy(b, position, pom, 0, STORE);

        position = STORE;

        ByteArrayInputStream hlpByteArrayInputStream = new ByteArrayInputStream(pom);
        DataInputStream hlpInStream = new DataInputStream(hlpByteArrayInputStream);

        try {

            pocRecord = hlpInStream.readInt();
            hlbka = hlpInStream.readInt();
            if (hlbka == 0) {
                hlbka = 1;
            }
            int i = 0;
            while (position < b.length) {
                if (countRec >= pocRecord) {
                    break;
                }
                System.arraycopy(b, position, pom, 0, recSize);
                position += recSize;
                record[i].fromByteArray(pom);
                i++;

            }

        } catch (IOException e) {
            throw new IllegalStateException("Error during conversion from byte array.");
        }

    }

    public byte[] getByteArray() {

        ByteArrayOutputStream hlpByteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream hlpOutStream = new DataOutputStream(hlpByteArrayOutputStream);

        int position = 0;
        byte[] b = new byte[size];

        try {
            hlpOutStream.writeInt(countRec);
            hlpOutStream.writeInt(hlbka);
            System.arraycopy(hlpByteArrayOutputStream.toByteArray(), 0, b, position, STORE);
            position = STORE;
            for (Record rec : record) {
                if (rec != null) {
                    System.arraycopy(rec.getByteArray(), 0, b, position, rec.getSize());
                    position += rec.getSize();

                }
            }

        } catch (IOException e) {
            throw new IllegalStateException("Error during conversion to byte array.");
        }

        return b;

    }

    public boolean isFull() {

        return countRec == record.length;
    }

    public boolean isEmpty() {

        return countRec == 0;
    }

    public int getHlbka() {
        return hlbka;
    }

    public void setHlbka(int hlbka) {
        this.hlbka = hlbka;
    }

}
