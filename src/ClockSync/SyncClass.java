/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClockSync;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author anando
 */
public class SyncClass {

    private int port; //server port
    private ArrayList<mSocket> activeList;
    private ServerSocket server;
    private long timeIntervaleForActiveList;

    public SyncClass() throws IOException {
        port = 1234;
        activeList = new ArrayList<mSocket>();
        server = new ServerSocket(port);
        timeIntervaleForActiveList = 100;
        new Thread(new _CheckActiveList()).start();
        doTask();
    }

    private void sendActiveList(Socket socket) throws IOException {
        DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());
        DataInputStream dIn = new DataInputStream(socket.getInputStream());
        int port = Integer.parseInt(dIn.readUTF());
        System.out.println("Port Number# " + port);
        for (mSocket s : activeList) {
            dOut.writeUTF(s.getAddress() + " " + s.getPort());
            System.out.println("Sending list");
            dOut.flush();
        }
        dOut.close();
        dIn.close();
        activeList.add(new mSocket(socket.getInetAddress().toString().substring(1), port));
    }

    private void doTask() throws IOException {
        while (true) {
            Socket socket = server.accept();
            System.out.println(socket.getInetAddress() + " " + socket.getPort() + " is Connected to Sync");
            sendActiveList(socket);
            //activeList.add(new mSocket(socket.getInetAddress(),socket.getPort()));
        }
    }

    private class _CheckActiveList implements Runnable {

        @Override
        public void run() {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            while (true) {
                try {
                    int sz = activeList.size();
                    for (mSocket s : activeList) {
                        try {
                            System.out.println("address " + s.getAddress() + " port " + s.getPort());
                            Thread.sleep(500);
                            Socket _socket = new Socket(s.getAddress(), s.getPort());
                            DataOutputStream dOut = new DataOutputStream(_socket.getOutputStream());
                            dOut.writeUTF("T"); //Only for Testing
                        } catch (IOException ex) {
                            //Logger.getLogger(SyncClass.class.getName()).log(Level.SEVERE, null, ex);
                            //Socket Not exist
                            System.out.println(s.getAddress() + " " + s.getPort() + " is not active anymore");
                            //activeList.add(i, null);
                            activeList.remove(s);
                            continue;
                        } catch (InterruptedException ex) {
                            Logger.getLogger(SyncClass.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    try {
                        Thread.sleep(timeIntervaleForActiveList);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(SyncClass.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } catch (Exception e) {
                    continue;
                }
            }
        }

    }

    public static void main(String[] args) throws IOException {
        new SyncClass();
    }
}
