/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClockSync;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author anando
 */
public class testC {
    void doIt() throws IOException{
        ServerSocket server = new ServerSocket(1235);
        Socket socket = server.accept();
        DataInputStream dIn = new DataInputStream(socket.getInputStream());
        System.out.println(dIn.readUTF());
    }
    public static void main(String[] args) throws IOException {
        new testC().doIt();
    }
}
