/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.lojapedacospizza.objetos;

/**
 *
 * @author Luiz
 */
public abstract class Pizza {
    protected int pedacosRestantes;
    protected String tipo;
    protected final Object lock = new Object();
    
    public Pizza(int pedacosRestantes) {
        this.pedacosRestantes = pedacosRestantes;
    }
    
    public String getTipo() {
        return tipo;
    }
    public int getPedacosRestantes() {
        return pedacosRestantes;
    }
    public void adicionarPedacos(int pedacos) {
        pedacosRestantes += pedacos;
    }
    
    public void removerPedacos(int pedacos) {
        pedacosRestantes -= pedacos;
    }
}