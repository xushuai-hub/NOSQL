package myself;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class Jsonfile {
    private String path;
    private String jsonstr;
    private JSONObject jsonObject;

    public Jsonfile(String path) throws IOException {
        File jsonFile = new File(path);
        FileReader fileReader = new FileReader(jsonFile);
        Reader reader = new InputStreamReader(new FileInputStream(jsonFile),"utf-8");
        int i = 0;
        StringBuffer buffer = new StringBuffer();
        while((i = reader.read()) != -1) {
            buffer.append((char) i);
        }
        fileReader.close();
        reader.close();
        this.jsonstr = buffer.toString();
        this.jsonObject = JSON.parseObject(this.jsonstr);
        this.path=path;
    }



    public JSONObject getJsonObject(){
        return this.jsonObject;
    }

    public List<User> getUser() {
        ArrayList<User> Users = new ArrayList<User>();
        Iterator iterator = this.jsonObject.keySet().iterator();
        while(iterator.hasNext()) {
            User user1 = new User();
            String key = (String) iterator.next();
            JSONObject helper = this.jsonObject.getJSONObject(key);
            user1.setUser_id(key);
            user1.setCounter(helper.getIntValue("Num"));
            user1.setValues(helper.getString("description"));
            user1.setAction(helper.getString("Action"));
            user1.setTime("");

            Users.add(user1);
        }

        return Users;
    }
}
