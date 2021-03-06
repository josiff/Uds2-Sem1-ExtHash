/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package extenhash;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import system.Message;

/**
 *
 * @author Jožko
 */
public class ExtentHash {

    private List<Integer> adresar;
    private int hlbka;
    private RandomAccessFile raf;
    private Block block;

    private String file;

    private final static String FILE_PREFIX = "adr-";

    private static final int MAX_HLBKA = 20;

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

    /**
     * Vlozenie bloku na adresu
     *
     * @param block
     * @param adresa
     * @return
     */
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

    /**
     * Citanie zo suboru
     *
     * @param offset
     * @param length
     * @return
     */
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

    /**
     * Rozsirenie adresara
     *
     * @param index
     */
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

    /**
     * Insert record to file
     *
     * @param record
     * @return
     */
    public boolean insert(Record record, Message msg) {

        boolean flag = false;
        int adresa = 0;
        int index = 0;

        while (flag == false) {

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
                insert(block, adresar.get(index));
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
                        if (hlbka >= MAX_HLBKA) {
                            //System.out.println("Plny adresar" + hlbka + " " + record.getData().getTreeString());
                            msg.setMsg(String.format("Záznam %s nebolo možné vložiť "
                                    + "pretože adresar ma maximálnu veľkosť "
                                    + "a nie je implementované preplnovanie blokov", record.getData().getTreeString()));
                            block.clearRec();
                            /*  System.out.println(adresar.size());*/
                            return false;

                        }
                        rozsirAdresy(index);

                    }
                    rozdelBlock(record, adresa);

                } else {
                    block.add(record);
                    insert(block, adresa);
                    flag = true;

                }

            }
        }

        return flag;

    }

    /**
     * Rozdelenie blokov a presunutie recordov
     *
     * @param record
     * @param paAdresa
     */
    public void rozdelBlock(Record record, int paAdresa) {
        int min = getIndexMin(record, block.getHlbka(), hlbka);
        int max = getIndexMax(record, block.getHlbka(), hlbka);

        int index = getIndexMin(record, block.getHlbka(), block.getHlbka() + 1);
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
        int end = (int) Math.ceil((max - min) / 2.0);
        min += end;
        for (int i = min; i <= max; i++) {
            adresar.set(i, novaAdres);
        }

        insert(block, paAdresa);

        insert(b, novaAdres);

    }

    /**
     * Vrati index
     *
     * @param record
     * @return
     */
    public int getIndex(Record record) {
        return getIndex(record, hlbka);

    }

    /**
     * Vrati index
     *
     * @param record
     * @param paHlbka
     * @return
     */
    public int getIndex(Record record, int paHlbka) {
        BitSet bs = record.getData().getHash();
        int num = 0;
        int index = 0;
        for (int i = paHlbka - 1; i >= 0; i--) {
            if (bs.get(i)) {
                num += Math.pow(2, index);
            }
            index++;
        }
        return num;

    }

    /**
     * Najdenie minimalneho indexu v bloku
     *
     * @param record
     * @param paHlbka
     * @param novaHlbka
     * @return
     */
    public int getIndexMin(Record record, int paHlbka, int novaHlbka) {
        BitSet bs = record.getData().getHash();
        int num = 0;
        int index = novaHlbka - 1;
        for (int i = 0; i < novaHlbka; i++) {
            if (i < paHlbka) {
                if (bs.get(i)) {
                    num += Math.pow(2, index);
                }
            }
            index--;
        }
        return num;

    }

    /**
     * Najdenie maximalneho indexu v bloku
     *
     * @param record
     * @param paHlbka
     * @param novaHlbka
     * @return
     */
    public int getIndexMax(Record record, int paHlbka, int novaHlbka) {
        BitSet bs = record.getData().getHash();
        int num = 0;
        int index = novaHlbka - 1;
        for (int i = 0; i < novaHlbka; i++) {
            if (i < paHlbka) {
                if (bs.get(i)) {
                    num += Math.pow(2, index);
                }
            } else {
                num += Math.pow(2, index);
            }
            index--;
        }
        return num;

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

        IData data = block.find(record);
        if (data != null) {
            return data;
        }
        int index = getIndex(record);

        int adresa = adresar.get(index);
        block.fromArray(read(adresa, block.getSize()));

        return block.find(record);
    }

    /**
     * Zmeno recordu v adresari
     *
     * @param record
     * @return
     */
    public boolean change(Record record) {

        if (adresar.isEmpty()) {
            return false;
        }

        boolean result = false;
        if (!block.isEmpty()) {
            result = block.change(record);
        }

        int index = getIndex(record);
        int adresa = adresar.get(index);
        if (result == false) {

            block.fromArray(read(adresa, block.getSize()));

        }

        if (block.change(record)) {
            insert(block, adresa);
            return true;
        } else {
            return false;
        }

    }

    /**
     * Zmeno recordu v adresari
     *
     * @param record
     * @return
     */
    public boolean remove(Record record) {

        if (adresar.isEmpty()) {
            return false;
        }

        boolean result = false;
        if (!block.isEmpty()) {
            result = block.remove(record);
        }

        int index = getIndex(record);
        int adresa = adresar.get(index);
        if (result == false) {

            block.fromArray(read(adresa, block.getSize()));

        }

        if (block.remove(record)) {
            insert(block, adresa);
            return true;
        } else {
            return false;
        }

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
        /* for (Integer cis : adresar) {
         if (pom != cis) {

         block.fromArray(read(cis, block.getSize()));
         sum += block.getCountRec();
         root.add(block.getRecOfBlock(cis));
         pom = cis;
         }

         }*/

        pom = 0;
        int maxAdr = Collections.max(adresar);
        while (pom <= maxAdr) {

            block.fromArray(read(pom, block.getSize()));
            sum += block.getCountRec();
            root.add(block.getRecOfBlock(pom));
            pom += block.getSize();

        }

        System.out.println(sum);

        return model;

    }

    /**
     * Ulozenie adresara a hlbky
     */
    public void save() {

        try {
            PrintWriter pr = new PrintWriter(new BufferedWriter(new FileWriter(FILE_PREFIX + file)));
            pr.println(hlbka);
            for (Integer adr : adresar) {
                pr.println(adr);
            }
            pr.close();

        } catch (IOException ex) {
            Logger.getLogger(ExtentHash.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Nacitanie adresara a hlbky
     */
    public void load() {

        BufferedReader bufReader;

        String line = null;
        File f = new File(FILE_PREFIX + file);
        if (f.exists()) {
            try {
                bufReader = new BufferedReader(new FileReader(f));
                if ((line = bufReader.readLine()) != null) {
                    hlbka = Integer.parseInt(line);
                }

                while ((line = bufReader.readLine()) != null) {

                    adresar.add(Integer.parseInt(line));

                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(ExtentHash.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ExtentHash.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public void clearFile() {

        PrintWriter pr = null;
        try {
            pr = new PrintWriter(new File(file));
            pr.print("");
            pr.close();
            pr = new PrintWriter(new File(FILE_PREFIX + file));
            pr.print("");
            pr.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ExtentHash.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            pr.close();
        }

    }

}
