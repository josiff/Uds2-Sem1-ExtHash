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
public abstract class Record {

    abstract BitSet getHash(int pocet);

    abstract byte[] getByteArray();

    abstract void fromByteArray(byte [] array);
    
    abstract int getSize();
    
    abstract boolean equals(Record record);
    
    abstract String getHas();
    
    
    
    
}
