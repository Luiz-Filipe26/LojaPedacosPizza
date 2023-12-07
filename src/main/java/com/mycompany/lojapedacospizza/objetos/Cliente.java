/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.lojapedacospizza.objetos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Luiz
 */
public class Cliente {
    public String nome;
    private Map<String, Pizza> pizzas = new HashMap<>();
    public int x;
    public int y;

    public Cliente(String nome, int x, int y) {
        this.nome = nome;
        this.x = x;
        this.y = y;
    }
    
    public Ponto getPonto() {
        return new Ponto(x, y);
    }
    
    public void comer() {
        List<String> tiposDePizza = new ArrayList<>(pizzas.keySet());
        String ultimaPizza = tiposDePizza.get(tiposDePizza.size() - 1);
        
        if(pizzas.get(ultimaPizza).getPedacosRestantes() == 1) {
            pizzas.remove(ultimaPizza);
        }
        else {
            pizzas.get(ultimaPizza).removerPedacos(1);
        }
    }
    
    public void adicionarPizza(String tipoPizza, int pedacos) {
        if(pizzas.containsKey(tipoPizza)) {
            pizzas.get(tipoPizza).adicionarPedacos(pedacos);
            return;
        }
        
        Pizza pizza = null;
        switch(tipoPizza) {
            case "Pizza de Calabresa":
                pizza = new PizzaCalabresa(0);
                break;
            case "Pizza de Frango com Catupiry":
                pizza = new PizzaFrangoCatupiry(0);
                break;
            case "Pizza Margherita":
                pizza = new PizzaMargherita(0);
                break;
            case "Pizza Portuguesa":
                pizza = new PizzaPortuguesa(0);
                break;
        }
        
        pizza.adicionarPedacos(pedacos);
        
        pizzas.put(tipoPizza, pizza);
    }
    
    public Map<String, Pizza> getPizzas() {
        return pizzas;
    }
}
