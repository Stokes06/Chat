/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package View;

import Model.Recepteur;
import Model.Special;
import java.util.Observable;
import java.util.Observer;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;

import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Reception extends VBox implements Observer {
    Recepteur r;
    Thread th;
    int nbMsg;
    
    //   final ScrollBar sc;
    public Reception(Recepteur _r)
    {
        
        
        this.nbMsg = 0;
        this.r = _r;
        this.r.addObserver(this);
        th = new Thread(this.r);
        th.start();
    }
    
    @Override
    public void update(Observable o, Object o1) {
        Label l = new Label();
        l.setFont(Font.font("System", FontWeight.BOLD, 15));
        Reception rec = this;
        String message = r.getMessage();
        Platform.runLater(new Runnable(){
            @Override
            public void run() {
                String messageRecu = new String();
                if(message.startsWith(Special.MOI.toString()))
                {
                    
                    l.setStyle("-fx-text-fill : green;");
                    messageRecu = message.replaceFirst("!", "");
                }else if(message.startsWith(Special.DECO.toString()))
                {
                    l.setStyle("-fx-text-fill : orange;");
                    messageRecu = message.replaceFirst(Special.DECO.toString(),"");
                    
                }else if(message.startsWith(Special.SERVEUR.toString())){
                    l.setStyle("-fx-text-fill : purple;");
                    messageRecu = message.replaceFirst(Special.SERVEUR.toString(),"");
                }else{
                    messageRecu = message;
                    l.setStyle("-fx-text-fill : darkblue;");
                }
                l.setText(messageRecu);
                
                
                rec.incrementerNbMsg();
                rec.getChildren().add(l);
            }
        });
        
        
    }
    
    public int getNbMsg(){
        return this.nbMsg;
    }
    
    public void incrementerNbMsg()
    {
        this.nbMsg++;
               // je ne me sers plus d'observer ici mais peut etre plus tard donc fonction utilise potentiellement
    }

    
}