/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MultiCasting;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import jdk.nashorn.internal.ir.BreakNode;

/**
 *
 * @author anando
 */
public class SyncClass {

    private final int port;
    private final String ipaddress;
    private ArrayList<Socket> activeList;
    private ServerSocket server;
    private int account;

    public SyncClass(int port) throws IOException {
        this.port = port;
        this.account = 0;
        this.ipaddress = null;//ipaddress;
        activeList = new ArrayList<Socket>();
        server = new ServerSocket(this.port);
        new Thread(new _CheckActiveList()).start();
        new Thread(new _CheckMultiCast()).start();
        doTask();
    }

    private class _CheckMultiCast implements Runnable {

        @Override
        public void run() {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            while (true) {
                try{
                    for (Socket s : activeList) {
                    try {
                        DataInputStream dIn = new DataInputStream(s.getInputStream());
                        DataOutputStream dOut = new DataOutputStream(s.getOutputStream());

                        if (dIn.available() > 0) {
                            String message = dIn.readUTF();
                            if (message.startsWith("R")) {
                                //Response containse Time
                                String sendToEveryOne = message.substring(message.indexOf(" ") + 1);
                                System.out.println("Sending update time to everyone " + sendToEveryOne);
                                for (Socket t : activeList) {
                                    try {
                                        if (s != t) {
                                            DataOutputStream tempdOut = new DataOutputStream(t.getOutputStream());
                                            tempdOut.writeUTF("U " + sendToEveryOne);
                                            tempdOut.flush();
                                        }
                                    } catch (Exception e) {
                                        continue;
                                    }
                                }
                            } else if (message.startsWith("M")) {
                                //multicast contains adding integer to the account
                                String sendToEveryOne = message.substring(message.indexOf(" ") + 1);
                                System.out.println("Send Multicast Adding Integer " + sendToEveryOne);

                                for (Socket t : activeList) {
                                    try {
                                        if (s != t) {
                                            DataOutputStream tempdOut = new DataOutputStream(t.getOutputStream());
                                            tempdOut.writeUTF(message);
                                            tempdOut.flush();
                                        }
                                    } catch (Exception e) {
                                        continue;
                                    }
                                }
                                account += Integer.parseInt(sendToEveryOne);
                                System.out.println("Account Updated " + account);
                            }
                        }
                    } catch (Exception e) {
                        continue;
                    }
                }
                }catch(Exception e){
                    continue;
                }
            }
        }

    }

    private void doTask() throws IOException {

        while (true) {
            Socket socket = server.accept();
            addActive(socket);
        }
    }

    private void addActive(Socket obj) {
        synchronized (activeList) {
            activeList.add(obj);
            activeList.notifyAll();
        }

    }

    private void removeActive(Socket obj) {
        synchronized (activeList) {
            activeList.remove(obj);
            activeList.notifyAll();
        }
    }

    private class _CheckActiveList implements Runnable {

        @Override
        public void run() {
            while (true) {
                try {
                    int sz = activeList.size();
                    for (Socket s : activeList) {
                        try {
                            System.out.println("checking address " + s.getInetAddress() + " port " + s.getPort());
                            Thread.sleep(500);
                            DataOutputStream dOut = new DataOutputStream(s.getOutputStream());
                            dOut.writeUTF("T"); //Only for Testing
                        } catch (IOException ex) {
                            System.out.println(s.getInetAddress() + " " + s.getPort() + " is not active anymore");
                            removeActive(s);
                            continue;
                        } catch (InterruptedException ex) {
                            Logger.getLogger(ClockSync.SyncClass.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        //Logger.getLogger(ClockSync.SyncClass.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } catch (Exception e) {
                    continue;
                }
            }
        }

    }
    public static void main(String[] args) throws IOException {
        new SyncClass(1234);
    }

}
