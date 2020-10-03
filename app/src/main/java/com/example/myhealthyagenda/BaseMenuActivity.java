package com.example.myhealthyagenda;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;

import com.example.myhealthyagenda.fragments.LogOutDialogFramgent;
import com.example.myhealthyagenda.menu.MenuFragment;
import com.example.myhealthyagenda.pages.diary.DiaryActivity;
import com.example.myhealthyagenda.pages.friends.FriendsActivity;
import com.example.myhealthyagenda.pages.home.UserMainActivity;
import com.example.myhealthyagenda.user.User;
import com.example.myhealthyagenda.util.Serializer;

import java.io.IOException;
import java.util.Arrays;

public abstract class BaseMenuActivity extends AppCompatActivity
        implements MenuFragment.onItemClickedListener,
        LogOutDialogFramgent.OnLogOutConfirmedListener {
    protected FragmentManager manager;
    protected FrameLayout frameMenu;
    protected ViewGroup rootLayout;
    protected  User user;
    protected final String FRAGMENT_MENU = "FRAGMENT_MENU";
    protected final String FRAGMENT_LOGOUT = "FRAGMENT_LOGOUT";
    protected boolean isLogged;
    private static final String TAG = "BaseMenuActivity::";
    public static final int REQUEST_LOG_USER = 0;
    public static final int REQUEST_LOADING_DB = 1;

    public static final String USER_SER_FILENAME = "profile2.ser";
    public static final String DIARY_SER_FILENAME = "current_diary.ser";

    public static final String SHARED_PREF_FILENAME = "com.example.myhealthyagenda.log_status";
    public static final String SHARED_LOG_STATUS = "log_status";
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        manager = getSupportFragmentManager();
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_FILENAME,MODE_PRIVATE);
        isLogged = sharedPreferences.getBoolean(SHARED_LOG_STATUS,false);
        if(isLogged)
            try {
                if (ApplicationClass.getLoggedUser() == null) {
                    Log.d(TAG + "onCreate","User Is Logged! Deserializing");
                    user = Serializer.getInstance().deserialize(BaseMenuActivity.this,USER_SER_FILENAME,User.class);
                    ApplicationClass.setLoggedUser(user);
                }
                else
                    user = ApplicationClass.getLoggedUser();
            }catch(IOException e){
                Log.e(TAG+"onCreate",e.getMessage());
            }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.user_main,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        while(true) {
            try{
                switch (item.getItemId()) {
                    case R.id.menu_main: {
                        frameMenu.setLayoutParams(new RelativeLayout.LayoutParams(getRootWidth(),
                                RelativeLayout.LayoutParams.MATCH_PARENT));
                        frameMenu.bringToFront();
                        if(manager.findFragmentByTag(FRAGMENT_MENU) == null) {
                            manager.beginTransaction()
                                    .setCustomAnimations(android.R.anim.slide_in_left,
                                            android.R.anim.fade_out,
                                            android.R.anim.slide_in_left,
                                            android.R.anim.fade_out)
                                    .add(frameMenu.getId(), MenuFragment.newInstance(user.getUsername()), this.FRAGMENT_MENU)
                                    .addToBackStack(null)
                                    .commit();
                        }
                        return true;
                    }
                    default: {
                        return super.onOptionsItemSelected(item);
                    }
                }
            }catch (NullPointerException e){
                loadMenuRes();
            }
        }

    }
    @Override
    public void onItemClicked( com.example.myhealthyagenda.menu.MenuItem index) {
        FragmentManager manager = getSupportFragmentManager();
        manager.popBackStack();
        switch(index.getDrawableRes()){
            case R.drawable.ic_friends:{
                if(!(this.getClass().isAssignableFrom(FriendsActivity.class))) {
                    Intent friendsIntent = new Intent(Intent.ACTION_VIEW);
                    friendsIntent.setClass(BaseMenuActivity.this, FriendsActivity.class);
                    startActivity(friendsIntent);
                }
                break;
            }
            case R.drawable.ic_diary:{
                if(!(this.getClass().isAssignableFrom(DiaryActivity.class))){
                    Intent diaryIntent = new Intent(BaseMenuActivity.this, DiaryActivity.class);
                    startActivity(diaryIntent);
                }
                break;

            }
            case R.drawable.ic_home:{
                if(!(this.getClass().isAssignableFrom(UserMainActivity.class))){
                    Intent homeIntent = new Intent(BaseMenuActivity.this,UserMainActivity.class);
                    startActivity(homeIntent);
                }
                break;
            }
            case R.drawable.ic_log_out:{
                LogOutDialogFramgent dialog = new LogOutDialogFramgent();
                dialog.show(getSupportFragmentManager(),FRAGMENT_LOGOUT);
            }
            default: return;
        }
    }

    @Override
    public void onStop(){
        super.onStop();
        if(isLogged && !(this.getClass().isAssignableFrom(UserMainActivity.class))) {
            Log.d(TAG + "onStop", "Serializing User");
            try {
                Serializer.getInstance().serialize(BaseMenuActivity.this, user, USER_SER_FILENAME);
                ApplicationClass.setLoggedUser(null);
            } catch (IOException e) {
                Log.e(TAG + "onStop", e.getMessage());
            }
            //Log.d(TAG + "onStop","Logging out the user");
            //new SetLoggedInAsyncTask(this,false).execute();
        }
    }

    @Override
    public void onLogOutConfirmed() {
        Log.d(TAG +"onLogoutConfirmed","Providint base implementation of loggin out...");
        new SetLoggedInAsyncTask(this,false).execute();
        this.finish();
    }

    public abstract void loadMenuRes();
    public abstract  int getRootWidth();

    public static class SetLoggedInAsyncTask extends AsyncTask<Void,Void,Void> {
        private Context context;
        private boolean value;
        public SetLoggedInAsyncTask(Context context,boolean value){
            this.context = context;
            this.value = value;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            Log.i("SetLoggedInAsyncTask","Setting LoggedIn to True in background");
            SharedPreferences.Editor sharedEdit = context.getSharedPreferences(BaseMenuActivity.SHARED_PREF_FILENAME,MODE_PRIVATE).edit();
            sharedEdit.putBoolean(BaseMenuActivity.SHARED_LOG_STATUS,value);
            sharedEdit.commit();
            return null;
        }
    }
}
