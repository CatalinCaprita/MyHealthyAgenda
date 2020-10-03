package com.example.myhealthyagenda.pages.home;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.sqlite.db.SupportSQLiteDatabase;

import android.accounts.AbstractAccountAuthenticator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.myhealthyagenda.ApplicationClass;
import com.example.myhealthyagenda.BaseMenuActivity;
import com.example.myhealthyagenda.MHADatabase;
import com.example.myhealthyagenda.R;
import com.example.myhealthyagenda.article.ArticleAdapter;
import com.example.myhealthyagenda.fragments.BalanceFragment;
import com.example.myhealthyagenda.menu.MenuFragment;
import com.example.myhealthyagenda.menu.MenuItem;
import com.example.myhealthyagenda.pages.diary.DiaryActivity;
import com.example.myhealthyagenda.pages.friends.FriendsActivity;
import com.example.myhealthyagenda.pages.login.LogActivity;
import com.example.myhealthyagenda.user.User;
import com.example.myhealthyagenda.util.Serializer;

import java.io.IOException;

public class UserMainActivity extends BaseMenuActivity {
    private final String FRAGMENT_DETAIL = "FRAGMENT_DETAIL";
    private static final String TAG =  "UserMainActivity::";
    int totalCal = -1;
    int todayCal = -1;
    TextView tvDetail;
    ProgressBar pbCals;
    BalanceFragment fragBalance;
    FrameLayout frameBalance;
    RecyclerView rvArticles;
    ActionBar actionBar;
    FragmentManager manager;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(isLogged){
            Log.d(TAG +"onCreate","User is Logged. Fetching DB data");
            startActivityForResult(new Intent(this,LoadingActivity.class),REQUEST_LOADING_DB);
        }
        setContentView(R.layout.activity_user_main);
        loadMenuRes();
        setSupportActionBar((androidx.appcompat.widget.Toolbar)findViewById(R.id.toolbarMain));
        actionBar = getSupportActionBar();
        actionBar.show();
        actionBar.setDisplayHomeAsUpEnabled(true);
        manager = getSupportFragmentManager();
        rvArticles = findViewById(R.id.list);
        rvArticles.setHasFixedSize(true);
        rvArticles.setLayoutManager(new LinearLayoutManager(UserMainActivity.this));
        rvArticles.setAdapter(new ArticleAdapter(UserMainActivity.this, ApplicationClass.articles));
        DividerItemDecoration divider = new DividerItemDecoration(this, LinearLayout.VERTICAL);
        divider.setDrawable(getDrawable(R.drawable.item_article_divider));
        rvArticles.addItemDecoration(divider);
        frameBalance = findViewById(R.id.frame_balance);

        MHADatabase database = MHADatabase.getInstance(this);
        SupportSQLiteDatabase s = database.getOpenHelper().getWritableDatabase();

        if(!isLogged){
            Log.d(TAG + "onCreate","User has not been Serialized yet. Requesting LogIn Credentials");
            Intent requestLog = new Intent(UserMainActivity.this,LogActivity.class);
            requestLog.setAction(Intent.ACTION_ATTACH_DATA);
            startActivityForResult(requestLog,BaseMenuActivity.REQUEST_LOG_USER);
        }


    }

    @Override
    protected void onResume(){
        super.onResume();
        if(user != null) {
               manager.beginTransaction()
                        .replace(R.id.frame_balance, BalanceFragment.newInstance(user.getTotalKcal(), user.getActualKcal())
                                , BalanceFragment.TAG_FRAG_BALANCE)
                        .commit();

        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    public void onActivityResult(int requestCode,int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        switch (requestCode){
            case BaseMenuActivity.REQUEST_LOG_USER:{
                if(resultCode == Activity.RESULT_OK){
                    try {
                        user = ApplicationClass.getLoggedUser();
                        Log.d(TAG + "onActivityResult","Serializing The Logged User with kcal: " + user.getActualKcal());
                        Serializer.getInstance().serialize((BaseMenuActivity)(this),user,USER_SER_FILENAME);
                        new BaseMenuActivity.SetLoggedInAsyncTask(this,true).execute();
                        startActivityForResult(new Intent(this,LoadingActivity.class),REQUEST_LOADING_DB);
                    }catch(IOException e){
                        Log.e(TAG+"onCreate",e.getMessage());
                    }
                    break;
                }
                break;
            }
            case BaseMenuActivity.REQUEST_LOADING_DB:{
                if(resultCode == Activity.RESULT_OK){
                    Log.d(TAG +"onActivityResult","Data has been Loaded into DB");
                    user.setActualKcal(data.getIntExtra(LoadingActivity.EXTRA_USER_ACTUAL_KCAL,0));
                    break;
                }
                break;
            }
        }

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item){
        switch (item.getItemId()){
            case R.id.menu_main:{
                return super.onOptionsItemSelected(item);
            }
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void loadMenuRes() {
        rootLayout = findViewById(R.id.layout_port) == null ? findViewById(R.id.layout_land) : findViewById(R.id.layout_port);
        frameMenu = findViewById(R.id.frame_menu);
    }

    @Override
    public int getRootWidth(){
        return 3 * rootLayout.getWidth() / 4;
    }


}