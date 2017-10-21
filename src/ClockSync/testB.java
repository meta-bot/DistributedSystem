/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClockSync;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author anando
 */
public class testB {
    void doIt() throws IOException, ClassNotFoundException, InterruptedException{
        ServerSocket server = new ServerSocket(1234);
        Socket socket = server.accept();
        System.out.println(socket.getInetAddress()+" "+socket.getPort());
        DataOutputStream dOut = new DataOutputStream(new Socket(socket.getInetAddress(), socket.getPort()).getOutputStream());
        dOut.writeUTF("hi");
        //Thread.sleep(5000);
    }

    /**
     *
     * @param args
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        new testB().doIt();
    }
}
