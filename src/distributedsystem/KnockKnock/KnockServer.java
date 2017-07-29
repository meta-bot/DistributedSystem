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
        
        mInner = new ArrayList<>();
        str = "never"; mInner.add(str);
        str = "ever"; mInner.add(str);
        str = "fever"; mInner.add(str);
        str = "whatever"; mInner.add(str);
        jokes.add(mInner);
        
        mInner = new ArrayList<>();
        str = "huhu"; mInner.add(str);
        str = "haha"; mInner.add(str);
        str = "gugu"; mInner.add(str);
        str = "gaga"; mInner.add(str);
        jokes.add(mInner);
        
        
        mInner = new ArrayList<>();
        str = "knock-knock"; mInner.add(str);
        str = "who's there"; mInner.add(str);
        str = "hatch"; mInner.add(str);
        str = "hatch who?"; mInner.add(str);
        str = "god bless you."; mInner.add(str);
        jokes.add(mInner);
        
        mInner = new ArrayList<>();
        str = "knock-knock"; mInner.add(str);
        str = "who's there"; mInner.add(str);
        str = "creal"; mInner.add(str);
        str = "creal who?"; mInner.add(str);
        str = "creal pleasure to meet you."; mInner.add(str);
        jokes.add(mInner);
        
        
        mInner = new ArrayList<>();
        str = "knock-knock"; mInner.add(str);
        str = "who's there"; mInner.add(str);
        str = "harry"; mInner.add(str);
        str = "harry who?"; mInner.add(str);
        str = "harry up and answer the door."; mInner.add(str);
        jokes.add(mInner);
        
         mInner = new ArrayList<>();
        str = "knock-knock"; mInner.add(str);
        str = "who's there"; mInner.add(str);
        str = "keith"; mInner.add(str);
        str = "keith who?"; mInner.add(str);
        str = "keith me, my thweet preenth!"; mInner.add(str);
        jokes.add(mInner);
        
        
         mInner = new ArrayList<>();
        str = "knock-knock"; mInner.add(str);
        str = "who's there"; mInner.add(str);
        str = "atch"; mInner.add(str);
        str = "atch who?"; mInner.add(str);
        str = "I'm sorry i did not know you had a cold!"; mInner.add(str);
        jokes.add(mInner);
        
        
         mInner = new ArrayList<>();
        str = "knock-knock"; mInner.add(str);
        str = "who's there"; mInner.add(str);
        str = "Doris"; mInner.add(str);
        str = "doris, who?"; mInner.add(str);
        str = "Doris locked, that's why I had to knock!"; mInner.add(str);
        jokes.add(mInner);
        
        mInner = new ArrayList<>();
        str = "knock-knock"; mInner.add(str);
        str = "who's there"; mInner.add(str);
        str = "Clear"; mInner.add(str);
        str = "clear, who?"; mInner.add(str);
        str = "clear this hallway for delivery."; mInner.add(str);
        jokes.add(mInner);
        
        mInner = new ArrayList<>();
        str = "knock-knock"; mInner.add(str);
        str = "who's there"; mInner.add(str);
        str = "I love"; mInner.add(str);
        str = "I love who?"; mInner.add(str);
        str = "I don't know, you tell me!"; mInner.add(str);
        jokes.add(mInner);
        
        mInner = new ArrayList<>();
        str = "knock-knock!Who's There?"; mInner.add(str);
        str = "Buster!"; mInner.add(str);
        str = "Buster who?"; mInner.add(str);
        str = "Buster Cherry!"; mInner.add(str);
        str = "Is your daughter home?"; mInner.add(str);
        jokes.add(mInner);
        
        mInner = new ArrayList<>();
        str = "knock-knock"; mInner.add(str);
        str = "who's there"; mInner.add(str);
        str = "Doris"; mInner.add(str);
        str = "doris, who?"; mInner.add(str);
        str = "Doris locked, that's why I had to knock!"; mInner.add(str);
        jokes.add(mInner);
        
        mInner = new ArrayList<>();
        str = "knock-knock"; mInner.add(str);
        str = "who's there"; mInner.add(str);
        str = "Boo"; mInner.add(str);
        str = "Boo, who?"; mInner.add(str);
        str = "Stop crying you little baby!"; mInner.add(str);
        jokes.add(mInner);
        
        mInner = new ArrayList<>();
        str = "I know a grear knock knock joke."; mInner.add(str);
        str = "Ok, tell me."; mInner.add(str);
        str = "All right, You start"; mInner.add(str);
        str = "Oh, knock knock"; mInner.add(str);
        str = "who's there?"; mInner.add(str);
        jokes.add(mInner);
        
        mInner = new ArrayList<>();
        str = "knock-knock"; mInner.add(str);
        str = "who's there"; mInner.add(str);
        str = "Nobel"; mInner.add(str);
        str = "Nobel, who?"; mInner.add(str);
        str = "No bell so I'll knock!"; mInner.add(str);
        jokes.add(mInner);
        
        mInner = new ArrayList<>();
        str = "knock-knock"; mInner.add(str);
        str = "who's there"; mInner.add(str);
        str = "I scream"; mInner.add(str);
        str = "I scream, who?"; mInner.add(str);
        str = "I scream tastes cool on a hot day!"; mInner.add(str);
        jokes.add(mInner);
        
        mInner = new ArrayList<>();
        str = "knock-knock"; mInner.add(str);
        str = "who's there"; mInner.add(str);
        str = "value"; mInner.add(str);
        str = "Value, who?"; mInner.add(str);
        str = "Value be my Valentine?"; mInner.add(str);
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
