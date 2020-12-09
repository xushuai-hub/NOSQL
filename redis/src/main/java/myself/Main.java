package myself;

import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String args[]) throws Exception {
        //获取json文件
        Jsonfile reader = new Jsonfile(Jsonfile.class.getClassLoader().getResource("test.json").getPath());
        Redis redis = new Redis();

        redis.Loaddata();

        //对读取json文件内容操作,对json内每个用户信息转为user实体，并返回文件中的所有用户的数据
        List<User> Users = reader.getUser();

        //一个action包括Num的增加，list、set、zset的增加
        for (User s: Users){
            redis.action(s);
        }

        //查找用户登录周期
        System.out.println(redis.getFreq("xs"));
        System.out.println(redis.getFreq("user"));

        //incr测试
        System.out.println("当前点击数:" + redis.getInt());
        redis.increase("number",10);
        System.out.println("当前点击数:" + redis.getInt());

        //STR测试
        redis.setkey("test","test-STR");
        System.out.println(redis.getSTR("test"));

        //获得LIST SET ZSET
        System.out.println(redis.getLIST("LIST",10));
        System.out.println(redis.getSET("SET"));
        System.out.println(redis.getZSET("ZSET",10));

        System.out.println("\n\n\n\n");
        int i=0;
        int increase=1;
        while(i==0){
            System.out.println("1.增加(默认为1)");
            System.out.println("2.设置增加量");
            System.out.println("3.显示总数");
            System.out.println("4.查看List");
            System.out.println("5.查看Set");
            System.out.println("6.查看ZSet");
            System.out.println("0.退出程序");
            System.out.println("请输入序号：");

            Scanner input=new Scanner(System.in);
            String choice=input.nextLine();//获取输入选项
            switch (choice){
                case "1":{
                    redis.increase("number",increase);
                    System.out.println("当前点击数:" + redis.getInt());
                    break;
                }
                case "2":{
                    Scanner in=new Scanner(System.in);
                    increase=Integer.valueOf(in.nextLine());
                    break;
                }
                case "3":{
                    System.out.println("当前点击数:" + redis.getInt());
                    break;
                }
                case "4":{
                    System.out.println(redis.getLIST("LIST",10));
                    break;
                }
                case "5":{
                    System.out.println(redis.getSET("SET"));
                    break;
                }
                case "6":{
                    System.out.println(redis.getZSET("ZSET",10));
                    break;
                }
                case "0":{
                    i=1;
                    break;
                }
            }
        }


        redis.save();
    }
}
