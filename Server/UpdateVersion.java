/*************************************************************************
	> File Name: UpdateVersion.java
	> Author: Kaile Huang
	> Mail: MG1933024@smail.nju.edu.cn
	> Created Time: 2020年11月04日 星期三 20时04分25秒
 ************************************************************************/

public class UpdateVersion{
    private TimeStamp t;
    private Object key;
    private Object value;

    public UpdateVersion(TimeStamp time, Object key, Object value){
        t = time;
        this.key = key;
        this.value = value;
    }

    public UpdateVersion(){
        t = new TimeStamp();
        key = new Object();
        value = new Object();
    }

    public TimeStamp get_ts(){
        return t;
    }

    public Object get_key(){
        return key;
    }

    public Object get_value(){
        return value;
    }
}
