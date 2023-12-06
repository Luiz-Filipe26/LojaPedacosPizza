/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.lojapedacospizza.core;

import com.mycompany.lojapedacospizza.controle.LojaController;
import com.mycompany.lojapedacospizza.objetos.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Luiz
 */
public class ClienteLogic extends Thread {
    private final LojaController lojaController = LojaController.getInstancia();
    private final Cliente cliente;
    private boolean parar = false;
    
    
    public final Object lock = new Object();
    
    private final int limiteX = 240;
    private static final int UNIDADELARGURA = 30;
    
    private int quantidade;
    private String tipoPizza;

    
    public ClienteLogic(String nome, int x, int y) {
        cliente = new Cliente(nome, x, y);
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
            if(cliente.x + UNIDADELARGURA <= limiteX) {
                cliente.x += UNIDADELARGURA;
            }
        }
        else {
            if(cliente.x - UNIDADELARGURA >= UNIDADELARGURA) {
                cliente.x -= UNIDADELARGURA;
            }
        }
        
        lojaController.desenharCliente(cliente);
        
        if(cliente.x == limiteX) {
            lojaController.balaoPedir();
        }
    }
    
    public void clienteSolicitarPizza(int quantidade, String tipoPizza) {
        this.quantidade = quantidade;
        this.tipoPizza = tipoPizza;
        synchronized(lock) {
            lock.notify();
        }
    }
    
    public void receberPizza(String tipoPizza, int pedacos) {
        cliente.adicionarPizza(tipoPizza, pedacos);
    }
    
    @Override
    public void run() {
        
        while(true) {
            esperar();
            if(parar) {
                break;
            }
            try {
                Mesa.mesaEsperandoCozinhar.acquire();
            } catch (InterruptedException ex) {
                Logger.getLogger(ClienteLogic.class.getName()).log(Level.SEVERE, null, ex);
            }

            Mesa.mesaEsperandoCozinhar.release();
            
            lojaController.solicitarMesa(cliente.nome, quantidade, tipoPizza);
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
