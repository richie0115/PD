package src.Minesweeper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class User implements Serializable{

    private String name;
    private ArrayList<Integer> UserTime = new ArrayList<>();

    public User(String name) {
        this.name = name;
        try {
            printMinTime();
        } catch (NullPointerException e) {
            //UserTime = new ArrayList<>();
            e.printStackTrace();
        }
    }

    public void addTimeToRecord(int gamemode ,int TimeRecord) {
        UserTime.add(TimeRecord);
    }

    public void getName() {
        System.out.println(name);
    }

    public void printMinTime() {
        if(UserTime.isEmpty()) {
            System.out.println("null");
        } else {
            System.out.println(Collections.min(UserTime));
        }
    }

    public boolean isNull() {
        if(UserTime.isEmpty()) {
            return true;
        } else {
            return false;
        }
 }
 public void setUserTime(ArrayList<Integer> userTime) {
    this.UserTime = userTime;
}

public Integer getMinTime() {
    if (UserTime.isEmpty()) {
        return null;
    }
    return UserTime.stream().min(Integer::compare).orElse(null);
}

    public ArrayList<Integer> getUserTime() {
        return UserTime;
    }


}
