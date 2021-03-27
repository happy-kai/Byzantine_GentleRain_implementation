/*************************************************************************
	> File Name: PBFT.java
	> Author: Kaile Huang
	> Mail: MG1933024@smail.nju.edu.cn
	> Created Time: 2021年03月05日 星期五 14时45分04秒
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

public class PBFT extends Thread {
    private int phase;                    //which phase of PBFT-Algorithm that this server is going on
    private int f;                        //the maximum number of byzantine nodes
    private String request;               //the request which needs to be sent to each server
    private String[] servers;             //the address of all the servers
    private int replicaID;                //the ID of this server
    private int server_num;

    /******
     * initialization of a PBFT Algotithm
     **/
    public PBFT(int f, String[] servers, int replicaID){
        phase = 0;
        this.f = f;
        this.servers = servers;
        this.replicaID = replicaID;

    }

    public void run(){
        phase++;
        //phase one
        send_PREPARE(request);
        phase++;
        //phase two
        send_COMMIT(request);
        phase++;

        finish();
    }

    private void send_PREPARE(String msg){
        String prepare_msg = "PREPARE " + msg;
        try{
            for(int i = 0;i < server_num;i++){
                if(i!=replicaID){
                    String[] address = servers[i].split(" ");
                    Socket send_socket = new Socket(address[0], Integer.parseInt(address[1]));
                    OutputStream outToServer = send_socket.getOutputStream();
                    DataOutputStream out = new DataOutputStream(outToServer);
                    out.writeUTF(prepare_msg);
                    send_socket.close();
                }
            }
        } catch( IOException e ){
            e.printStackTrace();
        }
    }

    private void send_COMMIT(String msg){
        String commit_msg = "COMMIT" + msg;
        try{
            for(int i = 0;i < server_num;i++){
                String[] address = servers[i].split(" ");
                Socket send_commit = new Socket(address[0], Integer.parseInt(address[1]));
                OutputStream outToServer = send_commit.getOutputStream();
                DataOutputStream out = new DataOutputStream(outToServer);
                out.writeUTF(commit_msg);
                send_commit.close();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private void finish(){}
}
