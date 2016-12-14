/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package extenhash;

import java.util.BitSet;

/**
 *
 * @author Jožko
 */
public interface IData {

    /**
     * Vrati hash
     * @return 
     */
    BitSet getHash();

    /**
     * Vrati pole bytov recordu
     * @return 
     */
    byte[] getByteArray();

    /**
     * Nacita record z pola bytov
     * @param array 
     */
    void fromByteArray(byte[] array);

    int getSize();

    
    boolean equals(Record record);

    /**
     * Vypis pre stromove zobrazenie
     * @return 
     */
    String getTreeString();

    IData newRecord();
}
