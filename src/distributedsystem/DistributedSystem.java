/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package distributedsystem;

import distributedsystem.KnockKnock.KnockClient;
import distributedsystem.KnockKnock.KnockServer;
import distributedsystem.KnockKnock.TestClient;
import distributedsystem.KnockKnock.TestServer;
import java.util.Scanner;

/**
 *
 * @author anando
 */
public class DistributedSystem {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Scanner sc= new Scanner(System.in);
        int port = 3600;
        System.out.println("Enter 1 for client and 2 for server");
        if(sc.nextInt() == 1){
            String ip = "127.0.0.1";
            new KnockClient(ip, port);
            //new TestClient();
        }else{
            new KnockServer(port);
            //new TestServer();
        }
    }
    
}
