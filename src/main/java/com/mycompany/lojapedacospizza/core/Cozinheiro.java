/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.lojapedacospizza.core;

import com.mycompany.lojapedacospizza.core.Mesa;
import com.mycompany.lojapedacospizza.objetos.Pizza;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Luiz
 */
public class Cozinheiro extends Thread {
    private static Cozinheiro cozinheiro;
    private final Object lock;
    private boolean parar = false;
    private int quantidadeCozinhar;
    private Pizza pizza;
    
    public synchronized static Cozinheiro getInstancia() {
        if(cozinheiro == null) {
            cozinheiro = new Cozinheiro();
        }
        return cozinheiro;
    }
    
    private Cozinheiro() {
        lock = new Object();
    }
    
    public void cozinhar(Pizza pizza, int quantidade) {
        try {
            Mesa.cozinhando.acquire();
        } catch (InterruptedException ex) {
            Logger.getLogger(Cozinheiro.class.getName()).log(Level.SEVERE, null, ex);
        }
        synchronized(lock) {
            quantidadeCozinhar = quantidade;
            this.pizza = pizza;
            lock.notify();
        }
    }
    
    public void parar() {
        parar = true;
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
                Thread.sleep(2000 * quantidadeCozinhar);
            } catch (InterruptedException ex) {
                Logger.getLogger(Cozinheiro.class.getName()).log(Level.SEVERE, null, ex);
            }
            pizza.adicionarPedacos(quantidadeCozinhar);
            quantidadeCozinhar = 0;
            Mesa.cozinhando.release();
        }
    }
    
    public void esperar() {
        synchronized(lock) {
            try {
                lock.wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(Cozinheiro.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
