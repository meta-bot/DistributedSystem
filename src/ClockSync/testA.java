/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClockSync;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author anando
 */
public class testA {
    void doIt() throws IOException, InterruptedException{
        Socket socket = new Socket("127.0.0.1", 1234);
        DataInputStream dIn = new DataInputStream(socket.getInputStream());
        System.out.println(dIn.readUTF());
        //System.out.println("Not Reached");
    }
    public static void main(String[] args) throws IOException, InterruptedException {
        new testA().doIt();
    }
}
