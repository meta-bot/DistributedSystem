/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JRMI;

import JRMI.server.Encryption;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

/**
 *
 * @author anando
 */
public class Client {
    public static void main(String[] args) throws RemoteException, NotBoundException {
        Registry rej = LocateRegistry.getRegistry("127.0.0.1", 1099);
        Encryption stub = (Encryption)rej.lookup("Enc");
        System.out.println("press 1 to Chiper and 2 to Tab");
        int flag = new Scanner(System.in).nextInt();
        if(flag==1){
            System.out.println(stub.cipher());
        }else System.out.println(stub.vigenere());
    }
}
