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

    BitSet getHash(int pocet);

    byte[] getByteArray();

    void fromByteArray(byte[] array);

    int getSize();

    boolean equals(Record record);

    String getHas();

    IData newRecord();
}
