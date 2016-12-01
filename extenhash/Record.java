/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package extenhash;

/**
 *
 * @author Jožko
 */
public class Record {

    private IData data;
    private boolean platny;

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
    
    
    
    

}
