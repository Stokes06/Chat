/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package View;
import Model.*;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author Marie_Estelle
 */
public class MainView extends Application implements Observer {
    Client cli;
    private Label lb;
    private Scene scene;
    private Reception communication;
    ScrollPane sp;
    HBox root = new HBox();
    @Override
    public void start(Stage primaryStage) throws IOException {
        cli = new Client();
        
        
        communication = new Reception(cli.getRecepteur());
        TextField text = new TextField();
        text.setPadding(new Insets(10,10,10,10));
        Button btnOk = new Button("Envoyer");
        btnOk.setPrefSize(410,25);
        btnOk.setDefaultButton(true);
        btnOk.setCursor(Cursor.HAND);
        btnOk.setStyle("-fx-text-fill : darkgreen; -fx-background-color : black; -fx-font-size : 30;");
        btnOk.setOnAction(new EventHandler<ActionEvent>(){
            
            
            @Override
            public void handle(ActionEvent event) {
                cli.envoyer(text.getText());
                text.setText("");
            }
            
        });
        
        sp = new ScrollPane(communication);
       
        
        sp.setPrefHeight(495);
        
        BorderPane bp = new BorderPane();
       
        
        bp.setTop(sp);
        bp.setCenter(text);
        bp.setBottom(btnOk);
        
        BorderPane.setAlignment(btnOk, Pos.CENTER);
        BorderPane.setAlignment(text, Pos.BOTTOM_CENTER);
        
        scene = new Scene(bp, 400, 600);
        
        primaryStage.setTitle("Salon de Discussion privé");
        primaryStage.setX(50);
        primaryStage.setY(50);
        primaryStage.setScene(scene);
        //  primaryStage.setResizable(false);
        
        
        
        primaryStage.setOnCloseRequest(new EventHandler(){
            @Override
            public void handle(Event event) {
                System.exit(0);
            }
        });
        primaryStage.show();
        
        
         /**
         * la variable hProperty, lorsqu'elle changera, le signalera a mon scrollPane 
         * et lui dira de faire descendre la barre de defilement
         * fonction bind pour dire ce que represente cette variable
         */
        DoubleProperty hProperty = new SimpleDoubleProperty();
        hProperty.bind(communication.heightProperty()); // bind to Hbox height changes
        hProperty.addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue ov, Object t, Object t1) {
                //when ever Hbox width chnages set ScrollPane Hvalue
                sp.setVvalue(sp.getVmax());
            }
        }) ;
    }
    
    
    /***
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        launch(args);
        
        
    }
    
    @Override
    public void update(Observable o, Object o1) {
     //   sp.setVvalue(sp.getVmax());
        
    }
    
}
