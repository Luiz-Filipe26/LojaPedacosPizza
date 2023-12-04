/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.lojapedacospizza.core;

import com.mycompany.lojapedacospizza.controle.LojaController;
import com.mycompany.lojapedacospizza.objetos.Area;
import com.mycompany.lojapedacospizza.objetos.Cliente;
import com.mycompany.lojapedacospizza.objetos.Ponto;
import java.util.HashMap;
import java.util.List;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

/**
 *
 * @author Luiz
 */
public class Desenho {
    private static Desenho desenho;
    private GraphicsContext gc;
    private LojaController lojaController;
    
    
    private Image clienteImg;
    private Image balaoImg;
    private Area balaoArea;
    
    private double altura;
    private double largura;
        
    private HashMap<String, Ponto> pontosPizza = new HashMap<>();
    
    private HashMap<Integer, Image> pedacosImagem = new HashMap<>();
    
    public synchronized static Desenho getInstancia() {
        return getInstancia(null);
    }
    
    public synchronized static Desenho getInstancia(GraphicsContext gc) {
        if(desenho == null) {
            desenho = new Desenho(gc);
        }
        return desenho;
    }
    
    private Desenho(GraphicsContext gc) {
        lojaController = LojaController.getInstancia();
        clienteImg = new Image(getClass().getResourceAsStream("/client.png"));
        balaoImg = new Image(getClass().getResourceAsStream("/balao.png"));
        
        
        this.gc = gc;
        
        largura = gc.getCanvas().getWidth();
        altura = gc.getCanvas().getHeight();
        
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, largura, altura);
        
        inicializarColecoes();
        desenharTela();
    }
    
    public void inicializarColecoes() {
        
        Ponto primeiroPonto = new Ponto(380, (int) altura - 80);
        pontosPizza.put("Pizza de Calabresa", primeiroPonto);
        pontosPizza.put("Pizza de Frango com Catupiry", primeiroPonto.add(0, -60));
        pontosPizza.put("Pizza Margherita", primeiroPonto.add(0, -120));
        pontosPizza.put("Pizza Portuguesa", primeiroPonto.add(0, -180));
        
        for(int i=1; i<=8; i++) {
            Image imagem = new Image(getClass().getResourceAsStream("/pizza" + i + ".png"));
            Image imagemRedimensionada = redimensionarImagem(imagem, 50, 50);
            
            pedacosImagem.put(i, imagemRedimensionada);
        }
        
        pontosPizza.forEach((tipoPizza, v) -> {
            desenharPizza(tipoPizza, 8);
        });
    }
    
    public void desenharTela() {
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, 350, altura);
        
        gc.setFill(Color.YELLOW);
        gc.fillRect(320, 30, 30, altura - 60);
        
        List<Cliente> clientes = lojaController.getClientes();
        if(clientes.isEmpty()) {
            return;
        }
        Cliente cliente = lojaController.getCliente();
        
        gc.setFill(Color.BLUE);
        for(Cliente c : clientes) {
            gc.fillRect(10, altura - (c.y + clienteImg.getHeight() / 2), 20, 20);
        }
        
        gc.drawImage(clienteImg, cliente.x, altura - cliente.y - clienteImg.getHeight());
        
        
        boolean mostrar = lojaController.checarMostrarBalao();
        if(mostrar) {
            balaoPedir(cliente);
        }
    }
    
    public void desenharPizza(String tipoPizza, int pedacosRestante) {
        Ponto pontoPizza = pontosPizza.get(tipoPizza);
        Image imagemPizza = pedacosImagem.get(pedacosRestante);
        
        gc.drawImage(imagemPizza, pontoPizza.x, pontoPizza.y);
    }
    
    private Image redimensionarImagem(Image imagemOriginal, double novaLargura, double novaAltura) {
        ImageView imageView = new ImageView(imagemOriginal);
        imageView.setSmooth(true);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(novaLargura);
        imageView.setFitHeight(novaAltura);
        
        Image imagemRedimensionada = imageView.snapshot(null, null);
        return imagemRedimensionada;
    }

    public void balaoPedir(Cliente cliente) {
        double x = cliente.x + 20;
        double y = (altura - cliente.y) - clienteImg.getHeight() - balaoImg.getHeight();
        
        gc.drawImage(balaoImg, x, y);
        gc.setFill(Color.BLACK);
        gc.fillText("Pedir", (x + balaoImg.getWidth() / 2) - 15, (y + balaoImg.getWidth() / 2));
        
        int x1Clique = (int) x + 10;
        int y1Clique = (int) y + 10;
        
        int x2Clique = x1Clique + 40;
        int y2Clique = y1Clique + 30;
        
        balaoArea = new Area(x1Clique, y1Clique, x2Clique, y2Clique);
    }

    public void mouseClique(int x, int y) {
        List<Cliente> clientes = lojaController.getClientes();
        String clienteAtual = clienteClicado(clientes, x, y);
        
        if(clienteAtual != null) {
            lojaController.setClienteAtual(clienteAtual);
        }
        else if(isBalaoClique(x, y)) {
            lojaController.habilitarPedir();
        }
        
        desenharTela();
    }
    
    public String clienteClicado(List<Cliente> clientes, int x, int y) {
        for(Cliente cliente : clientes) {
            double x1 = 10;
            double y1 = altura - (cliente.y + clienteImg.getHeight() / 2);
            double x2 = x1 + 20;
            double y2 = y1 + 20;
            
            if(x >= x1 && x <= x2 && y >= y1 && y <= y2) {
                return cliente.nome;
            }
        }
        return null;
    }
    
    public boolean isBalaoClique(int x, int y) {
        return balaoArea != null && x >= balaoArea.x1 && x <= balaoArea.x2 && y >= balaoArea.y1 && y<=balaoArea.y2;
    }

    public void atualizarPizzas(String tipo, int pedacos, int pedacosRestantes) {
        desenharPizza(tipo, pedacosRestantes);
    }
}
