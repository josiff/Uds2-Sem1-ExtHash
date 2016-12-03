/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package system;

import extenhash.Block;
import extenhash.ExtentHash;
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

    private final int POC_RECORDOV = 5;

    public Core() {

        msg = new Message();
        imesageListener = new ArrayList<>();
        osoby = new ExtentHash("Osoby.txt", new Record(new Osoba()), POC_RECORDOV);
        vozidlaEvc = new ExtentHash("VozidlaEvc.txt", new Record(new Vozidlo()), POC_RECORDOV);
        vozidlaVin = new ExtentHash("VozidlaVin.txt", new Record(new VozidloVIN()), POC_RECORDOV);
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

    /**
     * Najde osobu
     * @param evc
     * @return 
     */
    public String findOsobu(int evc) {

        Osoba os = (Osoba) osoby.find(new Record(new Osoba(evc)));
        if (os == null) {
            setInfoMsg(String.format("Osoba s evč: %s sa nenašla", evc));
            return "";
        }

        return os.toString();

    }

    /**
     * Gnerovanie dat
     * @param pocOsob
     * @param pocVoz 
     */
    public void generujData(int pocOsob, int pocVoz) {
        generator.generujData(this, pocOsob, pocVoz);
    }

    /**
     * Vrati vypis suboru v stromovom zobrazeni
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

}
