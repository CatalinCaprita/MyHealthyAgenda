package com.example.myhealthyagenda.user;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.example.myhealthyagenda.ApplicationClass;
import com.example.myhealthyagenda.R;

import java.io.Serializable;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@Entity(tableName = "USER")
public class User implements Serializable {
    @ColumnInfo(name = "USERNAME")
    protected String username;
    @ColumnInfo(name = "LAST_ACTIVE")
    protected LocalDate lastActive;
    @ColumnInfo(name = "PROFILE_DRAW")
    protected int proileDrawable;

    protected float progress;
    protected Map<User,String> friends;
    transient private static Random r;
    private int totalKcal;
    private int actualKcal;
    private static final long serialVersionUID = 891231029380192l;
    public int getTotalKcal() {
        return totalKcal;
    }

    public void setTotalKcal(int totalKcal) {
        this.totalKcal = totalKcal;
    }

    public int getActualKcal() {
        return actualKcal;
    }

    public void setActualKcal(int actualKcal) {
        this.actualKcal = actualKcal;
    }

    public void increaseActualKcal(int newAmount){
        this.actualKcal += newAmount;
    }

    static{
        r = new Random(System.currentTimeMillis());
    }

    public User(String username, int resID) {
        this.username = username;
        proileDrawable = resID > -1 ? resID : R.drawable.ic_user;
        lastActive = (LocalDate.now().minusDays(r.nextInt(60)));
        friends = new HashMap<>();

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDate getLastActive() {
        return lastActive;
    }

    public void setLastActive(LocalDate lastActive) { this.lastActive = lastActive; }

    public int getProileDrawable() {
        return proileDrawable;
    }

    public void setProileDrawable(int proileDrawable) {
        this.proileDrawable = proileDrawable;
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    public Map<User,String> getFriends() {
        return this.friends;
    }

    public ArrayList<String> getFriendsAliases(){
        return new ArrayList<String>(friends.values());
    }
    public void setFriends(Map<User,String> friends) {
        this.friends = friends;
    }


    public void addNFriends(int n ){
        Log.d("User::addNFriends","Adding random friends for " + getUsername() );
        for(int i=0; i < n ; i++){
            User friend = ApplicationClass.users.get(r.nextInt(ApplicationClass.users.size()));
            friends.putIfAbsent(friend,friend.getUsername());
        }
    }
    public void addFriend(String username,String alias) throws NullPointerException,UserNotFoundException{
       int userID = -100;
        try{
            userID = Integer.parseInt(username.substring(4));
        }catch(NumberFormatException|StringIndexOutOfBoundsException e){
            throw new UserNotFoundException("For user: " + userID);
        }
        if(userID >= ApplicationClass.users.size()){
            throw new NullPointerException();
        }
        if(alias != null) {
            friends.put(ApplicationClass.users.get(userID),alias);
        }else{
            friends.put(ApplicationClass.users.get(userID),"No Alias");
        }
    }

}
