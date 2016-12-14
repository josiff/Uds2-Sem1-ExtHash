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

/**
 *
 * @author Jožko
 */
public class Record {

    private IData data;
    private boolean platny; //1

    private final static int STORE = 1;

    public Record(IData data) {
        this.data = data;
        this.platny = false;
    }

    public IData getData() {
        return data;
    }

    public void setData(IData data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return data.toString();
    }

    public boolean isPlatny() {
        return platny;
    }

    public void setPlatny(boolean platny) {
        this.platny = platny;
    }

    public int getSize() {
        return getData().getSize() + STORE;

    }

    public byte[] getBArray() {
        byte b[] = new byte[getSize()];

        int position = 0;
        System.arraycopy(getByteArray(), 0, b, position, STORE);
        position += STORE;
        System.arraycopy(getData().getByteArray(), 0, b, position, getData().getSize());

        return b;

    }

    public void fromBArray(byte[] paArray) {

        byte b[] = new byte[getSize()];
        int position = 0;
        System.arraycopy(paArray, position, b, 0, STORE);
        fromByteArray(b);
        position += STORE;
        System.arraycopy(paArray, position, b, 0, getData().getSize());
        getData().fromByteArray(b);

    }

    private byte[] getByteArray() {
        ByteArrayOutputStream hlpByteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream hlpOutStream = new DataOutputStream(hlpByteArrayOutputStream);

        try {
            hlpOutStream.writeBoolean(this.platny);

            return hlpByteArrayOutputStream.toByteArray();

        } catch (IOException e) {
            throw new IllegalStateException("Error during conversion to byte array.");
        }
    }

    private void fromByteArray(byte[] paArray) {
        ByteArrayInputStream hlpByteArrayInputStream = new ByteArrayInputStream(paArray);
        DataInputStream hlpInStream = new DataInputStream(hlpByteArrayInputStream);

        try {

            this.platny = hlpInStream.readBoolean();

        } catch (IOException e) {
            throw new IllegalStateException("Error during conversion from byte array.");
        }
    }

}
