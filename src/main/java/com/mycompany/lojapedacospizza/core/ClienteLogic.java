/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.lojapedacospizza.core;

import com.mycompany.lojapedacospizza.controle.LojaController;
import com.mycompany.lojapedacospizza.objetos.Cliente;
import com.mycompany.lojapedacospizza.objetos.Pizza;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Luiz
 */
public class ClienteLogic extends Thread {
    private String direcao;
    private Cliente cliente;
    private boolean parar = false;
    private LojaController lojaController;
    
    public final Object lock = new Object();
    
    private final int limiteX = 240;
    private static final int unidadeLargura = 30;
    
    private int quantidade;
    private String tipoPizza;

    
    public ClienteLogic(String nome, int x, int y) {
        cliente = new Cliente(nome, x, y);
        lojaController = LojaController.getInstancia();
    }
    
    public Cliente getCliente() {
        return cliente;
    }
    
    public void parar() {
        parar = true;
        synchronized(lock) {
            lock.notify();
        }
    }
    
    public boolean checarMostrarBalao() {
        return (cliente.x == limiteX);
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
    
    public void clienteSolicitarPizza(int quantidade, String tipoPizza) {
        this.quantidade = quantidade;
        this.tipoPizza = tipoPizza;
        synchronized(lock) {
            lock.notify();
        }
    }
    
    @Override
    public void run() {
        
        while(true) {
            esperar();
            if(parar) {
                break;
            }
            try {
                Mesa.cozinhando.acquire();
            } catch (InterruptedException ex) {
                Logger.getLogger(ClienteLogic.class.getName()).log(Level.SEVERE, null, ex);
            }

            Mesa.cozinhando.release();

            lojaController.solicitarMesa(quantidade, tipoPizza);
        }
    }
    
    public void esperar() {
        synchronized(lock) {
            try {
                lock.wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(Mesa.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
