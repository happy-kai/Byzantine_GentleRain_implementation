/*************************************************************************
	> File Name: TimeStamp.java
	> Author: Kaile Huang
	> Mail: MG1933024@smail.nju.edu.cn
	> Created Time: 2020年11月03日 星期二 19时20分35秒
 ************************************************************************/
import java.util.Date;
public class TimeStamp{
    private Date t;
    private String client_name;

    public TimeStamp(Date time, String name){
        t = time;
        client_name = name;
    }

    public TimeStamp(long time, String name){
        t = new Date();
        t.setTime(time);
        client_name = name;
    }

    public TimeStamp(){
        t = new Date();
    }

    public void set_TimeStamp(Date time, String name){
        t = time;
        client_name = name;
    }

    public void set_TimeStamp(long time, String name){
        t.setTime(time);
        client_name = name;
    }

    public Date get_time(){
        return t;
    }

    public String get_client(){
        return client_name;
    }
    
    public boolean Before(TimeStamp o){
        if(t.equals(o.get_time())){
            return client_name.compareTo(o.get_client()) < 0;
        }
        return t.before(o.get_time());
    }
}
