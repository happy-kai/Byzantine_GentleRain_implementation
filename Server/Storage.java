/*************************************************************************
	> File Name: Storage.java
	> Author: Kaile Huang
	> Mail: MG1933024@smail.nju.edu.cn
	> Created Time: 2020年10月13日 星期二 08时27分09秒
 ************************************************************************/
import java.util.*;

public class Storage{
    private Map<Object,ArrayList<UpdateVersion>> replica;
    private int replicaId;
    private int partitionId;

    public Storage(int replicaId, int partitionId){
        replica = new HashMap<Object,ArrayList<UpdateVersion>>();
        this.replicaId = replicaId;
        this.partitionId = partitionId;
    }

    public int get_replicaId(){
        return replicaId;
    }

    public int get_partitionId(){
        return partitionId;
    }

    public void update(Object key, Object value, TimeStamp time){
        if(replica.containsKey(key)){
            ArrayList<UpdateVersion> updates = replica.get(key);
            if(!updates.contains(new UpdateVersion(time,key,value))){
                UpdateVersion new_up = new UpdateVersion(time, key, value);
                
                int i = 0;
                for(;i < updates.size();){
                    if(updates.get(i).get_ts().Before(new_up.get_ts())){
                        i++;
                    }
                }
                
                updates.add(new_up);
                for(int k = updates.size()-1;k >i;k--){
                    updates.set(k,updates.get(k-1));
                }
                updates.set(i,new_up);
            }
            replica.remove(key);
            replica.put(key,updates);
        }
        else{
            UpdateVersion up = new UpdateVersion(time, key, value);
            ArrayList<UpdateVersion> updates = new ArrayList<UpdateVersion>();
            updates.add(up);
            replica.put(key,updates);
        }
            //replica.put(key,value);
        return;
    }

    public Object get_value(Object key, TimeStamp ts){
        if(replica.containsKey(key)){
            ArrayList<UpdateVersion> updates = replica.get(key);
            int index = 0;
            for(;index < updates.size();){
                if(updates.get(index).get_ts().Before(ts))
                    index++;
                else
                    break;
            }
            if(index == 0)
                return "NULL";
            else
                return updates.get(index-1).get_value();
            //return replica.get(key);
        }
        else
            return "NULL";
    }
}
