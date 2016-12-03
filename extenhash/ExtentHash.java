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
import java.util.BitSet;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

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

    private String file;

    public ExtentHash(String file, Record record, int pocetZaznamov) {
        block = new Block(pocetZaznamov, record);
        adresar = new ArrayList();
        hlbka = 1;
        this.file = file;
        try {
            raf = new RandomAccessFile(file, "rw");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ExtentHash.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private int insert(Block block, int adresa) {

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

        hlbka += 1;
        ArrayList<Integer> pom = new ArrayList<>();
        for (int i = 0; i < adresar.size(); i++) {

            pom.add(adresar.get(i));
            pom.add(adresar.get(i));

        }
        adresar = pom;

        //  return index;
    }

    public int getHlbka() {
        return hlbka;
    }

    public void insert(Record record) {

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
                block.add(record);
                insert(block, index * block.getSize());
                flag = true;
            } else {

                adresa = adresar.get(index);

                if (block.isLoad() == false) {
                    block.fromArray(read(adresa, block.getSize()));
                } else {
                    block.setLoad(false);
                }

                if (block.isFull()) {

                    if (block.getHlbka() == getHlbka()) {

                        rozsirAdresy(index);
                        index = 2 * index;

                        //ulozim zmenenu hlbku
                        //insert(block, adresa);
                    }
                    rozdelBlock(record, index);

                } else {
                    block.add(record);
                    insert(block, adresa);
                    flag = true;

                }

            }
        }

    }

    public void rozdelBlock(Record record, int index) {
        block.setHlbka(block.getHlbka() + 1);

        Block b = block.copyBlock();

        Record[] rec = block.getRecord();
        for (int i = 0; i < rec.length; i++) {
            if (getIndex(rec[i], block.getHlbka()) == index) {
                b.remove(i);

            } else {
                block.remove(i);

            }
        }

        int novaAdres = Collections.max(adresar) + block.getSize();
        for (int i = 0; i < adresar.size(); i++) {
            if (adresar.get(i) == adresar.get(index)) {
                int pom = i >>> (hlbka - b.getHlbka());
                if (pom != index) {
                    adresar.set(i, novaAdres);
                }
            }
        }

        if (getIndex(record) == index) {
            insert(b, novaAdres);
            block.setLoad(true);

        } else {
            insert(block, adresar.get(index));
            block = b;
        }

    }

    public int getIndex(Record record) {
        return getIndex(record, hlbka);

    }

    public int getIndex(Record record, int paHlbka) {
        BitSet bs = record.getData().getHash();
        StringBuilder sb = new StringBuilder();
        int size = bs.size() - 1;
        for (int i = 0; i < paHlbka; i++) {
            sb.append(bs.get(size - i) == true ? "1" : "0");
        }

        return Integer.parseInt(sb.toString(), 2);

    }

    public void getAktStav() {

        for (Integer cis : adresar) {
            block.fromArray(read(cis, block.getSize()));
            System.out.println("Block s adresou: " + cis);
            System.out.println(block.getAktStav());
        }

    }

    /**
     * Najde record
     *
     * @param record
     * @return
     */
    public IData find(Record record) {
        if (adresar.isEmpty()) {
            return null;
        }

        int index = getIndex(record);

        int adresa = adresar.get(index);
        block.fromArray(read(adresa, block.getSize()));

        return block.find(record);
    }

    /**
     * Vrati stromovy vypis blokov
     *
     * @return
     */
    public DefaultTreeModel getTreModel() {

        DefaultMutableTreeNode root = new DefaultMutableTreeNode(file);
        DefaultTreeModel model = new DefaultTreeModel(root);

        int sum = 0;
        int pom = Integer.MIN_VALUE;
        for (Integer cis : adresar) {
            if (pom != cis) {
                
                block.fromArray(read(cis, block.getSize()));
                sum += block.getCountRec();
                root.add(block.getRecOfBlock(cis));
                pom = cis;
            }

        }
        System.out.println(sum);

        return model;

    }

}
