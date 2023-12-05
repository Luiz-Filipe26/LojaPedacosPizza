package com.mycompany.lojapedacospizza.controle;

import com.mycompany.lojapedacospizza.core.Cozinheiro;
import com.mycompany.lojapedacospizza.core.Desenho;
import com.mycompany.lojapedacospizza.core.ClienteLogic;
import com.mycompany.lojapedacospizza.core.LojaLogic;
import com.mycompany.lojapedacospizza.core.Mesa;
import com.mycompany.lojapedacospizza.view.LojaFXMLController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashSet;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;

/**
 * JavaFX App
 */
public class App extends Application {
    
    private LojaController lojaController;
    private HashSet<KeyCode> teclasValidas;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/LojaView.fxml"));
        Parent root = fxmlLoader.load();
        
        lojaController = LojaController.getInstancia();
        
        LojaFXMLController lojaFXMLController = fxmlLoader.getController();
        lojaController.setFXMLController(lojaFXMLController);
        
        Mesa mesa = new Mesa(Cozinheiro.getInstancia());
        mesa.start();
        lojaController.setMesa(mesa);
        
        Canvas canvas = (Canvas) root.lookup("#canvasGrafico");
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Desenho desenho = Desenho.getInstancia(gc);
        lojaController.setDesenho(desenho);
        
        LojaLogic lojaLogic = new LojaLogic();
        lojaController.setLojaLogic(lojaLogic);
        
        Scene scene = new Scene(root);
        stage.setScene(scene);
        
        criarListeners(stage, scene);
        
        stage.show();
    }

    public void criarListeners(Stage stage, Scene scene) {
        stage.setOnCloseRequest(e -> {
            LojaController.getInstancia().viewFechada();
            Platform.exit();
        });
        
        
        teclasValidas = new HashSet(){{
            add(KeyCode.LEFT); add(KeyCode.RIGHT);
        }};
        
        scene.setOnKeyPressed(event -> {
            KeyCode keyCode = event.getCode();
            if(teclasValidas.contains(keyCode)) {
                lojaController.keyPressed(keyCode.toString());
            }
        });
    }
}