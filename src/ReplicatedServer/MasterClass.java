/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ReplicatedServer;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author anando
 */
public class MasterClass {

    private ServerSocket server;
    private ArrayList<Socket> workers;
    private ArrayList<Socket> clients;
    private Queue<String> jobs;
    private ArrayList<Integer> loads;
    private int port;
    private ExecutorService threadPool;
    private int MAXIMUMBUFFERSIZE;

    public MasterClass(int port) throws IOException {
        this.port = port;
        System.out.println("MasterPort# "+this.port);
        workers = new ArrayList<Socket>();
        clients = new ArrayList<Socket>();
        loads = new ArrayList<Integer>();
        jobs = new LinkedList<String>();
        server = new ServerSocket(port);
        threadPool = Executors.newFixedThreadPool(100);
        doTask();
    }

    private void conf() throws IOException {
        if (workers.isEmpty()) {
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter Number of Slave Server(s):");
            int slaves = Integer.parseInt(sc.nextLine());//sc.nextInt();
            System.out.println("Do You wan't to set default IP to servers?(Y/N)");
            String flag = sc.nextLine();
            String IP = "127.0.0.1";
            if (flag.equals("Y")) {
                for (int i = 1; i <= slaves; i++) {
                    System.out.println("PortNow Creating# "+(port+i));
                    Socket temp = new Socket(IP, port + i);
                    int pos_X = slaveSize();
                    workers.add(temp);
                    loads.add(0);
                    new WorkerResponseHandler(temp, pos_X).start();
                }
            } else {
                for (int i = 0; i < slaves; i++) {
                    System.out.println("Enter IP of server# " + (i + 1));
                    IP = sc.nextLine();
                    Socket temp = new Socket(IP, port + i + 1);
                    int pos_X = slaveSize();
                    workers.add(temp);
                    loads.add(0);
                    new WorkerResponseHandler(temp, pos_X).start();
                }
            }
        } else {
            System.out.println("Want to add another Server?(Y/N)");
            Scanner sc = new Scanner(System.in);
            String flag = sc.nextLine();
            if (flag.equals("Y")) {
                String IP = sc.nextLine();
                int wport = slaveSize() + 1;
                Socket temp = new Socket(IP, wport);
                int pos_X = slaveSize();
                addSlave(temp);
                addLoads(0);
                new WorkerResponseHandler(temp, pos_X).start();
            }
        }
    }

    private void addLoads(int val) {
        synchronized (loads) {
            loads.add(0);
        }
    }

    private void updateLoads(int val, int index) {
        synchronized (loads) {
            loads.set(index, val);
        }
    }

    private int getLoads(int index) {
        synchronized (loads) {
            return loads.get(index);
        }
    }

    private void addSlave(Socket socket) {
        synchronized (workers) {
            workers.add(socket);
        }
    }

    private int slaveSize() {
        synchronized (workers) {
            return workers.size();
        }
    }

    private Socket getSlave(int index) {
        synchronized (workers) {
            return workers.get(index);
        }
    }

    private void jobSubmit(String str) {
        synchronized (jobs) {
            jobs.add(str);
        }
    }

    private String getJob() {
        synchronized (jobs) {
            return jobs.poll();
        }
    }

    private int jobSize() {
        synchronized (jobs) {
            return jobs.size();
        }
    }

    private int pushClient(Socket socket) {
        synchronized (clients) {
            int ret = clients.size();
            clients.add(socket);
            return ret;
        }
    }

    private int ClientSize() {
        synchronized (clients) {
            return clients.size();
        }
    }

    private Socket getClient(int index) {
        synchronized (clients) {
            return clients.get(index);
        }
    }

    private void doTask() throws IOException {
        conf();
        //client request accept thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                while (true) {
                    System.out.println("Waiting for Client");
                    try {
                        Socket socket = server.accept();
                        int index = pushClient(socket);
                        System.out.println("Client# " + index + " is connected");
                        threadPool.execute(new ClientRequestHandler(index, socket));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        //select and send to worker thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                while (true) {
                    while (jobSize() > 0) {
                        String job = getJob();
                        int minVal = Integer.MAX_VALUE, minPos = 0;
                        int nOs = slaveSize();
                        for (int i = 0; i < nOs; i++) {
                            int loadM = getLoads(i);
                            if (loadM < minVal) {
                                minPos = i;
                                minVal = loadM;
                            }
                        }
                        Socket workerSocket = getSlave(minPos);
                        try {
                            DataOutputStream dOut = new DataOutputStream(workerSocket.getOutputStream());
                            dOut.writeUTF(job);
                            dOut.flush();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
        //receive any kind of reply from workers
        while (true) {
            conf();
        }
    }

    private class WorkerResponseHandler extends Thread {

        private Socket socket;
        private int index;
        private DataInputStream dIn;

        public WorkerResponseHandler(Socket socket,int index) {
            this.socket = socket;
            this.index = index;
            try{
                dIn = new DataInputStream(this.socket.getInputStream());
        
            }catch(Exception e){
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
                        if(str.startsWith("D")){
                            String[] strs = str.split(" ");
                            updateLoads(Integer.parseInt(strs[1]), this.index);
                            int client_pos = Integer.parseInt(strs[2]);
                            Socket ClientSocket = getClient(client_pos);
                            DataOutputStream dOut = new DataOutputStream(ClientSocket.getOutputStream());
                            dOut.writeUTF(strs[3]+" "+strs[4]);dOut.flush();
                        }else{
                            updateLoads(Integer.parseInt(str), this.index);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private class ClientRequestHandler implements Runnable {

        private int id;
        private Socket socket;
        private DataInputStream dIn;

        public ClientRequestHandler(int id, Socket socket) {
            this.id = id;
            this.socket = socket;
            try {
                dIn = new DataInputStream(this.socket.getInputStream());
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
                        String dataFromClient = dIn.readUTF();
                        if (jobSize() < MAXIMUMBUFFERSIZE) {
                            jobSubmit(this.id + " " + dataFromClient);
                        } else {
                            System.out.println("Client# " + this.id + "'s request is dropped due to stackoverflow(Sorry)");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
