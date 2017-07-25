/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package distributedsystem.KnockKnock;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author anando
 */
public class KnockClient {
    private String ip;
    private int port;
    private int client_token;

    public KnockClient(String ip , int port) {
        this.ip = ip;
        this.port = port;
        client_token = new Random().nextInt(100);
        
        doTask();
    }
    
    private void doTask(){
        System.out.printf("Client #%d is trying to establish connection\n",client_token);
        try{
            Socket socket = new Socket(ip, port);
            System.out.println("Connection established");
            
            DataInputStream dIn = new DataInputStream(socket.getInputStream());
            DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());
            Scanner sc = new Scanner(System.in);
            
            dOut.writeUTF(""+client_token);dOut.flush();
            System.out.println("Token Sent To Server");
            
            while(true){
                String dataFromServer,dataToServer;
                while(dIn.available() <=0)continue;
                dataFromServer = dIn.readUTF();
                System.out.println(dataFromServer);
                if(dataFromServer.startsWith("You are supposed to say"))continue;
                else if(dataFromServer.contains("(Y/N)")){
                    System.out.print("> ");
                    dataToServer = sc.nextLine();
                    dOut.writeUTF(dataToServer); dOut.flush();
                    if(dataToServer.endsWith("N")){
                        socket.close(); dIn.close(); dOut.close(); sc.close();
                        return;
                    }
                    continue;
                }
                else if(dataFromServer.startsWith("I have no more jokes")){
                    socket.close(); dIn.close(); dOut.close(); sc.close();
                    return;
                }
                System.out.print("> ");
                dataToServer = sc.nextLine();
                dOut.writeUTF(dataToServer); dOut.flush();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        
    }
    
}
