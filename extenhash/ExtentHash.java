/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package extenhash;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jožko
 */
public class ExtentHash {

    private LinkedList volneBloky;
    private List<Integer> adresar;
    private int hlbka;
    private RandomAccessFile raf;
    private Block block;

    public ExtentHash(String file, Record record, int pocetZaznamov) {
        block = new Block(pocetZaznamov, record);
        adresar = new ArrayList();
        hlbka = 1;
        try {
            raf = new RandomAccessFile(file, "rw");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ExtentHash.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int insert(Block block, int adresa) {

        try {
            raf.seek(adresa);
            raf.write(block.getByteArray(), 0, block.getSize());

        } catch (FileNotFoundException ex) {
            Logger.getLogger(ExtentHash.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ExtentHash.class.getName()).log(Level.SEVERE, null, ex);
        }

        return 0;
    }

    public byte[] read(int offset, int length) {

        byte[] b = new byte[length];
        try {

            raf.seek(offset);
            raf.read(b, 0, length);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(ExtentHash.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ExtentHash.class.getName()).log(Level.SEVERE, null, ex);
        }

        return b;
    }

    public void rozsirAdresy(int index) {

        int max = Collections.max(adresar);
        hlbka += 1;
        ArrayList<Integer> pom = new ArrayList<>();
        for (int i = 0; i < adresar.size(); i++) {
            if (i != index) {
                pom.add(adresar.get(i));
                pom.add(adresar.get(i));
            } else {
                pom.add(adresar.get(index));
                pom.add(block.getSize() + max);

            }
        }
        adresar = pom;
    }

    public int getHlbka() {
        return hlbka;
    }

    public void insert(Record record) {
        block.setHlbka(hlbka);
        boolean flag = false;
        int adresa = 0;
        int index = 0;
        while (flag == false) {

            /*todo hash*/
            index = getIndex(record);

            if (adresar.isEmpty()) {
                if (index == 0) {
                    adresar.add(0);
                    adresar.add(block.getSize());
                } else {
                    adresar.add(block.getSize());
                    adresar.add(0);
                }
                block.getRecord()[1] = record;
                insert(block, index * block.getSize());
                flag = true;
            } else {

                adresa = adresar.get(index);

                block.fromArray(read(adresa, block.getSize()));

                if (block.isFull()) {

                    if (block.getHlbka() == getHlbka()) {

                        rozsirAdresy(index);
                        block.setHlbka(hlbka);
                        //ulozim zmenenu hlbku
                        // insert(block, adresa);

                    }
                    rozdelBlock(block, index);

                } else {
                    block.add(record);
                    insert(block, adresa);
                    flag = true;

                }

            }
        }

    }

    public void rozdelBlock(Block paBlock, int index) {
        Block b = null; //new Block(paBlock.getSize());
        int novaAdres = 0;
        b.setHlbka(hlbka);
        Record[] rec = block.getRecord();
        for (int i = 0; i < rec.length; i++) {

            if (getIndex(rec[i]) > index) {
                novaAdres = getIndex(rec[i]);
                b.add(rec[i]);
                block.remove(i);
            }
        }
        if (!b.isEmpty()) {
            insert(block, adresar.get(novaAdres));
        }
        insert(paBlock, adresar.get(index));
    }

    public int getIndex(Record record) {
        String s = record.getHas();
        s = s.substring(0, hlbka);
        return Integer.parseInt(s, 2);

    }

}
