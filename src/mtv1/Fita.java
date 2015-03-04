/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mtv1;

import java.util.ListIterator;

/**
 *
 * @author Neto
 */
public class Fita {
    private DoublyLinkedList<TipoIntString> fita;
    private static final int xCentral = 210;
    private ListIterator<TipoIntString> iterator;
    TipoIntString valorAtual;
    
    
    public Fita() {
        fita = new DoublyLinkedList<>();
        iterator = fita.iterator();
    }
    
    public Fita(String entrada) {
        int x = xCentral;
        
        fita = new DoublyLinkedList<>();
        iterator = fita.iterator();
        
        if (entrada == null || entrada.equals(""))
            return;
        
        for (int i = 0; i < entrada.length(); i++) {
            if (x == xCentral)
                valorAtual = new TipoIntString(x, ( entrada.charAt(i) ));
            fita.add(new TipoIntString(x, ( entrada.charAt(i) )));
            x += 27;
        }
        
        //iterator.next();    // Aponta para o primeiro elemento
    }
    
    public DoublyLinkedList<TipoIntString> getFita() {
        return fita;
    }
    
    public int setEntrada(String entrada) {
        int x = xCentral;
        fita = new DoublyLinkedList<>();
        
        if (entrada == null || entrada.equals(""))
            return 1;
        
        for (int i = 0; i < entrada.length(); i++) {
            if (x == xCentral)
                valorAtual = new TipoIntString(x, ( entrada.charAt(i) ));
            fita.add(new TipoIntString(x, ( entrada.charAt(i) )));
            x += 27;
        }
        
        //iterator.next();    // Aponta para o primeiro elemento
        
        return 0;
    }
    
    public void paraDireita() {
        //if (iterator.hasNext())
        //    iterator.next();
        
        ListIterator<TipoIntString> iteratorTemporary = fita.iterator();
        while (iteratorTemporary.hasNext()) {
            TipoIntString campo = iteratorTemporary.next();
            int x = campo.getX();
            campo.setX(x-27);
            iteratorTemporary.set(campo);
            
            if (xCentral == campo.getX())
                valorAtual = campo;
        }
    }
    
    public void paraEsquerda() {
        //if (iterator.hasPrevious())
        //    iterator.previous();
        
        ListIterator<TipoIntString> iteratorTemporary = fita.iterator();
        while (iteratorTemporary.hasNext()) {
            TipoIntString campo = iteratorTemporary.next();
            int x = campo.getX();
            campo.setX(x+27);
            iteratorTemporary.set(campo);
            
            if (xCentral == campo.getX())
                valorAtual = campo;
        }
    }
    
    public TipoIntString getCampo() {
        ListIterator<TipoIntString> iteratorTemporary = fita.iterator();
        while (iteratorTemporary.hasNext()) {
            TipoIntString campo = iteratorTemporary.next();
            
            if (xCentral == campo.getX())
                return campo;
        }
        return valorAtual;
    }
    
    public void setCampo(char dado) {
        valorAtual.setDado(dado);
        ListIterator<TipoIntString> iteratorTemporary = fita.iterator();
        while (iteratorTemporary.hasNext()) {
            TipoIntString campo = iteratorTemporary.next();
            
            if (xCentral == campo.getX())
                campo.setDado(dado);// .set(valorAtual);
        }
    }
}
