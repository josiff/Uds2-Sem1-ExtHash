/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package system;

import extenhash.Block;
import extenhash.ExtentHash;
import extenhash.IData;
import extenhash.Record;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.swing.tree.DefaultTreeModel;
import model.Osoba;
import model.Vozidlo;
import model.VozidloVIN;

/**
 *
 * @author Jožko
 */
public class Core {

    private ExtentHash osoby;
    private ExtentHash vozidlaEvc;
    private ExtentHash vozidlaVin;
    private Block block;

    private List<IMessage> imesageListener;
    private Message msg;
    private SimpleDateFormat sf;
    private Generator generator;

    private final int POC_RECORDOV_OS = 5;
    private final int POC_RECORDOV_EVC = 4;
    private final int POC_RECORDOV_VIN = 3;

    public Core() {

        msg = new Message();
        imesageListener = new ArrayList<>();
        osoby = new ExtentHash("Osoby.txt", new Record(new Osoba()), POC_RECORDOV_OS);
        vozidlaEvc = new ExtentHash("VozidlaEvc.txt", new Record(new Vozidlo()), POC_RECORDOV_EVC);
        vozidlaVin = new ExtentHash("VozidlaVin.txt", new Record(new VozidloVIN()), POC_RECORDOV_VIN);
        this.sf = new SimpleDateFormat("dd.MM.yyyy");
        generator = new Generator();

    }

    public void addMsgListener(IMessage imsg) {
        imesageListener.add(imsg);
    }

    /**
     * Vyvolanie eventu
     *
     * @param msg
     */
    public void showMsg(Message msg) {

        for (IMessage item : imesageListener) {
            item.onMessage(msg);
        }

    }

    /**
     * Prida osobu
     *
     * @param meno
     * @param przv
     * @param evc
     * @param endPlatnost
     * @param zakaz
     * @param priestupky
     */
    public void addOsobu(String meno, String przv, int evc, Calendar endPlatnost, boolean zakaz, int priestupky) {
        Osoba os = new Osoba(meno, przv, evc, endPlatnost, zakaz, priestupky);
        osoby.insert(new Record(os));
        // setInfoMsg("Záznam bol uložený");

    }

    public void changeOsobu(String meno, String przv, int evc, Calendar endPlatnost, boolean zakaz, int priestupky) {
        Osoba os = new Osoba(meno, przv, evc, endPlatnost, zakaz, priestupky);
        osoby.change(new Record(os));
        // setInfoMsg("Záznam bol uložený");

    }

    /**
     * Najde osobu
     *
     * @param evc
     * @return
     */
    public String findOsobu(int evc) {

        Osoba os = findOsobuO(evc);
        if (os == null) {
            //setInfoMsg(String.format("Osoba s evč: %s sa nenašla", evc));
            return "";
        }

        return os.toString();

    }

    public Osoba findOsobuO(int evc) {

        Osoba os = (Osoba) osoby.find(new Record(new Osoba(evc)));
        if (os == null) {
            setInfoMsg(String.format("Osoba s evč: %s sa nenašla", evc));

        }

        return os;

    }

    /**
     * Vlozi vozidlo do suboru
     *
     * @param evc
     * @param vin
     * @param napravy
     * @param hmotnost
     * @param hladane
     * @param endStk
     * @param endEk
     */
    public void addVozidlo(String evc, String vin, int napravy,
            int hmotnost, boolean hladane, Calendar endStk, Calendar endEk) {

        Record rec = new Record(new Vozidlo(evc, vin, napravy, hmotnost, hladane, endStk, endEk));
        if (vozidlaEvc.insert(rec)) {
            if (vozidlaVin.insert(new Record(new VozidloVIN(evc, vin))) == false) {
                rec.setPlatny(false);
                vozidlaEvc.remove(rec);
                System.out.println(rec.getData().getTreeString());
            }
        }

    }

    public void changeVoz(String evc, String vin, int napravy,
            int hmotnost, boolean hladane, Calendar endStk, Calendar endEk) {
        vozidlaEvc.change(new Record(new Vozidlo(evc, vin, napravy, hmotnost, hladane, endStk, endEk)));
    }

    /**
     * Najde vozidlo podla EVC
     *
     * @param evc
     * @return
     */
    public String findVozidloEvc(String evc) {

        Vozidlo voz = findVizidloE(evc);
        if (voz == null) {
            //setInfoMsg(String.format("Vozidlo s evč: %s sa nenašlo", evc));
            return "";
        }

        return voz.toString();
    }

    public Vozidlo findVizidloE(String evc) {

        Vozidlo voz = (Vozidlo) vozidlaEvc.find(new Record(new Vozidlo(evc)));
        if (voz == null) {
            setInfoMsg(String.format("Vozidlo s evč: %s sa nenašlo", evc));

        }

        return voz;

    }

    public String findVozidloVIN(String vin) {

        VozidloVIN vozVin = (VozidloVIN) vozidlaVin.find(new Record(new VozidloVIN(vin)));
        if (vozVin == null) {
            setInfoMsg(String.format("Vozidlo s vin: %s sa nenašlo", vin));
            return "";
        }

        return findVozidloEvc(vozVin.getEvc());

    }

    /**
     * Gnerovanie dat
     *
     * @param pocOsob
     * @param pocVoz
     */
    public void generujData(int pocOsob, int pocVoz) {
        long startTime = System.nanoTime();
        generator.generujData(this, pocOsob, pocVoz);
        long endTime = System.nanoTime();
        double duration = (endTime - startTime) / 60000000000.0;
        setInfoMsg("Dáta boli vygenerované " + duration);
    }

    public void testujData(int pocOsob) {
        long startTime = System.nanoTime();
        generator.testujData(this, pocOsob);
        long endTime = System.nanoTime();
        double duration = (endTime - startTime) / 60000000000.0;
        setInfoMsg("Koniec testovania. Doba testovania>" + duration);

    }

    /**
     * Vrati vypis suboru v stromovom zobrazeni
     *
     * @param file
     * @return
     */
    public DefaultTreeModel getFileVypis(String file) {

        switch (file) {

            case "osoby":
                return osoby.getTreModel();
            case "vozidlaEvc":
                return vozidlaEvc.getTreModel();
            case "vozidlaVin":
                return vozidlaVin.getTreModel();
            default:
                return null;

        }

    }

    /**
     * Vyvolanie eventu msg
     *
     * @param text
     */
    public void setErrMsg(String text) {
        msg.viewAsError();
        msg.setMsg(text);
        showMsg(msg);
    }

    /**
     * Vyvolanie eventu msg
     *
     * @param text
     */
    public void setInfoMsg(String text) {
        msg.viewAsInfo();
        msg.setMsg(text);
        showMsg(msg);
    }

    public ExtentHash getOsoby() {
        return osoby;
    }

    public Generator getGenerator() {
        return generator;
    }

    public void save() {

        osoby.save();
        vozidlaEvc.save();
        vozidlaVin.save();
        setInfoMsg("Dáta boli uložené");

    }

    public void load() {

        osoby.load();
        vozidlaEvc.load();
        vozidlaVin.load();
        setInfoMsg("Dáta boli načítané");

    }

}
