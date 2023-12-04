/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.lojapedacospizza.objetos;

import com.mycompany.lojapedacospizza.core.Mesa;
import com.mycompany.lojapedacospizza.controle.LojaController;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Luiz
 */
public class PizzaCalabresa extends Pizza {

    public PizzaCalabresa() {
        tipo = "Pizza de Calabresa";
        pedacosRestantes = Mesa.TAMPIZZA;
    }
    
}
