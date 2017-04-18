package Model;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Serveur {
    
    ServerSocket serveurSocket;
    Socket clientSocket;
    int port;
    ArrayList<BufferedReader> clientIn = new ArrayList<>();
    ArrayList<PrintWriter> clientOut = new ArrayList<>();
    public Serveur() throws IOException
    {
        
        this.port = 5000;
    }
    public static void main(String[] test) throws IOException {
        Serveur serv = new Serveur();
        
        final Scanner sc=new Scanner(System.in);
        
        try {
            serv.setServerSocket(new ServerSocket(serv.getPort()));
            System.out.println("On habite à la porte numero "+serv.getPort());
            while(true)
            {
                serv.accueillirClient(serv.getServeurSocket());
            }
        }catch(IOException exc) {
            System.out.println("probleme de connexion");
        }
    }
    public void accueillirClient(ServerSocket serveurSocket) throws IOException
    {
        Socket s = serveurSocket.accept();
        System.out.println("Ca toc à la porte...");
        
        new Chat(s, clientIn, clientOut).start();
        
        
    }
    
    public ServerSocket getServeurSocket()
    {
        return this.serveurSocket;
    }
    public void setServerSocket(ServerSocket ss)
    {
        this.serveurSocket = ss;
    }
    public int getPort()
    {
        return this.port;
    }
}


class Chat extends Thread{
    Socket s;
    boolean nouveauClient;
    ArrayList<BufferedReader> clientIn;
    ArrayList<PrintWriter> clientOut;
    RSA rserv;
    RSA rcli;
    public Chat(Socket _s,  ArrayList<BufferedReader> _clientIn,  ArrayList<PrintWriter> _clientOut)
    {
        this.s = _s;
        this.clientIn = _clientIn;
        this.clientOut = _clientOut;
    }
    public void run()
    {
        PrintWriter out = null;
        BufferedReader in = null;
        try {
            out = new PrintWriter(s.getOutputStream());
            in = new BufferedReader (new InputStreamReader (s.getInputStream()));
            
            
            out.println(Special.SERVEUR+"Bienvenue chez moi !");
            out.flush();
            
            
            out.println(Special.SERVEUR+"Qui etes vous ?");
            out.flush();
            String name = in.readLine();
             if(!authentifier(name)){
                 //je ne sais pas encore
             }
             System.out.println(nouveauClient);
            if(nouveauClient)
            {
                out.println(Special.SERVEUR+"Enchante "+name+" ! C'est la premiere fois que je vous vois");
                out.flush();
            }else{
                out.println(Special.SERVEUR+"Bonjour "+name+", comment allez vous ?");
                out.flush();
                
            }
            if(clientIn.size() == 0)
            {
                out.println(Special.SERVEUR+"Vous etes seul dans le salon pour l'instant");
                out.flush();
            }else{
                out.println(Special.SERVEUR+"Il y a actuellement "+ (clientIn.size()) +" autres personnes dans le salon");
                out.flush();
            }
            
            
            clientOut.add(out);
            clientIn.add(in);
            
            
            
            for(PrintWriter lesAutres : clientOut)
            {
                if(lesAutres != out)
                {
                    lesAutres.println(Special.SERVEUR+name+" vient de rejoindre le salon, nous sommes a present "+clientOut.size()+" personnes");
                    lesAutres.flush();
                }
                
            }
            boolean connecte = true;
            while(connecte)
            {
                //gérer la deconnexion comme il faut
                
                String msg = null;
                try{
                    msg = in.readLine();
                    for(PrintWriter pw : clientOut)
                    {
                        
                        if(pw != out)
                        {
                            pw.println(name+" : "+msg);
                        }else{
                            pw.println(Special.MOI+" : "+msg);
                        }
                        
                        pw.flush();
                        
                    }
                    
                    
                } catch(IOException ex) {
                    
                    clientOut.remove(out);
                    clientIn.remove(in);
                    
                    for(PrintWriter pw : clientOut)
                    {
                        pw.println(Special.DECO+name+" vient de se deconnecter");
                        pw.flush();
                        
                    }
                    
                    System.out.println("client deconnecte");
                    connecte = false;
                    
                }
            }
            
        } catch (IOException ex) {
            System.out.println("client parti"); //quand le client ferme la fenetre au moment de l'identification
            clientOut.remove(out);
            clientIn.remove(in);
            
        }
        
    }
    
    private boolean authentifier(String name) throws IOException
    {
        
        this.rcli = new RSA(name,"client");
        this.rserv = new RSA(name,"serveur");
        this.nouveauClient = rserv.IsNew();
        System.out.println("dans authentifier : "+this.nouveauClient);
        Integer nbAlea = (int) (Math.random()*100000);
        String snbAlea = nbAlea.toString();
        BigInteger bigAlea = rserv.crypter(snbAlea);
        String res = rcli.decrypter(bigAlea);
        if(res.equals(snbAlea))
        {
            System.out.println("authentification réussie");
            return true;
        }
        return false;
    }
}