/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package distributedsystem.KnockKnock;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author anando
 */
public class TestServer {

    public TestServer() {
        doTask();
    }
    private void doTask(){
        try {
            ServerSocket server = new ServerSocket(3605);
            System.out.println("Waiting for client");
            Socket socket = server.accept();
            System.out.println("Got it");
            /*DataInputStream dIn = new DataInputStream(socket.getInputStream());
            DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());
            
            while(true){
                while(dIn.available()<=0)continue;
                String str = dIn.readUTF();
                System.out.println(str);
                dOut.writeUTF(str); dOut.flush();
                if(str.contains("exit")){
                    socket.close();
                    server.close();
                    return;
                }
            }*/
        } catch (IOException ex) {
            Logger.getLogger(TestServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    public static void main(String[] args) {
        new TestServer().doTask();
    }
}
