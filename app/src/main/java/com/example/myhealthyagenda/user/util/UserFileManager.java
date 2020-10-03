package com.example.myhealthyagenda.user.util;

import android.content.Context;
import android.util.Log;

import com.example.myhealthyagenda.user.User;
import com.github.mikephil.charting.data.PieEntry;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class UserFileManager {
    private static UserFileManager userFileManager;
    public static final String FILE_DIARY = "diary.txt";
    public static final String FILE_PROFILE = "profile";

    private UserFileManager(){}


    public static UserFileManager getInstance(){
        if(userFileManager == null) {
            userFileManager = new UserFileManager();
        }
        return userFileManager;
    }
    public void intiUserFiles(Context context, User user) throws UserFileException, IOException{
        Log.i("UserFilesManager::initUserFiles","Creating User Files");
        try{
            File userDir = new File(context.getFilesDir(),user.getUsername());
            Log.d("initUserFiles", Arrays.toString(context.fileList()));
            if(!userDir.mkdirs() && !userDir.isDirectory()){
                throw new UserFileException("Could not create files for user " + user.getUsername());
            }
            File currentDiary = new File(userDir,FILE_DIARY);
            File profile = new File(userDir,FILE_PROFILE);
            if(!currentDiary.exists())
                currentDiary.createNewFile();
            if(!profile.exists())
                profile.createNewFile();
            Log.d("PARENT FOLDER:",userDir.getPath());
            for(String file : userDir.list()){
                Log.d("FILE:",file);
            }

        }catch(IOException e){
            throw e;
        }
    }
}
