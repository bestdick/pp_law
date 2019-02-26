package com.storyvendingmachine.www.pp_law;



import android.Manifest;
import android.content.Intent;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {
    static int REQUEST_CODE_LOGIN = 10001; // login page
    static int REQUEST_CODE_LOGGED_IN = 10003; // Loggedin page
    final static int REQUEST_CODE_FOR_FLASHCARD_WRITE = 22011; // write flashcard
    static String LoginType;
    static String G_user_id;
    static String G_user_level;
    static String G_user_nickname;
    static String G_user_thumbnail;

    ImageView toolbar_thumbnail_imageView;
    TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Login_Initializer();
        permission();
        initializer();

    }
    public void Login_Initializer(){
        toolbar_thumbnail_imageView = (ImageView) findViewById(R.id.toolbar_thumbnail_imageView);

        Intent intent = getIntent();
        LoginType = intent.getStringExtra("login_type");
        G_user_id = intent.getStringExtra("user_id");
        G_user_level =intent.getStringExtra("user_level");
        G_user_nickname = intent.getStringExtra("user_nickname");
        G_user_thumbnail = intent.getStringExtra("user_thumbnail");

        if(LoginType.equals("null")|| G_user_id.equals("null")){
            toolbar_thumbnail_imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent =new Intent(MainActivity.this, LoginActivity.class);
                    startActivityForResult(intent, REQUEST_CODE_LOGIN);// 일반 로그인
                    slide_left_and_slide_in();
                }
            });
        }else{
            getThumbnailImageForAuthor(toolbar_thumbnail_imageView, G_user_thumbnail);
            toolbar_thumbnail_imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent =new Intent(MainActivity.this, LoggedInActivity.class);
                    startActivityForResult(intent, REQUEST_CODE_LOGGED_IN);
                    slide_left_and_slide_in();
                }
            });
        }

//        LoginType = null;
//        G_user_id = null;
//        G_user_level =null;
//        G_user_nickname = null;
//        G_user_thumbnail = null;
    }
    public void initializer(){
        app_start_fragment_initializer();
        TabItemSelector();

    }
    public void toolbarTitle(String title_str){
        TextView toolbar_title_textView = (TextView) findViewById(R.id.toolbar_title_textView);
        toolbar_title_textView.setText(title_str);
    }
    public void app_start_fragment_initializer(){
        HomeFragment homeFragment = new HomeFragment();
        homeFragment.setArguments(null);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, homeFragment)
                .commit();

    }
    public void TabItemSelector(){
            //(Fragment homeFragment,Fragment examFragment,Fragment studyFragment,Fragment settingFragment){
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int tab_position = tab.getPosition();

                String title = "";
                Fragment fragment = null;
                switch (tab_position){
                    case 0:
                        title = "#홈";
                        toolbarTitle(title);
                        fragment = new HomeFragment();
//                        detach_attach_fragment(homeFragment);
//                        last_fragment= 0;
                        break;
                    case 1:
                        title = "#기출문제";
                        toolbarTitle(title);
//                        detach_attach_fragment(examFragment);
                        fragment = new ExamFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("param1", "one");
                        bundle.putString("param2", "two");
                        fragment.setArguments(bundle);
//                        last_fragment= 1;
                        break;
                    case 2:
                        title = "#스터디";
                        toolbarTitle(title);
//                        detach_attach_fragment(studyFragment);
                        fragment = new StudyFragment();
//                        last_fragment= 2;
                        break;
                    case 3:
                        title = "#세팅";
                        toolbarTitle(title);
//                        detach_attach_fragment(settingFragment);
                        fragment = new SettingFragment();
//                        last_fragment= 3;
                        break;
                }
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                Log.e("tab selected", String.valueOf(tab_position));

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public void removeFragmentInSupportManager(){
        for(int i = 0 ; i< getSupportFragmentManager().getFragments().size(); i++){
            getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().getFragments().get(i)).commit();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_LOGIN){
            if(resultCode == RESULT_OK){
                Log.e("login", "login ok");

                LoginType = data.getStringExtra("login_type");
                G_user_id = data.getStringExtra("user_id");
                G_user_level =data.getStringExtra("user_level");
                G_user_nickname = data.getStringExtra("user_nickname");
                G_user_thumbnail = data.getStringExtra("user_thumbnail");
                if(G_user_thumbnail.equals("null")){
                    // if there is no thumbnail
                }else{
                    getThumbnailImageForAuthor(toolbar_thumbnail_imageView, G_user_thumbnail);
                }

                toolbar_thumbnail_imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent =new Intent(MainActivity.this, LoggedInActivity.class);
                        startActivityForResult(intent, REQUEST_CODE_LOGGED_IN);
                        slide_left_and_slide_in();
                    }
                });

                removeFragmentInSupportManager();
                refresh_selected_tab_fragment();
            }else if (resultCode == RESULT_CANCELED){
                Log.e("login", "login cancel");
            }else{
                Log.e("login", "login other");
            }
        }else if(requestCode == REQUEST_CODE_LOGGED_IN){
            if(resultCode == RESULT_OK){
                toolbar_thumbnail_imageView.setImageDrawable(null);
                Log.e("logout", "success");
                toolbar_thumbnail_imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent =new Intent(MainActivity.this, LoginActivity.class);
                        startActivityForResult(intent, REQUEST_CODE_LOGIN);// 일반 로그인
                        slide_left_and_slide_in();
                    }
                });
                removeFragmentInSupportManager();
                refresh_selected_tab_fragment();
            }else{
                Log.e("logout", "logout cancel");
            }
        }
    }
    public void refresh_selected_tab_fragment(){
        int position = tabLayout.getSelectedTabPosition();
        String title = "";
        Fragment fragment = null;
        switch (position){
            case 0:
                title = "#홈";
                toolbarTitle(title);
                fragment = new HomeFragment();
//                        detach_attach_fragment(homeFragment);
//                        last_fragment= 0;
                break;
            case 1:
                title = "#기출문제";
                toolbarTitle(title);
//                        detach_attach_fragment(examFragment);
                fragment = new ExamFragment();
                Bundle bundle = new Bundle();
                bundle.putString("param1", "one");
                bundle.putString("param2", "two");
                fragment.setArguments(bundle);
//                        last_fragment= 1;
                break;
            case 2:
                title = "#스터디";
                toolbarTitle(title);
//                        detach_attach_fragment(studyFragment);
                fragment = new StudyFragment();
//                        last_fragment= 2;
                break;
            case 3:
                title = "#세팅";
                toolbarTitle(title);
//                        detach_attach_fragment(settingFragment);
                fragment = new SettingFragment();
//                        last_fragment= 3;
                break;
        }
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
    public void getThumbnailImageForAuthor(ImageView imageView, String url){
        Picasso.with(this)
                .load(url)
                .transform(new CircleTransform())
                .into(imageView, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {

                    }
                    @Override
                    public void onError() {
                        Log.e("load image", "fail to load images ");
                    }
                });
    }
    public void slide_left_and_slide_in(){//opening new activity
        overridePendingTransition(R.anim.slide_in, R.anim.slide_left_bit); // 처음이 앞으로 들어올 activity 두번째가 현재 activity 가 할 애니매이션
    }
    public void permission(){
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.INTERNET}, 0);
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, 0);
    }
}
