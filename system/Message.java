/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package system;

import javax.swing.JOptionPane;

/**
 *
 * @author Jožko
 */
public class Message {

    public static final int ERROR = JOptionPane.ERROR_MESSAGE;
    public static final int INFO = JOptionPane.INFORMATION_MESSAGE;

    private int type;
    private String msg;

    public Message() {
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * Nastavi flag na error
     */
    public void viewAsError() {
        setType(ERROR);
    }

    /**
     * Nastavi flag na info
     */
    public void viewAsInfo() {
        setType(INFO);
    }

}
