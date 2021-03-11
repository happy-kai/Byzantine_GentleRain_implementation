/*************************************************************************
	> File Name: GreetingClient.java
	> Author: Kaile Huang
	> Mail: MG1933024@smail.nju.edu.cn
	> Created Time: 2020年09月23日 星期三 14时45分30秒
 ************************************************************************/
import java.net.*;
import java.io.*;
public class GreetingClient{
    public static void main(String[] args){
        String serverName = args[0];
        int port = Integer.parseInt(args[1]);
        try{
            System.out.println("连接到主机：  " + serverName + ", 端口号: " + port);
            Socket client = new Socket(serverName, port);
            System.out.println("远程主机地址： "+ client.getRemoteSocketAddress());
            OutputStream outToServer = client.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);

            out.writeUTF("Hello from" + client.getLocalSocketAddress());
            InputStream inFromServer = client.getInputStream();
            DataInputStream in = new DataInputStream(inFromServer);
            System.out.println("服务器响应：" + in.readUTF());
            client.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

}
