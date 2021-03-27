/*************************************************************************
	> File Name: PBFT_descriptor.java
	> Author: 
	> Mail: 
	> Created Time: 2021年03月26日 星期五 13时33分41秒
 ************************************************************************/

public class PBFT_descriptor{
    private int Byzantine_Num;
    private TimeStamp TS;
    private String operation;
    private object lock;


    private PBFT P_thread;

    public PBFT_descriptor(int Byz_num, TimeStamp T, String op){
        Byzantine_Num = Byz_num;
        T = TS;
        operation = op;
    }
}
