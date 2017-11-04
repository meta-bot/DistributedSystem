/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MultiCasting;

import java.io.DataInput;
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
public class WorkerClass {
    private int port;
    private String address;
    private Socket socket;
    private int account;
    private int timer;

    public WorkerClass(int port , String address) throws IOException, InterruptedException {
        this.port = 1234;
        this.address = address;
        socket = new Socket(address,port);
        account = 0;
        new Thread(new _MessageManager()).start();
        doTask();
    }
    private class _MessageManager implements Runnable{

        @Override
        public void run() {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            while(true){
                try{
                    DataInputStream dIn = new DataInputStream(socket.getInputStream());
                    DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());
                    
                    if(dIn.available()>0){
                        String message = dIn.readUTF();
                        if(message.startsWith("U")){
                            String update = message.substring(message.indexOf(" ")+1);
                            int nwNumber = Integer.parseInt(update);
                            if(nwNumber > timer){
                                System.out.println("Timer updated from "+timer+" to "+nwNumber+". Account "+account);
                                timer = nwNumber;
                            }
                        }else if(message.startsWith("M")){
                            String update = message.substring(message.indexOf(" ")+1);
                            int nwNumber = Integer.parseInt(update);
                            System.out.println("Account update from "+account+" to "+(account+nwNumber));
                            account+=nwNumber;
                            dOut.writeUTF("R "+timer); dOut.flush();
                        }
                    }
                }
                catch(Exception e){
                    continue;
                }
            }
        }
        
    }
    private void doTask() throws InterruptedException, IOException{
        Thread.sleep(5000);
        timer = new Random().nextInt(10000);
        System.out.println("TIMER "+timer);
        while(true){
            if(new Random().nextInt(100)>95){
                int temp = new Random().nextInt(100);
                DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());
                dOut.writeUTF("M "+temp);
                dOut.flush();
                account+=timer;
                Thread.sleep(new Random().nextInt(500));
            }
        }
    }
    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Enter worker number");
        new WorkerClass(1234, "127.0.0.1");
    }
}
