/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package distributedsystem.KnockKnock;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLServerSocket;

/**
 *
 * @author anando
 */
class WorkingClass implements Runnable{

    private Socket socket;
    private DataInputStream dIn;
    private DataOutputStream dOut;
    ArrayList< ArrayList<String> > jokes;
    private int numberOfJokes;
    private int jokeLeft;

    public WorkingClass(Socket socket, ArrayList< ArrayList<String> > jokes) {
        try {
            this.socket = socket;
            dIn = new DataInputStream(socket.getInputStream());
            dOut= new DataOutputStream(socket.getOutputStream());
            this.jokes = jokes;
            numberOfJokes = jokes.size();
        } catch (IOException ex) {
            Logger.getLogger(WorkingClass.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
    @Override
    public void run() {
        try {
            System.out.println("Client #"+dIn.readUTF()+" is connected");
            jokeLeft = numberOfJokes;
            boolean []arr = new boolean[numberOfJokes+2];
            Arrays.fill(arr, false);
            while(jokeLeft>0){
                int index = new Random().nextInt(numberOfJokes);
                if(arr[index])continue;
                arr[index] = true;
                jokeLeft--;
                for(int i=0;i<jokes.get(index).size();){
                    if((i&1) > 0){
                        while(dIn.available() <=0)continue;
                        String dataFromClient = dIn.readUTF();
                        if(dataFromClient.equalsIgnoreCase(jokes.get(index).get(i))==false){
                            String dataToClient = "You are supposed to say,\""+jokes.get(index).get(i)+"\". Let's try again.";
                            dOut.writeUTF(dataToClient);
                            dOut.flush();
                            i = 0;
                            continue;
                        }
                        i++;
                    }else{
                        dOut.writeUTF(jokes.get(index).get(i)); dOut.flush();
                        i++;
                    }
                }
                if(jokeLeft>0){
                    dOut.writeUTF("Would you like to listen to another?(Y/N)"); dOut.flush();
                    while(dIn.available()<=0)continue;
                    if(dIn.readUTF().equalsIgnoreCase("N")){
                        closeAll();
                        return;
                    }
                }
            }
            
            dOut.writeUTF("I have no more jokes to tell"); dOut.flush();
            closeAll();
            return;
        } catch (IOException ex) {
            Logger.getLogger(WorkingClass.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void closeAll(){
        try {
            socket.close();
            dIn.close();
            dOut.close();
        } catch (IOException ex) {
            Logger.getLogger(WorkingClass.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
public class KnockServer {
    private int port;
    private ServerSocket server;
    private  ExecutorService threadPool;
    public ArrayList< ArrayList<String> > jokes;
    public KnockServer(int port) {
        this.port = port;
        threadPool =  Executors.newFixedThreadPool(100);
        constructJokes();
        connectServer();
        doTask();
    }
    private void constructJokes(){
        jokes = new ArrayList<>();
        ArrayList<String> mInner = new ArrayList<>();
        String str = "hi"; mInner.add(str);
        str = "hello"; mInner.add(str);
        str = "bye"; mInner.add(str);
        str = "Bello"; mInner.add(str);
        jokes.add(mInner);
        
        mInner = new ArrayList<>();
        str = "huhu"; mInner.add(str);
        str = "haha"; mInner.add(str);
        str = "gugu"; mInner.add(str);
        str = "gaga"; mInner.add(str);
        jokes.add(mInner);
    }
    private void doTask(){
        while(true){
            try {
                Socket socket = server.accept();
                threadPool.execute(new WorkingClass(socket,jokes));
            } catch (IOException ex) {
                Logger.getLogger(KnockServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void connectServer(){
        try {
            server = new ServerSocket(port);
        } catch (IOException ex) {
            Logger.getLogger(KnockServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
