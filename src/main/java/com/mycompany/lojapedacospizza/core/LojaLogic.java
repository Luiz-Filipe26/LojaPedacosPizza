
package com.mycompany.lojapedacospizza.core;

import com.mycompany.lojapedacospizza.controle.LojaController;
import com.mycompany.lojapedacospizza.objetos.Area;
import com.mycompany.lojapedacospizza.objetos.Cliente;
import java.util.List;
import javafx.animation.AnimationTimer;


public class LojaLogic {
    
    private final LojaController lojaController = LojaController.getInstancia();
    private final int altura;
    private final int clienteAltura;
    
    public LojaLogic() {
        altura = lojaController.getCanvasAltura();
        clienteAltura = lojaController.getClienteAltura();
        avancarhorario();
    }
    
    public void avancarhorario() {
//        new AnimationTimer() {
//            long duracaoNano = (long) (duracao * 1e9);
//            long tempoInicio = -1;
//            
//
//            @Override
//            public void handle(long now) {
//                if (tempoInicio < 0) {
//                    tempoInicio = now;
//                }
//
//                double progresso = (now - tempoInicio) / duracaoNano;
//
//                
//            }
//        }.start();
    }
    
    public void mouseClique(int x, int y) {
        List<Cliente> clientes = lojaController.getClientes();
        String clienteAtual = clienteClicado(clientes, x, y);
        
        if(clienteAtual != null) {
            lojaController.setClienteAtual(clienteAtual);
            lojaController.desenharTela();
            lojaController.desabilitarPedir();
        }
        else if(isBalaoClique(x, y)) {
            lojaController.habilitarPedir();
        }
    }
    
    public String clienteClicado(List<Cliente> clientes, int x, int y) {
        for(Cliente cliente : clientes) {
            double x1 = 10;
            double y1 = altura - (cliente.y + clienteAltura / 2);
            double x2 = x1 + 20;
            double y2 = y1 + 20;
            
            if(x >= x1 && x <= x2 && y >= y1 && y <= y2) {
                return cliente.nome;
            }
        }
        return null;
    }
    
    public boolean isBalaoClique(int x, int y) {
        Area balaoArea = lojaController.getBalaoArea();
        return balaoArea != null && x >= balaoArea.x1 && x <= balaoArea.x2 && y >= balaoArea.y1 && y<=balaoArea.y2;
    }
}