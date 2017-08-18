/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package distributedsystem;

import ReplicatedServer.MasterClass;
import ReplicatedServer.ReplicatedClient;
import ReplicatedServer.SlaveClass;
import distributedsystem.KnockKnock.KnockClient;
import distributedsystem.KnockKnock.KnockServer;
import distributedsystem.KnockKnock.TestClient;
import distributedsystem.KnockKnock.TestServer;
import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author anando
 */
public class DistributedSystem {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        Scanner sc= new Scanner(System.in);
        int port = 3600;
        System.out.println("Enter 1 for client and 2 for slaveserver and 3 for masterserver");
        int flag = sc.nextInt();
        if(flag == 1){
            String ip = "127.0.0.1";
            new ReplicatedClient(10, port,ip);
            //new TestClient();
        }else if(flag==2){
            System.out.println("Enter Port Number# ");
            int add = sc.nextInt();
            new SlaveClass(port+add,10);
        }else{
            new MasterClass(port);
        }
    }
    
}
