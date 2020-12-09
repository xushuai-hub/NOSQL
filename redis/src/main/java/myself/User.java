package myself;

public class User {
    private String User_id;
    private int counter;
    private String values;
    private String action;
    private String time;

    public User(){}

    public void setUser_id(String user_id){

        this.User_id = user_id;
    }

    public String getUser_id(){

        return this.User_id;
    }

    public void setCounter(int counter){

        this.counter=counter;
    }

    public int getCounter(){

        return this.counter;
    }

    public void setValues(String values){

        this.values=values;
    }

    public String getValues(){

        return this.values;
    }

    public void setAction(String action){

        this.action=action;
    }

    public String getAction(){

        return this.action;
    }

    public void setTime(String time){

        this.time=time;
    }

    public String getTime(){

        return this.time;
    }
}
