package src.Minesweeper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class User implements Serializable{

    private String name;
    private ArrayList<Integer> UserTime = new ArrayList<>();

    public User(String name) {
        this.name = name;
    }

    public void addTimeToRecord(int TimeRecord) {
        UserTime.add(TimeRecord);
    }

    public void getName() {
        System.out.println(name);
    }

    public Integer getMinTime() {
        if(UserTime.isEmpty()) {
            return null;
        } else {
            return Collections.min(UserTime);
        }
    }

    public boolean isNull() {
        if(UserTime.isEmpty()) {
            return true;
        } else {
            return false;
        }
 }

    public ArrayList<Integer> getUserTime() {
        return UserTime;
    }


}
