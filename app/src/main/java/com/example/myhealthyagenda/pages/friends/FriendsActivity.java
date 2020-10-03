package com.example.myhealthyagenda.pages.friends;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.myhealthyagenda.ApplicationClass;
import com.example.myhealthyagenda.BaseMenuActivity;
import com.example.myhealthyagenda.R;
import com.example.myhealthyagenda.menu.MenuFragment;
import com.example.myhealthyagenda.menu.MenuItem;
import com.example.myhealthyagenda.user.UserNotFoundException;

public class FriendsActivity extends BaseMenuActivity implements MenuFragment.onItemClickedListener,AddFriendFragment.onFriendAddListener {
    Button btnFriends, btnReqs;
    FrameLayout frameFrag;
    Fragment fragDetail;
    ActionBar actionBar;
    Toolbar toolbar;
    private final String FRIEND_LIST_FRAG = "FRIEND_LIST_FRAG";
    private final String MENU_FRAGMENT = "MENU_FRAG";
    private final String FRIEND_ADD = "FRIEND_ADD";
    private final int MENU_ADD_FRIEND = 2;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        //loadMenuRes();
        btnFriends = findViewById(R.id.btnFriends);
        btnReqs = findViewById(R.id.btnReqs);
        frameFrag = findViewById(R.id.frameFrag);
        toolbar = (androidx.appcompat.widget.Toolbar)findViewById(R.id.toolbarFriends);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.show();
        actionBar.setDisplayShowHomeEnabled(true);

        btnFriends.setOnClickListener(view->{
                if(manager.findFragmentByTag(this.FRIEND_LIST_FRAG) == null) {
                    manager.beginTransaction().setCustomAnimations(android.R.anim.fade_in,
                            android.R.anim.fade_out,
                            android.R.anim.fade_in,
                            android.R.anim.fade_out)
                            .replace(R.id.frameFrag, FriendListFragment.newInstance(), this.FRIEND_LIST_FRAG)
                            .commit();
                }
        });

        btnReqs.setOnClickListener(view->{

        });


    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        if(manager.findFragmentByTag(this.MENU_FRAGMENT) != null){
            frameMenu.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT));
        }

    }

    @Override
    public void onRequestSent(String username, String alias){
        manager.popBackStack();
        try{
           user.addFriend(username,alias);
            ApplicationClass.showErrorToast(this,(ViewGroup)findViewById(R.id.layout_root),getString(R.string.friends_add_success));
        }catch (NullPointerException| UserNotFoundException e){
            Log.d("EXECPTION",e.toString());
            ApplicationClass.showErrorToast(this,(ViewGroup)findViewById(R.id.layout_root),getString(R.string.friends_err_inexistent));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        android.view.MenuItem friend = menu.add(Menu.NONE,this.MENU_ADD_FRIEND,Menu.NONE,getString(R.string.label_menu_item_add_friend));
        friend.setIcon(R.drawable.ic_add_friend);
        friend.setShowAsAction(android.view.MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item){
        switch (item.getItemId()){
            case MENU_ADD_FRIEND:{
                manager.beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in,
                                android.R.anim.fade_out,
                                android.R.anim.fade_in,
                                android.R.anim.fade_out)
                        .replace(R.id.frameFrag, AddFriendFragment.newInstance(),this.FRIEND_ADD)
                        .addToBackStack(null)
                        .commit();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void loadMenuRes() {
        frameMenu = findViewById(R.id.frame_menu);
        rootLayout = findViewById(R.id.layout_root);
    }


    @Override
    public int getRootWidth() {
        return 3 * rootLayout.getWidth() / 4;
    }

}
