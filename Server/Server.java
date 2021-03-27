/*************************************************************************
	> File Name: Server.java
	> Author: Kaile Huang 
	> Mail: MG1922024@smail.nju.edu.cn
	> Created Time: 2020年09月26日 星期六 22时47分40秒
 ************************************************************************/
import java.net.*;
import java.io.*;
import java.util.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.concurrent.*;

public class Server extends Thread {
    private ServerSocket serverSocket;
    private String[] servers;                       //Sockets used to send msg to other servers
    private int server_num;                         //the number of other servers
    private int config_time;                        //initial Timestamp
    private TimeStamp[] versionVector;              //Highest TimeStamp in each server
    private TimeStamp[] LSTVector;                  //LST in each server
    private TimeStamp GST;                          // global stable time of server
    private TimeStamp LST;                          // Local stable time of server
    private int replicaId;                          // replica Id
    //private Timestamp GST;                         // global stable time of server 
    //private Timestamp[] versionVector;             // version vector
    //private Map<String, List<Item>> versionChain;  // version chain
    
    //private int replicaId;              // replica id that this server belongs to
    //private int partitionId;            // partition id that this server belongs to
    //private int numReplicas;            // total number of replicas
    //private int numPartitions;          // total number of partitions per data center

    private Storage replica;            //data replica
    
    private static ExecutorService executor = Executors.newCachedThreadPool();


    /******
     * initialization of the Server
     **/

    public Server(int port) throws IOException{
        LST = new TimeStamp(0, "NULL");
        GST = new TimeStamp(0, "NULL");

        serverSocket = new ServerSocket(port);
        String conf = "config.txt";
        try{
            //Thread.sleep(config_time);
            File file = new File(conf);
            if (!file.exists()) 
                file.createNewFile();

            //创建BufferedReader读取文件内容
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = br.readLine();
            line = br.readLine();
            replicaId = Integer.parseInt(line);
            //int port = Integer.parseInt(line);

            line = br.readLine();
            server_num = Integer.parseInt(line);
            servers = new String[server_num];
            versionVector = new TimeStamp[server_num];
            LSTVector = new TimeStamp[server_num];
            for(int i = 0;i < server_num;i++){
                line = br.readLine();
                servers[i] = line;
                versionVector[I] = new TimeStamp(0,"NULL");
                LSTVector[i] = new TimeStamp(0,"NULL");
            }
            for(int i = 0;i < server_num;i++){
                System.out.println("Server: " + servers[i]);
            }
            br.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        replica = new Storage(0,0);
        serverSocket.setSoTimeout(100000);
    }






    /******
     * deal with the request received
     * Write_op
     * Read_op
     * deal_request
     **/

    //deal with write operations
    private String Write_op(String[] op, String client){
        long time = Long.parseLong(op[3]);
        TimeStamp t = new TimeStamp(time, client);
        if(versionVector[replicaId].Before(t)){
            versionVector[replicaId] = t;
            compute_LST();
        }
        replica.update(op[1],op[2],t);
        return "Ack!";
    }
    //deal with read operation
    private String Read_op(String[] op, String client){
        Object key = op[1];
        long time = Long.parseLong(op[2]);
        TimeStamp t = new TimeStamp(time, client);
        Object ans = replica.get_value( key, t);
        return (String) ans;
    }
    //Process requests
    private String deal_request(String request, String client){
        String[] operations = request.split(" ");
        //System.out.println(request);
        //System.out.println(client);
        //return "ACK!";
        if(operations[0].compareTo("w") == 0){
            return Write_op(operations,client);
        } else if(operations[0].compareTo("r") == 0){
            return Read_op(operations,client);
        } else{
            return "Wrong Request";
        }
    }



    
    /******
     * save_logs
     **/

    private void add_log(String request, String Ack, String remote_client){
        try{ 
            File file = new File("log.txt");
            if(!file.exists())
               file.createNewFile();

            FileWriter writer = new FileWriter(file, true);
            writer.write(remote_client + ":\t" + request + "\t| " + Ack+"\n");
            //System.out.println(remote_client+":"+request+"|"+Ack);
            writer.flush();
            writer.close();
       }catch(IOException e){
           e.printStackTrace();
       }
        return ;
    }



    /******
     * thread that waiting for msg from networks
     */

    public void run(){
        //while(true){
            //try{
            System.out.println("Server started, the port is:" + serverSocket.getLocalPort() + "...");
         while(true){  
            try{
            // Get request 
            Socket server = serverSocket.accept();
            DataInputStream in = new DataInputStream(server.getInputStream());
            String request = in.readUTF();
            
            //deal with the request
            String Ack = deal_request(request, server.getRemoteSocketAddress().toString());
            //String Ack = "ACK!";
            //System.out.println(request);

            //Send Ack
            DataOutputStream out = new DataOutputStream(server.getOutputStream());
            compute_GST();
            out.writeUTF( Ack+" "+Long.toString(GST.get_time().getTime())+" "+GST.get_client() );
            //server.close();

            //add log
            add_log(request, Ack, server.getRemoteSocketAddress().toString());
            server.close();
            }catch(SocketTimeoutException s){
                System.out.println("Socket timed out!");
                break;
            }catch(IOException e){
                e.printStackTrace();
                break;
            }
        }
    }

    /*****
     * next we will dealwith gst and LST
     */
    void compute_GST(){
        TimeStamp t = LSTVector[0];
        for(int i = 0; i < server_num;i++){
            if(LSTVector[i].Before(t)){
                t = LSTVector[i];
            }
        }
        GST = t;
    }

    void compute_LST(){
        TimeStamp t = versionVector[0];
        for(int i = 0; i < server_num;i++){
            if(versionVector[i].Before(t)){
                t = versionVector[i];
            }
        }
        LST = t;
        LSTVector[replicaId] = LST;
        compute_GST();
    }
    
    public static void main(String [] args){

        String conf = "config.txt";
        String line;
        try{
            File file = new File(conf);
            //如果文件不存在，创建文件
            if (!file.exists()) 
                file.createNewFile();
            //创建FileWriter对象
            //FileWriter writer = new FileWriter(file);
            //向文件中写入内容
            //writer.write("the first way to write and read");
            //writer.flush();
            //writer.close();

            //创建BufferedReader读取文件内容
            BufferedReader br = new BufferedReader(new FileReader(file));
            line = br.readLine();
            int port = Integer.parseInt(line);

            br.close();
            
            //replica = new Storage(0,0);
            Thread t = new Server(port);
            //t.run();
            executor.execute(t);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
