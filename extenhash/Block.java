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
import javax.swing.tree.DefaultMutableTreeNode;

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

    private boolean load;

    private final int STORE = 4;

    public Block(int pocet, Record record) {
        this.record = new Record[pocet];
        countRec = 0;
        size = (pocet * record.getSize()) + STORE;
        recSize = record.getSize();
        load = false;

        for (int i = 0; i < this.record.length; i++) {
            this.record[i] = new Record(record.getData().newRecord());
        }

    }

    public Block(Block block) {
        this.record = new Record[block.getRecord().length];
        this.countRec = block.getCountRec();
        this.size = (block.getRecord().length * block.getRecSize()) + STORE;
        this.recSize = block.getRecSize();
        this.hlbka = block.getHlbka();
        load = false;
        for (int i = 0; i < record.length; i++) {
            this.record[i] = new Record(block.getRecord()[i].getData());
            this.record[i].setPlatny(block.getRecord()[i].isPlatny());
        }

    }

    /**
     * Pridanie do bloku
     * @param r 
     */
    public void add(Record r) {
        for (int i = 0; i < record.length; i++) {
            if (!record[i].isPlatny()) {
                record[i] = r;
                record[i].setPlatny(true);
                countRec++;
                break;
            }
        }

    }

    /**
     * Vymazanie z bloku
     * @param index 
     */
    public void remove(int index) {
        record[index].setPlatny(false);
        countRec--;

    }

    /**
     * Vycistenie bloku
     */
    public void clearRec() {
        countRec = 0;
        for (int i = 0; i < record.length; i++) {
            record[i].setPlatny(false);
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

    /**
     * Vytvorenie [] recordov z nacitanych bytov
     * @param b
     * @return 
     */
    public Record[] fromArray(byte[] b) {
        clearRec();
        int position = 0;
        int pocRecord = 0;
        byte[] pom = new byte[recSize];
        System.arraycopy(b, position, pom, 0, STORE);

        position = STORE;

        ByteArrayInputStream hlpByteArrayInputStream = new ByteArrayInputStream(pom);
        DataInputStream hlpInStream = new DataInputStream(hlpByteArrayInputStream);

        try {

            //pocRecord = hlpInStream.readInt();
            hlbka = hlpInStream.readInt();
            if (hlbka == 0) {
                hlbka = 1;
            }
            int i = 0;
            while (position < b.length) {
                /* if (countRec >= pocRecord) {
                 break;

                 }*/
                System.arraycopy(b, position, pom, 0, recSize);
                position += recSize;
                this.record[i].fromBArray(pom);
                if (this.record[i].isPlatny()) {
                    countRec++;
                }
                i++;

            }

        } catch (IOException e) {
            throw new IllegalStateException("Error during conversion from byte array.");
        }
        return record;
    }

    /**
     * Vytvorenie pola bytov na ulozenie
     * @return 
     */
    public byte[] getByteArray() {

        ByteArrayOutputStream hlpByteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream hlpOutStream = new DataOutputStream(hlpByteArrayOutputStream);

        int position = 0;
        byte[] b = new byte[size];

        try {
            // hlpOutStream.writeInt(countRec);
            hlpOutStream.writeInt(hlbka);
            System.arraycopy(hlpByteArrayOutputStream.toByteArray(), 0, b, position, STORE);
            position = STORE;
            for (Record rec : record) {
                /*if (rec.isPlatny()) {*/
                System.arraycopy(rec.getBArray(), 0, b, position, rec.getSize());
                position += rec.getSize();
                //rec.setPlatny(false);
                //countRec--;

                //}
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

    public String getAktStav() {
        String text = "Hlbka bloku: " + hlbka + "\n";
        int i = 0;
        for (Record rec : record) {
            if (rec.isPlatny()) {
                text += rec.toString() + "\n";
                remove(i);
                i++;
            }
        }
        return text;
    }

    public Block copyBlock() {

        return new Block(this);

    }

    public int getCountRec() {
        return countRec;
    }

    public void setRecord(Record[] record) {
        this.record = record;
    }

    public int getRecSize() {
        return recSize;
    }

    public boolean isLoad() {
        return load;
    }

    public void setLoad(boolean load) {
        this.load = load;
    }

    /**
     * Najdenie recordu
     * @param paRec
     * @return 
     */
    public IData find(Record paRec) {

        for (Record rec : record) {
            if (rec.isPlatny()) {
                if (paRec.getData().equals(rec)) {
                    return rec.getData();
                }
            }
        }

        return null;

    }

    /**
     * Zmena recordu
     * @param paRec
     * @return 
     */
    public boolean change(Record paRec) {

        for (Record rec : record) {
            if (paRec.getData().equals(rec)) {
                //clearRec();
                rec.setData(paRec.getData());

                return true;
            }
        }

        return false;

    }

    /**
     * Vymazanie recordu
     * @param paRec
     * @return 
     */
    public boolean remove(Record paRec) {

        for (Record rec : record) {
            if (paRec.getData().equals(rec)) {
                //clearRec();
                rec.setPlatny(false);
                countRec--;

                return true;
            }
        }

        return false;

    }

    /**
     * Vrati zoznam recordov v node
     *
     * @param adresa
     * @return
     */
    public DefaultMutableTreeNode getRecOfBlock(int adresa) {

        DefaultMutableTreeNode node
                = new DefaultMutableTreeNode(String.format("Adresa: %s, Hĺbka: %s,Počet záznamov: %d ",
                                adresa, getHlbka(), countRec));

        for (Record rec : record) {
            if (rec.isPlatny()) {
                node.add(new DefaultMutableTreeNode(rec.getData().getTreeString()));

            }
        }
        //clearRec();
        return node;

    }

    public boolean isPlatny() {
        return platny;
    }

}
