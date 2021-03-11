/*************************************************************************
	> File Name: connect_server.java
	> Author: Kaile Huang
	> Mail: MG1933024@smail.nju.edu.cn
	> Created Time: 2020年10月27日 星期二 15时58分11秒
 ************************************************************************/
import java.net.*;
import java.io.*;
import java.util.Scanner;
public class connect_server implements Runnable {
    private Thread t;
    private String serverName;
    private String msg;
    private int port;
   
    connect_server(String name, int port, String msg) {
        serverName = name;
        this.port = port;
        this.msg = msg;
      //System.out.println("Creating " +  threadName );
   }
   
    public void run() {
        try{
            Socket client = new Socket(serverName,port);
            OutputStream outToServer = client.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);
            
            out.writeUTF(msg + " | from " + client.getLocalSocketAddress());
            InputStream inFromServer = client.getInputStream();
            DataInputStream in = new DataInputStream(inFromServer);
            
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void start () {
      //System.out.println("Starting " +  threadName );
        if (t == null) {
            t = new Thread (this, serverName+":"+port);
            t.start ();
        }
    }
}
