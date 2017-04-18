package Model;


import java.io.*;
import java.math.*;

import java.security.SecureRandom;

public final class RSA {
    //Couple composant la clef publique
    
    private PublicKey publicKey;
    private BigInteger privateKey;
    private boolean permission; // permission de decrypter
    private boolean inscription;
    
    public static void main(String args[]) throws IOException
    {
        RSA r1 = new RSA("quica.txt","client");
        
        // r1.WriteKeys("client1.txt", !r1.permission);
        
    }
    public RSA(String filename, String identifiant) throws IOException {
        this.permission = identifiant.equals("serveur")? false : true;
         ReadKeys(filename, this.permission);
    }
    
    /**
     * B utilisera crypter pour l'envoyer à A
     * @param str
     * @return
     */
    public BigInteger crypter(String str) {
        
        
        BigInteger c = new BigInteger(str.getBytes());
        
        c = c.modPow(publicKey.e, publicKey.n);
        
        return c;
        
    }
    
    
    /**
     * A va utiliser cette fonction pour decrypter le nombre aleatoire généré par B
     * pour prouver qu'il a la clef privée et qu'il est bien connectable à B
     * @param c
     * @param PrivateKey
     * @return
     */
    public String decrypter(BigInteger c) {
        if(permission)
        {
            c = c.modPow(this.privateKey, publicKey.n);
            String res = new String(c.toByteArray());
            return  res;
        }
        return null;
    }
    
    public BigInteger getPrivateKey() {
        return privateKey;
    }
    public PublicKey getPublicKey() {
        return publicKey;
    }
    
    public void generateKeys()
    {
        System.out.println("génération des clefs en cours");
        BigInteger p = new BigInteger(150, 90, new SecureRandom());
        BigInteger q = new BigInteger(150, 90, new SecureRandom());
        BigInteger n = p.multiply(q);
        //System.out.println("n = " + n);
        BigInteger phi = p.add(new BigInteger("-1")).multiply(q.add(new BigInteger("-1")));
        //System.out.println("phi = " + phi);
        BigInteger e = new BigInteger(63, 90, new SecureRandom());
        //System.out.println("e = " + e);
        BigInteger  d = e.modInverse(phi);
        //System.out.println("d = " + d);
        publicKey = new PublicKey(n,e);
        privateKey = d;
        System.out.println("génération des clefs terminée");
    }
    
    /**
     * Permet de lire les clefs adéquates ou de les creer si fichier non trouvé
     * @param filename
     * @param isClient
     *
     * @throws IOException
     */
    public synchronized boolean ReadKeys(String filename, boolean isClient) throws IOException
    {
        boolean dejaConnu = false;
        
        String patt = (isClient)? "client/" : "serveur/";
        String nomComplet = patt+filename+".txt";
        File f = new File (nomComplet);
        
        try
        {
            FileReader fr = new FileReader (f);
            
            BufferedReader br = new BufferedReader (fr);
            String premiereLigne = br.readLine();
            
            if(premiereLigne.equals(""))
            {
                
                this.privateKey = null;
                
            }else{
                this.privateKey = new BigInteger(premiereLigne);
                
            }
        
            publicKey = new PublicKey(new BigInteger(br.readLine()),new BigInteger(br.readLine()));
            
            dejaConnu = true;
        }
           
        catch (FileNotFoundException | NullPointerException exception)
        {
           
            if(permission)
            {
                this.generateKeys();
                this.WriteKeys(filename, permission);
            }else{
                //On cherche chez le client
                
                this.ReadKeys(filename,true);
                this.WriteKeys(filename, this.permission);
            }
        }
            //On cherche chez le client
        this.inscription = !dejaConnu;
        System.out.println("dans rsa"+this.inscription);
        return dejaConnu;
    }
    
    public synchronized void WriteKeys(String filename, boolean prive) throws IOException
    {
        
        String patt = (prive)? "client/" : "serveur/";
        String nomComplet = patt+filename+".txt";
        try{
            File f = new File(nomComplet);
            FileWriter fw = new FileWriter(f);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);
            if(prive)
            {
                pw.println(this.getPrivateKey());
                pw.println(this.getPublicKey());
            }else{
                pw.println("");
                pw.println(this.publicKey);
            }
            pw.close();
            
        }catch(IOException e)
        {
            System.out.println("erreur");
        }
    }
    
    public boolean getPermission()
    {
        return this.permission;
    }
    public boolean IsNew()
    {
        return this.inscription;
    }
}

class PublicKey{
    public BigInteger n,e;
    
    public PublicKey(BigInteger _n, BigInteger _e)
    {
        n = _n;
        e = _e;
    }
    public PublicKey getPublic()
    {
        return this;
    }
    @Override
    public String toString()
    {
        String ret = new String();
        ret += this.n.toString();
        ret +="\r\n";
        ret +=this.e.toString();
        
        return ret;
    }
    
}