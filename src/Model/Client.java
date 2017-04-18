package Model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Observable;
import java.util.Scanner;

public class Client extends Observable {
    RSA rsa;
    private  Socket clientSocket;
    private int port;
    public  BufferedReader in;
    public  PrintWriter out;
    private boolean connexion = false;
    private Recepteur recepteur;
    public Client() throws IOException{
       
        port = 5000;
        clientSocket = new Socket("localhost",this.port);
        in = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
        out = new PrintWriter(this.clientSocket.getOutputStream());
        connexion = true;
        recepteur = new Recepteur(this);
        
    }
    public static void main(String[] args) throws IOException {
        Client cli = new Client();
       
        
        final Scanner sc = new Scanner(System.in);//pour lire à partir du clavier
        Thread envoyer = new Thread(new Runnable() {
            String msg;
            @Override
            public void run() {
                while(true){
                    try{
                        
                        msg = sc.nextLine();
                        cli.out.println(msg);
                        cli.out.flush();
                    }catch(Exception e)
                    {
                        e.printStackTrace();
                        System.exit(1);
                    }
                    
                }
            }
        });
        envoyer.start();
       
        Thread recevoir = new Thread(new Runnable() {
            public void run() {
                String msg;
                try {
                    msg = cli.in.readLine();
                    
                    while(msg!=null){
                        System.out.println(msg);
                        msg = cli.in.readLine();
                        
                    }
                    System.out.println("Serveur déconnecté");
                    cli.out.close();
                    cli.getSocket().close();
                } catch (IOException e) {
                    e.printStackTrace();
                    System.exit(1);
                }
            }
        });
                recevoir.start();
                
    }
    
    public void envoyer(String msg)
    {
        try{
            this.out.println(msg);
            this.out.flush();
        }catch(Exception e)
        {
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    public Socket getSocket()
    {
        return this.clientSocket;
    }
    public boolean IsConnected()
    {
        return this.connexion;
    }
    public Recepteur getRecepteur()
    {
        return this.recepteur;
    }
}