/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JRMI.server;

import java.io.File;
import java.io.FileReader;
import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author anando
 */
class Describe implements Encryption{
    public String cipher()throws RemoteException{
        String keyword = "GORDIANBCEFHJKLMPQSTUVWXYZ";
        int[] index = new int[28];
        for(int i=0;i<keyword.length();i++){
            int ind = keyword.charAt(i)-'A';
            index[ind]=i;
        }
        StringBuilder ret = new StringBuilder();
        int c=-55;
        System.out.println("Hello");
        try{
            FileReader f = new FileReader("./src/JRMI/server/Cipher.txt");
            
            while((c = f.read()) != -1){
                System.out.print((char)c);
                char ch = (char)(index[c-'A']+'A');
                ret.append(ch);
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return ret.toString();
    }
    @Override
    public String vigenere() throws RemoteException {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        StringBuilder ret=new StringBuilder();
        String keyword = "GORDIAN";
        try{
            FileReader f = new FileReader("./src/JRMI/server/TabulaRecta.txt");
            int k=0,c=0;
            while((c = f.read()) != -1){
                if(c>='A' && c<='Z'){
                    int ind = c- keyword.charAt(k++);
                    k%=keyword.length();
                    if(ind<0)ind+=26;
                    ret.append((char)(ind+'A'));
                }else{ 
                    ret.append((char)c);
                    k=0;
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return ret.toString();
    }
}
public class Server extends Describe{

    public Server() throws RemoteException, AlreadyBoundException {
        try {
            LocateRegistry.createRegistry(1099);
        } catch (RemoteException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        doTask();
    }
    
    private void doTask() throws RemoteException, AlreadyBoundException{
        Describe obj = new Describe();
        Encryption stub = (Encryption)UnicastRemoteObject.exportObject(obj, 0);
        Registry rej = LocateRegistry.getRegistry(1099);
        rej.bind("Enc",stub);
        System.err.println("server ready");
    }
    public static void main(String[] args) {
        try {
            new Server();
        } catch (RemoteException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AlreadyBoundException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
