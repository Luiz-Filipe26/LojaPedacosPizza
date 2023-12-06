/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.lojapedacospizza.objetos;

import com.mycompany.lojapedacospizza.controle.LojaController;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Luiz
 */
public abstract class Pizza {
    protected int pedacosRestantes;
    protected String tipo;
    protected final Object lock = new Object();
    
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