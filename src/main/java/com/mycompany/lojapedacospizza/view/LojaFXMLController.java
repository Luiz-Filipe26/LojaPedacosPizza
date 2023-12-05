package com.mycompany.lojapedacospizza.view;

import com.mycompany.lojapedacospizza.controle.LojaController;
import com.mycompany.lojapedacospizza.core.ClienteLogic;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class LojaFXMLController implements Initializable {
    
    @FXML
    private AnchorPane anchorPane;
    
    @FXML
    private Button buttonPedir;
    @FXML
    private Canvas canvasGrafico;

    @FXML
    private ComboBox<String> comboBoxQuantidade;

    @FXML
    private ComboBox<String> comboBoxTipoPizza;

    @FXML
    private Label labelConsole;
    
    @FXML
    private TextField textFieldNome;
    
    private LojaController lojaController;
    

    @FXML
    public void pedirPressionado(ActionEvent event) {
        if(comboBoxQuantidade.getValue() == null || comboBoxTipoPizza.getValue() == null) {
            labelConsole.setText("Escolha valores válidos para pedir.");
            return;
        }
        
        comboBoxTipoPizza.setVisible(false);
        comboBoxQuantidade.setVisible(false);
        buttonPedir.setVisible(false);
        int quantidade = Integer.parseInt(comboBoxQuantidade.getValue());
        String pizza = comboBoxTipoPizza.getValue();
        LojaController.getInstancia().clienteSolicitarPizza(quantidade, pizza);
    }
    
    @FXML
    public void mouseClique(MouseEvent event) {
        lojaController.mouseClique((int) event.getX(), (int) event.getY());
    }
    
    @FXML
    public void adicionarPressionado(ActionEvent event) {
        String nome = textFieldNome.getText();
        
        if(nome.isBlank()) {
            labelConsole.setText("Digite um nome!");
            return;
        }
        
        if(lojaController.nomeExiste(nome)) {
            labelConsole.setText("Nome já existe!");
            return;
        }
        
        labelConsole.setText("");
        textFieldNome.setText("");
        lojaController.adicionarClienteLogic(nome);
        lojaController.desenharCliente(nome);
    }
    
    
    @FXML
    public void focarTeclado(MouseEvent event) {
        anchorPane.requestFocus();
    }
    
    public void habilitarPedir() {
        comboBoxTipoPizza.setVisible(true);
        comboBoxQuantidade.setVisible(true);
        
        Platform.runLater(() -> {
            buttonPedir.setVisible(true);
        });
    }
    
    public void desabilitarPedir() {
        comboBoxTipoPizza.setVisible(false);
        comboBoxQuantidade.setVisible(false);
        
        Platform.runLater(() -> {
            buttonPedir.setVisible(false);
        });
    }
    
    public void notificarCozinhando() {
        Platform.runLater(() -> {
            labelConsole.setText("Cozinhando...");
        });
    }
    
    public void entregarPizza(String tipoPizza, int pedacos) {
        Platform.runLater(() -> {
            labelConsole.setText(tipoPizza + " com " + pedacos + " pedaços foi entregue!");
        });
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        buttonPedir.setVisible(false);
        lojaController = LojaController.getInstancia();
        
        ObservableList<String> listaPizza = FXCollections.observableArrayList(
        "Pizza de Calabresa", "Pizza de Frango com Catupiry", "Pizza Margherita", "Pizza Portuguesa");
        ObservableList<String> listaQuantidade = FXCollections.observableArrayList(
        "1", "2", "3", "4", "5", "6", "7", "8");
        
        comboBoxTipoPizza.setItems(listaPizza);
        comboBoxQuantidade.setItems(listaQuantidade);
        
        comboBoxTipoPizza.setVisible(false);
        comboBoxQuantidade.setVisible(false);
    }
}
