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
    private final LojaController lojaController = LojaController.getInstancia();
    private Map<String, Pizza> pizzas = new HashMap<>();
    public static Semaphore mesaEsperandoCozinhar = new Semaphore(1);
    public final Object lock;
    
    
    public static final int TAMPIZZA = 8;
    private boolean parar = false;
    
    private int quantidadeSolicitada;
    private String tipoPizza;
    private String nome;
    
    public Mesa(Cozinheiro cozinheiro) {
        this.lock = new Object();
        pizzas.put("Pizza de Calabresa", new PizzaCalabresa(TAMPIZZA));
        pizzas.put("Pizza de Frango com Catupiry", new PizzaFrangoCatupiry(TAMPIZZA));
        pizzas.put("Pizza Margherita", new PizzaMargherita(TAMPIZZA));
        pizzas.put("Pizza Portuguesa", new PizzaPortuguesa(TAMPIZZA));
    }
    
    public void solicitarMesa(String nome, int quantidadeSolicitada, String tipoPizza) {
        
        synchronized(lock) {
            this.quantidadeSolicitada = quantidadeSolicitada;
            this.tipoPizza = tipoPizza;
            this.nome = nome;
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
                lojaController.pararCozinheiro();
                break;
            }
            
            Pizza pizzaSolicitada = pizzas.get(tipoPizza);
            int quantidadeAtual = pizzaSolicitada.getPedacosRestantes();

            if(quantidadeAtual < quantidadeSolicitada) {
                int quantidadeCozinhar = (quantidadeSolicitada - quantidadeAtual) + 8;

                lojaController.notificarCozinhando();
                lojaController.cozinhar(pizzaSolicitada, quantidadeCozinhar);
                try {
                    mesaEsperandoCozinhar.acquire();
                } catch (InterruptedException ex) {
                    Logger.getLogger(Mesa.class.getName()).log(Level.SEVERE, null, ex);
                }
                entregar(pizzaSolicitada, quantidadeSolicitada);
                mesaEsperandoCozinhar.release();
            }
            else if(quantidadeAtual == quantidadeSolicitada) {
                entregar(pizzaSolicitada, quantidadeSolicitada);
                mesaEsperandoCozinhar.release();
                lojaController.cozinhar(pizzaSolicitada, TAMPIZZA);
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
        int pedacosRestantes = pizza.getPedacosRestantes();
        
        lojaController.entregarPizza(nome, tipo, pedacos, pedacosRestantes);
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
