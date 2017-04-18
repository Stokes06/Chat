/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package Model;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

/**
 *
 * @author Marie_Estelle
 */
public class Recepteur extends Observable implements Runnable{
    
    Client cli;
   
    String msg;
    public Recepteur(Client _cli)
    {
        this.cli = _cli;
        msg = new String();
    }
    
    public void run() {
      //  System.out.println("recepteur demarré");
        try {
            msg = cli.in.readLine();
          //  System.out.println("message recu : "+msg);
            this.setChanged();
            this.notifyObservers();
            while(msg!=null){
                
                msg = cli.in.readLine();
            //    System.out.println("message recu : "+msg);
                
                this.setChanged();
                this.notifyObservers();
            }
          //  System.out.println("Serveur déconnecté");
            cli.out.close();
            cli.getSocket().close();
        } catch (IOException e) {
            System.out.println("Serveur déconnecté, revenez plus tard");
            System.exit(1);
        }
    }
    
    public String getMessage()
    {
        return this.msg;
    }
}
