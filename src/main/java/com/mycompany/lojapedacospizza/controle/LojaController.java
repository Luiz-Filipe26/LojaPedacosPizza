/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.lojapedacospizza.controle;

import com.mycompany.lojapedacospizza.core.Desenho;
import com.mycompany.lojapedacospizza.core.ClienteLogic;
import com.mycompany.lojapedacospizza.view.LojaFXMLController;
import com.mycompany.lojapedacospizza.core.Mesa;
import com.mycompany.lojapedacospizza.objetos.Cliente;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Luiz
 */
public class LojaController {
    private static LojaController lojaController;
    
    private HashMap<String, ClienteLogic> clientesPorNome = new HashMap<>();
    private List<ClienteLogic> clientesLogic = new ArrayList<>();
    private List<Cliente> clientes = new ArrayList<>();
    
    private LojaFXMLController lojaFXMLController;
    private String clienteAtual;
    private Desenho desenho;
    private Mesa mesa;
    
    public synchronized static LojaController getInstancia() {
        if(lojaController == null) {
            lojaController = new LojaController();
        }
        return lojaController;
    }
    
    private LojaController() {
    }
    
    public void adicionarClienteLogic(String nome) {
        if(clientesLogic.isEmpty()) {
            clienteAtual = nome;
        }
        
        int y = (clientesLogic.size() + 2) * 30;
        ClienteLogic clienteLogic = new ClienteLogic(nome, 30, y);
        clientes.add(clienteLogic.getCliente());
        clientesLogic.add(clienteLogic);
        clientesPorNome.put(nome, clienteLogic);
    }
    
    public void setMesa(Mesa mesa) {
        this.mesa = mesa;
    }
    
    public void setDesenho(Desenho desenho) {
        this.desenho = desenho;
    }
    
    public void setFXMLController(LojaFXMLController lojaFXMLController) {
        this.lojaFXMLController = lojaFXMLController;
    }
    
    public void setClienteAtual(String clienteAtual) {
        this.clienteAtual = clienteAtual;
    }
    
    public void notificarCozinhando() {
        lojaFXMLController.notificarCozinhando();
    }
    
    public void solicitarMesa(int quantidadeSolicitada, String tipoPizza) {
        mesa.solicitarMesa(quantidadeSolicitada, tipoPizza);
    }
    
    public void clienteSolicitarPizza(int quantidadeSolicitada, String tipoPizza) {
        ClienteLogic clienteLogic = clientesPorNome.get(clienteAtual);
        
        clienteLogic.clienteSolicitarPizza(quantidadeSolicitada, tipoPizza);
    }
    
    public void entregarPizza(String tipoPizza, int pedacos, int pedacosRestantes) {
        desenho.atualizarPizzas(tipoPizza, pedacos, pedacosRestantes);
        ClienteLogic clienteLogic = clientesPorNome.get(clienteAtual);
        
        
        lojaFXMLController.entregarPizza(tipoPizza, pedacos);
    }

    public void viewFechada() {
        mesa.viewFechada();
    }

    public void keyPressed(String keyCode) {
        clientesPorNome.get(clienteAtual).keyPressed(keyCode);
    }

    public Cliente getCliente() {
        return clientesPorNome.get(clienteAtual).getCliente();
    }
    
    public List<Cliente> getClientes() {
        return clientes;
    }

    public void desenharCliente(Cliente cliente) {
        desenho.desenharTela();
    }

    public void desenharCliente(String nome) {
        desenho.desenharTela();
    }

    public void balaoPedir(Cliente cliente) {
        desenho.balaoPedir(cliente);
    }

    public void mouseClique(int x, int y) {
        desenho.mouseClique(x, y);
    }

    public void habilitarPedir() {
        lojaFXMLController.habilitarPedir();
    }
    
    public boolean checarMostrarBalao() {
        return clientesPorNome.get(clienteAtual).checarMostrarBalao();
    }
}
