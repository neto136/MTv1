/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mtv1;

/**
 *
 * @author Neto
 */
public class TipoIntString {
    private int x;
    private char dado;
    
    public TipoIntString(int x, char dado) {
        this.x = x;
        this.dado = dado;
    }
    
    public void setX(int x) {
        this.x = x;
    }
    
    public int getX() {
        return x;
    }
    
    public void setDado(char dado) {
        this.dado = dado;
    }
    
    public char getDado() {
        return dado;
    }
}
