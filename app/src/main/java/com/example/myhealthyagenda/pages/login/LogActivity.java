package com.example.myhealthyagenda.pages.login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.myhealthyagenda.ApplicationClass;
import com.example.myhealthyagenda.BaseMenuActivity;
import com.example.myhealthyagenda.R;
import com.example.myhealthyagenda.fragments.BalanceFragment;
import com.example.myhealthyagenda.pages.home.UserMainActivity;
import com.example.myhealthyagenda.user.UserNotFoundException;
import com.example.myhealthyagenda.user.util.UserFileException;

import java.io.IOException;

public class LogActivity extends AppCompatActivity {
    private Button btnLog,btnExit;
    private EditText etUser,etPass;
    private TextView tvError;
    private static final String TAG = "LogActivity::";
    public static final String EXTRA_USERNAME = "username";
    public static final String EXTRA_PASSWORD = "pass";
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnLog = findViewById(R.id.btnLog);
        btnExit = findViewById(R.id.btnExit);
        etUser = findViewById(R.id.etUser);
        etPass = findViewById(R.id.etPass);
        tvError = findViewById(R.id.tvError);
        tvError.setVisibility(View.INVISIBLE);
        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    String username = etUser.getText().toString();
                    String password = etPass.getText().toString();
                    if(ApplicationClass.userDB.containsKey(username)){
                        if(ApplicationClass.userDB.get(username).equals(password)){

                            try {
                                Log.i(TAG+"onClick","User Found.Trying to log user");
                               ApplicationClass.logUser(LogActivity.this,Integer.parseInt(username.substring(username.length() - 1)));
                            }catch(NumberFormatException|UserNotFoundException e){
                                Log.e(TAG + "logUser",e.getMessage());
                                e.printStackTrace();
                                ApplicationClass.showErrorToast(LogActivity.this,(ViewGroup)findViewById(R.id.layout_root),getString(R.string.friends_err_inexistent));
                                return;
                            }catch(IOException| UserFileException e){
                                Log.e(TAG+ "logUser",e.getMessage());
                                e.printStackTrace();
                                ApplicationClass.showErrorToast(LogActivity.this,(ViewGroup)findViewById(R.id.layout_root),getString(R.string.error_file_creation));
                                return;
                            }
                            Log.i(TAG + "onClick","Returning from ApplicationClass configuration");
                            Intent logCredentials = new Intent();
                            logCredentials.putExtra(EXTRA_USERNAME,username);
                            logCredentials.putExtra(EXTRA_PASSWORD,password);
                            setResult(Activity.RESULT_OK,logCredentials);
                            LogActivity.this.finish();
                        }
                        else{
                            ApplicationClass.showErrorToast(LogActivity.this,(ViewGroup)findViewById(R.id.layout_root),getString(R.string.friends_err_inexistent));
                            return;
                        }
                    }
                }catch(NullPointerException e){
                    e.printStackTrace();
                    tvError.setVisibility(View.VISIBLE);
                }
            }
        });
        btnExit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                LogActivity.this.finish();
            }
        });

    }

}