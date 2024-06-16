package src.Minesweeper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class User implements Serializable{

    private String name;
    private Map<Integer,ArrayList<Integer>> ModeAndUserTime = new HashMap<>();
    private ArrayList<Integer> mode1Time = new ArrayList<>();
    private ArrayList<Integer> mode2Time = new ArrayList<>();
    private ArrayList<Integer> mode3Time = new ArrayList<>();

    public User(String name) {
        ModeAndUserTime.put(0, mode1Time);
        ModeAndUserTime.put(1, mode2Time);
        ModeAndUserTime.put(2, mode3Time);
        this.name = name;
        try {
            printMinTime(0);
        } catch (NullPointerException e) {
            //UserTime = new ArrayList<>();
            e.printStackTrace();
        }
    }

    public void addTimeToRecord(int gamemode ,int TimeRecord) {
        switch (gamemode) {
            case 0 :
                ModeAndUserTime.get(0).add(TimeRecord);
                break;
            case 1 :
                ModeAndUserTime.get(1).add(TimeRecord);
                break;
            case 2 :
                ModeAndUserTime.get(2).add(TimeRecord);
                break;
            default:
                break;

        }
        
    }

    public void getName() {
        System.out.println(name);
    }

    public void printMinTime(int mode) {
        if(ModeAndUserTime.get(mode).isEmpty()) {
            System.out.println("null");
        } else {
            System.out.println(Collections.min(ModeAndUserTime.get(mode)));
        }
    }

    public boolean isNull(int mode) {
        if(ModeAndUserTime.get(mode).isEmpty()) {
            return true;
        } else {
            return false;
        }
 }
public Integer getMinTime(int mode) {
    if (ModeAndUserTime.get(mode).isEmpty()) {
        return null;
    }
    return ModeAndUserTime.get(mode).stream().min(Integer::compare).orElse(null);
}

    public ArrayList<Integer> getUserTime(int mode) {
        return ModeAndUserTime.get(mode);
    }


}
