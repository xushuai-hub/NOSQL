package myself;

import com.JedisInstance;
import redis.clients.jedis.Jedis;

import com.alibaba.fastjson.JSONObject;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


public class Redis {
    private String Log_path;
    public Redis() {
        this.Log_path = Jsonfile.class.getClassLoader().getResource("log.json").getPath();
    }

    public  Jedis connection() throws Exception{
        Jedis jedis = JedisInstance.getInstance().getResource();
        return jedis;
    }

    public void Loaddata() throws Exception {
        Jedis jedis = connection();
        jedis.flushDB();

        System.out.println("加载数据...");

        Jsonfile reader = new Jsonfile(this.Log_path);
        JSONObject jobj = reader.getJsonObject();

        Iterator iterator = jobj.keySet().iterator();
        while(iterator.hasNext()) {
            String key = (String)iterator.next();
            JSONObject helper_obj = jobj.getJSONObject(key);
            increase("number",helper_obj.getIntValue("number"));
            System.out.println("正在载入: "+key+" 次数: "+ helper_obj.getIntValue("Num") + "总点击量:"+ getInt());

            setLIST("List",key);
            setSET("SET",key);
            setZSET("ZSET",key);

            //setHash(key,"selfNum",String.valueOf(helper_obj.getIntValue("Num")));
            jedis.hincrBy(key,"selfNum",helper_obj.getIntValue("Num"));
            setHash(key,"Action",helper_obj.getString("Action"));
            setHash(key,"time",helper_obj.getString("time"));
            setHash(key,"description",helper_obj.getString("description"));
        }
        System.out.println("载入成功");
    }

    public void save() throws Exception {
        System.out.println("保存记录");
        JSONObject jsonObject = new JSONObject();
        Iterator iterator = getSET("SET").iterator();
        while(iterator.hasNext()) {
            String key = (String)iterator.next();
            JSONObject helper = new JSONObject();
            helper.put("Num",Integer.valueOf(getHashStr(key,"selfNum")));
            helper.put("description",getHashStr(key,"description"));
            helper.put("Action",getHashStr(key,"Action"));
            helper.put("time",getHashStr(key,"time"));

            jsonObject.put(key,helper);
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter(this.Log_path));

        writer.write(jsonObject.toJSONString());
        writer.flush();
        writer.close();

        Jedis jedis = JedisInstance.getInstance().getResource();
        jedis.flushDB();
        System.out.println("保存成功");
    }

    public void action(User stu) throws Exception {
        Jedis jedis = JedisInstance.getInstance().getResource();

        int counter=stu.getCounter();
        String ID=stu.getUser_id();
        String values=stu.getValues();
        String action=stu.getAction();
        String time=stu.getTime();

        Gettime D=new Gettime();
        time=D.update(time);

        System.out.println("ID: " + ID + " 操作: " + action + " 点击数: "+counter + " 上次操作周期: " + D.getOldtime(time));

        increase("NUM",counter);
        setLIST("LIST",ID);
        setSET("SET",ID);
        setZSET("ZSET",ID);

        setHash(ID,"description",values);
        setHash(ID,"Action",action);
        setHash(ID,"time",time);
        //setHash(ID,"selfNum",String.valueOf(counter));
        jedis.hincrBy(ID,"selfNum",counter);
    }

    //NUM的增加
    public void increase(String num,int count) {
        Jedis jedis = JedisInstance.getInstance().getResource();
        for(int i = 0;i<count;i++) {
            jedis.incr(num);
        }
    }

    public void setkey(String key,String values) throws Exception {
        Jedis jedis = connection();
        if(jedis.exists(key))return;

        jedis.set(key,values);
    }
    public void setZSET(String zset,String s) throws Exception {
        Jedis jedis = connection();
        jedis.zadd(zset,1,s);
    }

    public void setLIST(String list,String s) throws Exception {
        Jedis jedis = connection();
        jedis.lpush(list,s);
    }

    public void setHash(String hash,String key,String value) throws Exception {
        Jedis jedis = connection();
        jedis.hset(hash,key,value);
    }

    public void setSET(String set,String s) throws Exception {
        Jedis jedis = connection();
        jedis.sadd(set,s);
    }


    //get方法
    public String getInt() throws Exception {
        Jedis jedis = connection();
        return jedis.get("NUM");
    }
    public String getSTR(String key) throws Exception {
        Jedis jedis = connection();
        if(!jedis.exists(key))return  "key:"+key+"不存在";
        return jedis.get(key);
    }
    public List<String> getLIST(String l, int range) throws Exception {
        Jedis jedis = connection();
        if(!jedis.exists(l))return null;

        List<String> list = jedis.lrange(l,0,range);
        return list;
    }

    public Set<String> getSET(String s) throws Exception {
        Jedis jedis = connection();
        if(!jedis.exists(s))return null;

        Set<String> set = jedis.smembers(s);
        return set;
    }

    public Set<String>getZSET(String z,int range) throws Exception {
        Jedis jedis = connection();
        if(!jedis.exists(z))return null;

        Set<String> zset = jedis.zrange(z,0,range);
        return zset;
    }

    public String getHashStr(String hash,String key) throws Exception {
        Jedis jedis = connection();
        if(!jedis.exists(hash) || !jedis.hexists(hash,key))return hash+"不存在或者"+key+"不存在";

        return jedis.hget(hash,key);
    }

    public String getFreq(String key) throws Exception {
        Jedis jedis = connection();
        if(!jedis.exists(key))return "未能找到"+key+"登录记录";

        Gettime da = new Gettime();
        return key + "上次登录时间:" + da.getNewtime(jedis.hget(key,"time")) ;
    }
}

