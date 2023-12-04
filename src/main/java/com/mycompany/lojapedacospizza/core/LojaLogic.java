/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.lojapedacospizza.core;

import com.mycompany.lojapedacospizza.controle.LojaController;
import com.mycompany.lojapedacospizza.objetos.Cliente;

/**
 *
 * @author Luiz
 */
public class LojaLogic extends Thread {
    private String direcao;
    private Cliente cliente;
    private LojaController lojaController;
    
    private final int limiteX = 240;
    private static final int unidadeLargura = 30;
    
    public LojaLogic() {
        cliente = new Cliente("Cliente 1", 30, 30);
        lojaController = LojaController.getInstancia();
    }
    
    public Cliente getCliente() {
        return cliente;
    }
    
    public void keyPressed(String keyCode) {
        
        if(keyCode.equals("RIGHT")) {
            if(cliente.x + unidadeLargura <= limiteX) {
                cliente.x += unidadeLargura;
            }
        }
        else {
            if(cliente.x - unidadeLargura >= unidadeLargura) {
                cliente.x -= unidadeLargura;
            }
        }
        
        lojaController.desenharCliente(cliente);
        
        if(cliente.x == limiteX) {
            lojaController.balaoPedir(cliente);
        }
    }
    
    @Override
    public void run() {
        
    }
}
