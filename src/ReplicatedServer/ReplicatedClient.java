/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ReplicatedServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author anando
 */
public class ReplicatedClient {

    private int numberOfIteration;
    private int portNumber;
    private String ip;
    private DataInputStream dIn;
    private DataOutputStream dOut;

    public ReplicatedClient(int n, int portNumber, String ip) {
        this.numberOfIteration = n;
        this.portNumber = portNumber;
        this.ip = ip;
        doTask();
    }

    private void doTask() {
        try{
            Socket socket = new Socket(ip, portNumber);
            dIn = new DataInputStream(socket.getInputStream());
            dOut = new DataOutputStream(socket.getOutputStream());
        }catch(Exception e){
            e.printStackTrace();
        }
        while (true) {
            try {
                
                for(int i=0;i<numberOfIteration;i++){
                    int number = new Random().nextInt(100);
                    System.out.println("Client Send# "+number);
                    dOut.writeUTF(number+""); dOut.flush();
                }
                for(int i=0;i<numberOfIteration;i++)System.out.println(dIn.readUTF());
                /*int NoTResult = numberOfIteration;
                
                for (int i = 0; i < numberOfIteration; i++) {
                    int number = new Random().nextInt(100);
                    System.out.println("Sending number: " + number);
                    dOut.writeUTF("$$"+" "+number);
                    dOut.flush();
                    String str = dIn.readUTF();
                    System.out.println(str);
                    while(str.startsWith("D")){
                        NoTResult--;
                        int pos = str.indexOf(" ");
                        //System.out.println(pos);
                        int pos_x = Integer.parseInt(str.substring(1, pos));
                        System.out.println("Pos: "+pos_x+" "+str.substring(pos+1));
                        //System.out.println("Pos"+pos+" "+str.substring(pos+1));
                        str=dIn.readUTF();
                    }
                    System.out.println("$$> "+str+" from inputThread");
                    
                    Thread.sleep(200);
                }
                while(NoTResult>0){
                    NoTResult--;
                    System.out.println(dIn.readUTF());
                }*/
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("Press AnyKey to Start");
            new Scanner(System.in).nextInt();
        }
    }
}
