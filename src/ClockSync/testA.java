/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClockSync;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author anando
 */
public class testA {
    void doIt() throws IOException, InterruptedException{
        ServerSocket server = new ServerSocket(1234);
        Socket socket = server.accept();
        try{
            //Scanner sc = new Scanner(socket.getInputStream());
            DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());
            System.out.println("Starting Work Server");
            Thread.sleep(1000);
            while(true){
                dOut.writeUTF("1");
                dOut.flush();
            }
        }catch(Exception e){
            System.out.println("Found Exception");
        }
    }
    public static void main(String[] args) throws IOException, InterruptedException {
        new testA().doIt();
    }
}
