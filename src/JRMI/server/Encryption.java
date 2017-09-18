/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JRMI.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author anando
 */
public interface Encryption extends Remote{
    public String cipher()throws RemoteException;
    public String vigenere() throws RemoteException;
}

