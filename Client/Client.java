/*************************************************************************
	> File Name: GreetingClient.java
	> Author: Kaile Huang
	> Mail: MG1933024@smail.nju.edu.cn
	> Created Time: 2020年09月23日 星期三 14时45分30秒
 ************************************************************************/
import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
//import java.io.inputStreamReader;
public class Client extends Thread{
    //private Socket clientSocket;
    //private int port;
    //private String serverName;
    private TimeStamp gst;
    private String[] servers;
    private int server_num;

    /*
     * public Client(String host, int port) throws IOException {
        //clientSocket = new Socket(host, port);
        //clientSocket.setSoTimeout(1000000);
        this.port = port;
        this.serverName = host;
        //long t = 1;
        gst = new TimeStamp();
    }
    */

    public Client() throws IOException {
        
            File file = new File("server_config.txt");
            if(!file.exists())
                file.createNewFile();

            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = br.readLine();
            server_num = Integer.parseInt(line);
            servers = new String[server_num];
            for(int i = 0;i <server_num;i++){
                line = br.readLine();
                servers[i] = line;
            }

            gst = new TimeStamp((long)0,"NULL");

            br.close();
    }

    private void save_log(String msg, String Ack){
        try{
            File file = new File("log.txt");
            if(!file.exists())
                file.createNewFile();

            FileWriter writer = new FileWriter(file,true);
            writer.write("operation: \" " + msg+" \" | Ack: \" "+Ack+" \" \n");
            writer.flush();
            writer.close();
        }catch(IOException e){
            e.printStackTrace();
        }
        return;
    }

    private void sendMsg(String msg){
        for(int i = 0;i < server_num;i++){
            String[] str = servers[i].split(" ");
            String serverName = str[1];
            int port = Integer.parseInt(str[2]);
            sendMsg(msg, serverName, port);
        }
    }

    private void sendMsg(String msg, String serverName, int port){
        try{
            
            Socket ClientSocket = new Socket(serverName,port);
            System.out.println("Server: " + ClientSocket.getRemoteSocketAddress());
            OutputStream outToServer = ClientSocket.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);
            out.writeUTF(msg + " | from " + ClientSocket.getLocalSocketAddress());
            InputStream inFromServer = ClientSocket.getInputStream();
            DataInputStream in = new DataInputStream(inFromServer);
            //System.out.println("Reply from server : " + in.readUTF());
            String Ack = in.readUTF();
            String []str = Ack.split(" ");
            gst.set_TimeStamp(Long.parseLong(str[1]), str[2]);
            System.out.println("Reply from server : " + Ack);
            save_log(msg, Ack);
            ClientSocket.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void run(){
        while(true){
            //try{
                System.out.println("Input your operation:");
                Scanner sc = new Scanner(System.in);
                String input = sc.nextLine();
                //System.out.println("get: " + input);
                String[] op = input.split("\\ ");
                //System.out.println(op[0]+"|"+op[1]+"|"+op[2]);
                if(op[0].compareTo("w") == 0){
                    //write operation;
                    System.out.println("Write " + op[1] + ":=" + op[2] + " to the system." );
                    Date now = new Date();
                    sendMsg(input+" "+Long.toString(now.getTime()));
                    //gst = new TimeStamp(now, "zlocal");
                    //System.out.println(input+" "+new Date().getTime());
                }else if(op[0].compareTo("r") == 0){
                    //read operation;
                    System.out.println("Read " + op[1] + " from the system."+gst.get_time()); 
                    sendMsg(input+" "+Long.toString(gst.get_time().getTime()));
                }
                else if(op[0].compareTo("f") == 0){
                    //finish
                    System.out.println("Goodbye!");
                    break;
                }
            //}catch(IOException e){
            //    e.printStackTrace();
            //}
            
        }
    }

    public static void main(String[] args){
        //String serverName = args[0];
        //int port = Integer.parseInt(args[1]);
        try{
            Thread t = new Client();
            t.run();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

}
