/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ReplicatedServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Pair;
import javax.net.ssl.SSLServerSocket;

/**
 *
 * @author anando
 */
public class SlaveClass {

    private int port;
    private int workingQueueSize;
    private ServerSocket server;
    private ThreadPoolExecutor threadPool;
    private Queue<Pair<String,Integer> > q;
    private Queue<String> res;

    public void qpushIn(Pair<String,Integer> val) {
        q.add(val);
    }

    public boolean qisEmpty() {
        //int ret;
        synchronized(q){
            return q.isEmpty();
        }
    }
    public int qSize(){
        synchronized(q){
            return q.size();
        }
    }

    public Pair<String,Integer> qpopOut() {
        synchronized(q){
            return q.poll();
        }
    }

    public void respushIn(String val) {
        res.add(val);
        
    }

    public boolean resisEmpty() {
        synchronized(res){
            return res.isEmpty();
        }
    }

    public String respopOut() {
        synchronized(res){
            return res.poll();
        }
        //return res.poll();
    }

    public SlaveClass(int port, int qSize) {
        this.port = port;
        this.workingQueueSize = qSize;
        System.out.println("Port# "+port);
        q = new LinkedList< Pair<String,Integer> >();
        res = new LinkedList<String>();
        try {
            this.server = new ServerSocket(this.port);
            doTask();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doTask() {
        try {
            System.out.println("Waiting for connection::");
            Socket socket = server.accept();
            Thread a = new Thread(new Input(this, socket));a.start();
            Thread b = new Thread(new Output(this, socket));b.start();
            Thread c = new Thread(new Working(this, socket));c.start();
            System.out.println("Connection Establised");
            a.join();
            b.join();
            c.join();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class Input implements Runnable {

        private SlaveClass slave;
        private Socket socket;
        private DataInputStream dIn;
        private DataOutputStream dOut;

        public Input(SlaveClass slave, Socket socket) {
            this.slave = slave;
            this.socket = socket;
            try {
                dIn = new DataInputStream(socket.getInputStream());
                dOut = new DataOutputStream(socket.getOutputStream());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            while (true) {
                try {
                    if (dIn.available() > 0) {
                        String str = dIn.readUTF();
                        System.out.println("Got Input## "+str+" Current BufferSize "+slave.qSize());
                        String[] strs = str.split(" ");
                        slave.qpushIn(new Pair<String,Integer>(strs[0],Integer.parseInt(strs[1])));
                        dOut.writeUTF(slave.qSize()+"");
                        dOut.flush();
                    }
                } catch (IOException ex) {
                    Logger.getLogger(SlaveClass.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

    private class Output implements Runnable {

        private SlaveClass slave;
        private Socket socket;
        private DataInputStream dIn;
        private DataOutputStream dOut;

        public Output(SlaveClass slave, Socket socket) {
            this.slave = slave;
            this.socket = socket;
            try {
                dIn = new DataInputStream(socket.getInputStream());
                dOut = new DataOutputStream(socket.getOutputStream());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            while (true) {
                if (!slave.resisEmpty()) {
                    try {
                        System.out.println("Sending Result "+slave.res.peek());
                        dOut.writeUTF("D"+" "+slave.qSize()+" "+slave.respopOut());
                        dOut.flush();
                        System.out.println("Gonna Sleep!! BYE BYE");
                           Thread.sleep(1000);
                        System.out.println("I am awake");
                    } catch (Exception ex) {
                        Logger.getLogger(SlaveClass.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }

    }

    private class Working implements Runnable {

        private SlaveClass slave;
        private Socket socket;

        public Working(SlaveClass slave, Socket socket) {
            this.slave = slave;
            this.socket = socket;
        }

        @Override
        public void run() {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            while (true) {
                if (!slave.qisEmpty()) {
                    Pair<String,Integer> number = slave.qpopOut();
                    System.out.println("Processing "+number.getValue()+" Current BufferSize "+slave.qSize());
                    slave.respushIn(number.getKey()+" "+number.getValue() + " " + String.valueOf(number.getValue() * number.getValue()));
                    try{Thread.sleep(500);}catch(Exception e){e.printStackTrace();}
                }
            }
        }

    }

}
