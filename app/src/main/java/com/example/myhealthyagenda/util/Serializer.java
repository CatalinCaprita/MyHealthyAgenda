package com.example.myhealthyagenda.util;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class Serializer {
    private static final String TAG  = "Serializer::";
    private static Serializer instance;
    private Serializer(){}

    public static Serializer getInstance(){
        if(instance == null){
            instance = new Serializer();
        }
        return instance;
    }

    public void serialize(Context context, Object toSerialize, String fileName) throws IOException{
        File destFile = new File(context.getFilesDir(),fileName);
        try(ObjectOutputStream objectOutputStream = new ObjectOutputStream(context.openFileOutput(fileName,Context.MODE_PRIVATE))){
            objectOutputStream.writeObject(toSerialize);
            Log.d("Serialization : ", Arrays.toString(context.fileList()));

        }catch(IOException e){
            throw e;
        }
    }
    public <T> T deserialize(Context context,String fileName, Class<T> clazz) throws IOException{
        File destFile = new File(context.getFilesDir(),fileName);
        try(ObjectInputStream objectInputStream = new ObjectInputStream(context.openFileInput(fileName))){
            T result = clazz.cast(objectInputStream.readObject());
            return result;
        }catch(IOException e){
            throw e;
        } catch (ClassNotFoundException|ClassCastException e) {
            Log.e(TAG + "deserialize ", e.getMessage());
            return null;
        }
    }

}
