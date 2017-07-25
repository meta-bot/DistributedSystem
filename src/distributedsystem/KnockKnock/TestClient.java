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
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author anando
 */
public class TestClient {

    public TestClient() {
        doTask();
    }
    
    private void doTask(){
        try {
            Socket socket = new Socket("127.0.0.1",3600);
            DataInputStream dIn = new DataInputStream(socket.getInputStream());
            DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());
            System.out.println("Client sending");
            while(true){
                String str = new Scanner(System.in).nextLine();
                dOut.writeUTF(str);
                dOut.flush();
                System.out.println("Data to Server -> "+str);
                while(dIn.available()<=0)continue;
                String xx = dIn.readUTF();
                System.out.println("data from server -> "+xx);
                if(xx.contains("exit"))return;
            }
        } catch (IOException ex) {
            Logger.getLogger(TestClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
