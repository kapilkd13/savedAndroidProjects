package com.example.anurag.connect_net;

/**
 * Created by Anurag on 05-06-2016.
 */
public class SubscribeItem {
    String name;
    int iconID;
    int buttonID;
    public SubscribeItem(String name, int iconID, int buttonID){
        this.name = name;
        this.iconID = iconID;
        this.buttonID = buttonID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIconID() {
        return iconID;
    }

    public void setIconID(int iconID) {
        this.iconID = iconID;
    }

    public int getButtonID() {
        return buttonID;
    }

    public void setButtonID(int buttonID) {
        this.buttonID = buttonID;
    }
}
