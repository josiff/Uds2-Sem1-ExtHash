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
public class Core {

    private ExtentHash osoby;
    private Block block;

    public Core() {
        osoby = new ExtentHash("Osoby.txt");
        block = new Block(5);
    }

    

}
