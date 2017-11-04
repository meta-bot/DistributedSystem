/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClockSync;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author anando
 */
public class testB {
    void doIt() throws IOException, ClassNotFoundException, InterruptedException{
        Socket socket = new Socket("127.0.0.1",1234);
        try{
            DataInputStream dIn = new DataInputStream(socket.getInputStream());
            while(dIn.available()==0)System.out.println("NO DATA");
            while(dIn.available()>0){
                System.out.println(dIn.readUTF());
            }
        }catch(Exception e){
            
        }
    }

    /**
     *
     * @param args
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        new testB().doIt();
    }
}
