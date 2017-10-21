/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClockSync;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author anando
 */
public class WorkerClass {

    private int port;
    private int syncNo;
    private ServerSocket server;
    private ArrayList<mSocket> activeList;
    private Stack<Socket> buffer;
    private boolean turnoffAccepting;

    private class LoadStack implements Runnable {

        @Override
        public void run() {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            while (true) {
                syncNo++;
                while (!buffer.isEmpty()) {
                    Socket socket = buffer.pop();
                    try {
                        DataInputStream dIn = new DataInputStream(socket.getInputStream());
                        String str = dIn.readUTF();

                        if (!str.startsWith("T")) {
                            int newSync = Integer.parseInt(str);
                            if (syncNo < newSync) {
                                syncNo = newSync;
                                System.out.println("Sync is updated. Now value is:: " + syncNo);
                            }
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(WorkerClass.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    Logger.getLogger(WorkerClass.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

    public WorkerClass(int port) throws IOException {
        this.port = port + 6845;
        turnoffAccepting = false;
        buffer = new Stack<Socket>();
        activeList = new ArrayList<>();
        server = new ServerSocket(this.port);
        new Thread(new LoadStack()).start();
        doTask();
    }

    private void getActiveList(Socket socket) throws IOException {
        DataInputStream dIn = new DataInputStream(socket.getInputStream());
        DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());
        dOut.writeUTF(port + "");
        dOut.flush();
        System.out.println("Active list: ");
        while (true) {
            try {
                System.out.println("Here");
                String dataFromSyc = dIn.readUTF();
                System.out.println("Got From Syc:: " + dataFromSyc);
                String[] datas = dataFromSyc.split(" ");
                activeList.add(new mSocket(datas[0], Integer.parseInt(datas[1])));
            } catch (EOFException e) {
                break;
            }
        }
    }

    private void sendData() throws IOException {
        for (mSocket m : activeList) {
            DataOutputStream dOut = new DataOutputStream(new Socket(m.getAddress(), m.getPort()).getOutputStream());
            System.out.println("Sending data to " + m.getPort());
            dOut.writeUTF(syncNo + "");
            dOut.flush();
        }
    }

    private void doTask() throws IOException {
        //first connect with sync
        Socket socket = new Socket("127.0.0.1", 1234);
        //generate a random number (syncNo)
        syncNo = new Random().nextInt(10000);
        System.out.println("SyncNo " + syncNo);
        // get active list from sync
        getActiveList(socket);
        //send Every activeList, the random number
        sendData();
        while (true) {
            Socket soc = server.accept();
            //System.out.println("new Connection establised");
            buffer.push(soc);
        }
    }

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter port Number of this worker");
        new WorkerClass(sc.nextInt());
    }
}
