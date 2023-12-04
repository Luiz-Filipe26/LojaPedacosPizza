/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.lojapedacospizza.core;

import com.mycompany.lojapedacospizza.objetos.PizzaMargherita;
import com.mycompany.lojapedacospizza.objetos.PizzaPortuguesa;
import com.mycompany.lojapedacospizza.objetos.PizzaFrangoCatupiry;
import com.mycompany.lojapedacospizza.objetos.Pizza;
import com.mycompany.lojapedacospizza.objetos.PizzaCalabresa;
import com.mycompany.lojapedacospizza.controle.LojaController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Luiz
 */
public class Mesa extends Thread {
    private Map<String, Pizza> pizzas = new HashMap<>();
    private Cozinheiro cozinheiro;
    public static Semaphore cozinhando = new Semaphore(1);
    
    
    public static final int TAMPIZZA = 8;
    private boolean parar = false;
    public Object lock = new Object();
    
    private int quantidadeSolicitada;
    private String tipoPizza;
    
    public Mesa(Cozinheiro cozinheiro) {
        pizzas.put("Pizza de Calabresa", new PizzaCalabresa());
        pizzas.put("Pizza de Frango com Catupiry", new PizzaFrangoCatupiry());
        pizzas.put("Pizza Margherita", new PizzaMargherita());
        pizzas.put("Pizza Portuguesa", new PizzaPortuguesa());
        this.cozinheiro = cozinheiro;
        this.cozinheiro.start();
    }
    
    public void solicitarMesa(int quantidadeSolicitada, String tipoPizza) {
        
        synchronized(lock) {
            this.quantidadeSolicitada = quantidadeSolicitada;
            this.tipoPizza = tipoPizza;
            lock.notify();
        }
    }

    public void viewFechada() {
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
                cozinheiro.parar();
                break;
            }
            
            Pizza pizzaSolicitada = pizzas.get(tipoPizza);
            int quantidadeAtual = pizzaSolicitada.getPedaçosRestantes();

            if(quantidadeAtual < quantidadeSolicitada) {
                int quantidadeCozinhar = (quantidadeSolicitada - quantidadeAtual) + 8;

                LojaController.getInstancia().notificarCozinhando();
                cozinheiro.cozinhar(pizzaSolicitada, quantidadeCozinhar);
                try {
                    cozinhando.acquire();
                } catch (InterruptedException ex) {
                    Logger.getLogger(Mesa.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                entregar(pizzaSolicitada, quantidadeSolicitada);

            }
            else if(quantidadeAtual == quantidadeSolicitada) {
                entregar(pizzaSolicitada, quantidadeSolicitada);
                cozinheiro.cozinhar(pizzaSolicitada, TAMPIZZA);
            }
            else {
                entregar(pizzaSolicitada, quantidadeSolicitada);
            }
        }
    }
        
    public void entregar(Pizza pizza, int pedacos) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        
        pizza.removerPedacos(pedacos);
        String tipo = pizza.getTipo();
        int pedacosRestantes = pizza.getPedaçosRestantes();
        
        LojaController.getInstancia().entregarPizza(tipo, pedacos, pedacosRestantes);
        cozinhando.release();
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
