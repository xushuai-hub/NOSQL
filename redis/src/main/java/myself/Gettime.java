package myself;

import java.util.Date;
import java.text.SimpleDateFormat;

public class Gettime {
    public Gettime(){}

    public String getOldtime(String s){
        if(s==null || s.length()==0) return s;
        String[] s1=s.split("\\s+");
        if (s1.length<=1) return s1[0];
        return s1[1];
    }

    public String getNewtime(String s){
        if (s==null ||s.length()==0) return s;
        String[] s1 = s.split("\\s+");
        return s1[0];
    }

    public String update(String s){
        String str;
        Date date=new Date();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyyMMddHHmm");
        if(!s.equals("")){
            str=simpleDateFormat.format(date)+" "+ getNewtime(s);
        }
        else{
            str=simpleDateFormat.format(date);
        }
        return str;
    }


}
