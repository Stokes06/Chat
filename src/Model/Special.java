/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author Marie_Estelle
 */
public enum Special {
    DECO("!deco"),
    SERVEUR("!serveur"),
    MOI("!moi");
    private String info;
     Special(String s)
    {
        this.info = s;
    }
    public String toString()
    {
        return this.info;
    }
    
    
}
