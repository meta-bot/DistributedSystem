/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClockSync;

import java.net.InetAddress;

/**
 *
 * @author anando
 */
public class mSocket {
    private String address;
    private int port;
    
    public mSocket(String address , int port){
        this.address = address;
        this.port = port;
    }
    public String getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }
    
}
