/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.lojapedacospizza.controle;

import com.mycompany.lojapedacospizza.core.Desenho;
import com.mycompany.lojapedacospizza.core.ClienteLogic;
import com.mycompany.lojapedacospizza.core.GerenciadorClientes;
import com.mycompany.lojapedacospizza.core.LojaLogic;
import com.mycompany.lojapedacospizza.view.LojaFXMLController;
import com.mycompany.lojapedacospizza.core.Mesa;
import com.mycompany.lojapedacospizza.objetos.Area;
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
    
    private LojaFXMLController lojaFXMLController;
    private LojaLogic lojaLogic;
    private Desenho desenho;
    private Mesa mesa;
    private GerenciadorClientes gerenciadorClientes;
    
    public synchronized static LojaController getInstancia() {
        if(lojaController == null) {
            lojaController = new LojaController();
        }
        return lojaController;
    }
    
    private LojaController() {
    }
    
    public void adicionarClienteLogic(String nome) {
        gerenciadorClientes.adicionarClienteLogic(nome);
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
    
    public void setLojaLogic(LojaLogic lojaLogic) {
        this.lojaLogic = lojaLogic;
    }
    
    public void setClienteAtual(String clienteAtual) {
        gerenciadorClientes.setClienteAtual(clienteAtual);
    }
    
    public void setGerenciadorClientes(GerenciadorClientes gerenciadorClientes) {
        this.gerenciadorClientes = gerenciadorClientes;
    }
    
    public void notificarCozinhando() {
        lojaFXMLController.notificarCozinhando();
    }
    
    public void solicitarMesa(int quantidadeSolicitada, String tipoPizza) {
        mesa.solicitarMesa(quantidadeSolicitada, tipoPizza);
    }
    
    public void clienteSolicitarPizza(int quantidadeSolicitada, String tipoPizza) {
        ClienteLogic clienteLogic = gerenciadorClientes.getClienteLogicAtual();
        
        clienteLogic.clienteSolicitarPizza(quantidadeSolicitada, tipoPizza);
    }
    
    public void entregarPizza(String tipoPizza, int pedacos, int pedacosRestantes) {
        desenho.atualizarPizzas(tipoPizza, pedacos, pedacosRestantes);
        ClienteLogic clienteLogic = gerenciadorClientes.getClienteLogicAtual();
        
        
        lojaFXMLController.entregarPizza(tipoPizza, pedacos);
    }

    public void viewFechada() {
        mesa.viewFechada();
        gerenciadorClientes.pararClientes();
    }

    public void keyPressed(String keyCode) {
        gerenciadorClientes.getClienteLogicAtual().keyPressed(keyCode);
    }

    public Cliente getCliente() {
        return gerenciadorClientes.getClienteAtual();
    }
    
    public List<Cliente> getClientes() {
        return gerenciadorClientes.getClientes();
    }

    public void desenharCliente(Cliente cliente) {
        desenho.desenharTela();
    }

    public void desenharCliente(String nome) {
        desenho.desenharTela();
    }

    public void balaoPedir() {
        desenho.balaoPedir();
    }

    public void mouseClique(int x, int y) {
        lojaLogic.mouseClique(x, y);
    }
    
    public void desenharTela() {
        desenho.desenharTela();
    }

    public void habilitarPedir() {
        lojaFXMLController.habilitarPedir();
    }
    
    public boolean checarMostrarBalao() {
        return gerenciadorClientes.getClienteLogicAtual().checarMostrarBalao();
    }

    public int getCanvasAltura() {
        return desenho.getCanvasAltura();
    }
    
    public int getClienteAltura() {
        return desenho.getClienteAltura();
    }
    
    public Area getBalaoArea() {
        return desenho.getBalaoArea();
    }

    public boolean nomeExiste(String nome) {
        return gerenciadorClientes.nomeExiste(nome);
    }
    
    public void desabilitarPedir() {
        lojaFXMLController.desabilitarPedir();
    }
}
