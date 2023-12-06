/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.lojapedacospizza.core;

import com.mycompany.lojapedacospizza.controle.LojaController;
import com.mycompany.lojapedacospizza.objetos.Area;
import com.mycompany.lojapedacospizza.objetos.Cliente;
import com.mycompany.lojapedacospizza.objetos.Pizza;
import com.mycompany.lojapedacospizza.objetos.Ponto;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

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
        gc.setGlobalBlendMode(BlendMode.SRC_OVER);
        
        largura = gc.getCanvas().getWidth();
        altura = gc.getCanvas().getHeight();
        
        limparTela();
        inicializarColecoes();
        desenharTela();
        
        pontosPizza.forEach((tipoPizza, v) -> {
            desenharPizza(tipoPizza, 8);
            desenharTextoPizza(tipoPizza);
        });
        
    }
    
    public void limparTela() {
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, largura, altura);
    }
    
    public void inicializarColecoes() {
        
        Ponto primeiroPonto = new Ponto(360, (int) altura - 80);
        pontosPizza.put("Pizza de Calabresa", primeiroPonto);
        pontosPizza.put("Pizza de Frango com Catupiry", primeiroPonto.add(0, -60));
        pontosPizza.put("Pizza Margherita", primeiroPonto.add(0, -120));
        pontosPizza.put("Pizza Portuguesa", primeiroPonto.add(0, -180));
        
        Image imagemTotal = new Image(getClass().getResourceAsStream("/pizza_sprites.png"));
        
        for(int i=0; i<8; i++) {
            Image subImagem = cortarImagem(imagemTotal, (i % 3) * 512, (i/3) * 512, 512, 512);
            Image imagemRedimensionada = redimensionarImagem(subImagem, 50, 50);
            
            pedacosImagem.put(i+1, imagemRedimensionada);
        }
    }
    
    private Image cortarImagem(Image imagemOriginal, int x, int y, int dx, int dy) {
        PixelReader pixelReader = imagemOriginal.getPixelReader();
        Image imagemCortada = new WritableImage(pixelReader, x, y, dx, dy);
        return imagemCortada;
    }
    
    private Image redimensionarImagem(Image imagemOriginal, double novaLargura, double novaAltura) {
        ImageView imageView = new ImageView(imagemOriginal);
        
        imageView.setSmooth(true);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(novaLargura);
        imageView.setFitHeight(novaAltura);
        
        SnapshotParameters sp = new SnapshotParameters();
        sp.setFill(Color.TRANSPARENT);
        Image imagemRedimensionada = imageView.snapshot(sp, null);
        
        return imagemRedimensionada;
    }
    
    public void desenharTela() {
        
        limparAreaClientes();
        desenharMesa();
        
        List<Cliente> clientes = lojaController.getClientes();
        if(clientes.isEmpty()) {
            return;
        }
        
        desenharSelecoesCliente(clientes);
        
        desenharClienteEPizza();
        
        boolean mostrar = lojaController.checarMostrarBalao();
        if(mostrar) {
            balaoPedir();
        }
    }
    
    public void desenharClienteEPizza() {
        Cliente clienteAtual = lojaController.getCliente();
        int xCliente = clienteAtual.x;
        int yCliente = (int) (altura - clienteAtual.y - clienteImg.getHeight());
        gc.drawImage(clienteImg, xCliente, yCliente);
        
        
        Ponto pontoMao = new Ponto(73, 49);
        Ponto pontoCentroPizza = new Ponto(-273 * 50 / 512, -262 * 50 / 512);
        Ponto pontoPizza = new Ponto(xCliente, yCliente).addPonto(pontoMao).addPonto(pontoCentroPizza);
        
        
        Map<String, Pizza> pizzas = clienteAtual.getPizzas();
        
        final int[] dy = {0};
        pizzas.forEach((tipoPizza, pizza) -> {
            Image imagemPizza;
            
            for(int i=0; i<pizza.getPedacosRestantes(); i+=8) {
                int indiceImagem = Math.min(8, pizza.getPedacosRestantes() - i);
                imagemPizza = pedacosImagem.get(indiceImagem);
                gc.drawImage(imagemPizza, pontoPizza.x, pontoPizza.y + dy[0]);
                dy[0] -= 5;
            }
        });
    }
    
    public void limparAreaClientes() {
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, 350, altura);
    }
    
    public void desenharMesa() {
        gc.setFill(Color.YELLOW);
        gc.fillRect(320, 30, 30, altura - 60);
    }
    
    public void desenharSelecoesCliente(List<Cliente> clientes) {
        
        gc.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        for(Cliente cliente : clientes) {
            gc.setFill(Color.BLUE);
            gc.fillRect(10, altura - (cliente.y + clienteImg.getHeight() / 2), 20, 20);
            gc.setFill(Color.BLACK);
            gc.fillText(cliente.nome, 6, altura - (cliente.y + clienteImg.getHeight() / 2) + 25);
        }
        gc.setFont(new Font(14));
    }

    public void balaoPedir() {
        Cliente cliente = lojaController.getCliente();
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
    
    public void desenharPizza(String tipoPizza, int pedacosRestantes) {
        Ponto pontoPizza = pontosPizza.get(tipoPizza);
        Image imagemPizza = pedacosImagem.get(pedacosRestantes);
        
        gc.setFill(Color.WHITE);
        gc.fillRect(pontoPizza.x, pontoPizza.y, imagemPizza.getWidth(), imagemPizza.getHeight());
        gc.drawImage(imagemPizza, pontoPizza.x, pontoPizza.y);
    }
    
    public void desenharTextoPizza(String tipoPizza) {
        Ponto pontoPizza = pontosPizza.get(tipoPizza);
        Image imagemPizza = pedacosImagem.get(1);
        String textoPizza[] =  tipoPizza.split(" ");
        String textoPizza1 = "", textoPizza2 = "", textoPizza3 = "";
        
        switch (textoPizza.length) {
            case 2:
                textoPizza1 = textoPizza[0];
                textoPizza2 = textoPizza[1];
                break;
            case 3:
                textoPizza1 = textoPizza[0] + " " + textoPizza[1];
                textoPizza2 = textoPizza[2];
                break;
            default:
                textoPizza1 = textoPizza[0] + " " + textoPizza[1];
                textoPizza2 = textoPizza[2] + " " + textoPizza[3];
                textoPizza3 = textoPizza[4];
                break;
        }
        
        gc.setFont(new Font(12));
        gc.setFill(Color.BLACK);
        gc.fillText(textoPizza1, pontoPizza.x + imagemPizza.getWidth(), pontoPizza.y + 20);
        gc.fillText(textoPizza2, pontoPizza.x + imagemPizza.getWidth(), pontoPizza.y + 40);
        if(!textoPizza3.isEmpty()) {
            gc.fillText(textoPizza3, pontoPizza.x + imagemPizza.getWidth(), pontoPizza.y + 60);
        }
    }

    public void atualizarPizzas(String tipo, int pedacosRestantes) {
        desenharPizza(tipo, pedacosRestantes);
    }

    public int getCanvasAltura() {
        return (int) altura;
    }

    public int getClienteAltura() {
        return (int) clienteImg.getHeight();
    }

    public Area getBalaoArea() {
        return balaoArea;
    }
    
}