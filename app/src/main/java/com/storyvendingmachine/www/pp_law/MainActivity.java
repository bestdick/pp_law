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

public class MainActivity extends AppCompatActivity {

//    int last_fragment;
//    HomeFragment homeFragment;
//    ExamFragment examFragment;
//    StudyFragment studyFragment;
//    SettingFragment settingFragment;

    static String LoginType;
    static String G_user_id;
    static String G_user_level;
    static String G_user_nickname;
    static String G_user_thumbnail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Login_Initializer();
        permission();
        initializer();

    }
    public void Login_Initializer(){
        LoginType = null;
        G_user_id = null;
        G_user_level =null;
        G_user_nickname = null;
        G_user_thumbnail = null;
    }
    public void initializer(){
        app_start_fragment_initializer();
        TabItemSelector();
        ImageView toolbar_thumbnail_imageView = (ImageView) findViewById(R.id.toolbar_thumbnail_imageView);
        toolbar_thumbnail_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(MainActivity.this, LoginActivity.class);
                startActivityForResult(intent, 10002);// 10002 카카오 로그인 RESULT 값
//                slide_left_and_slide_in();
            }
        });
    }
    public void toolbarTitle(String title_str){
        TextView toolbar_title_textView = (TextView) findViewById(R.id.toolbar_title_textView);
        toolbar_title_textView.setText(title_str);
    }
    public void app_start_fragment_initializer(){
//        last_fragment= 0;
        HomeFragment homeFragment = new HomeFragment();
//        homeFragment = new HomeFragment();
        homeFragment.setArguments(null);
        ExamFragment examFragment = new ExamFragment();
//        examFragment = new ExamFragment();
        examFragment.setArguments(null);
        StudyFragment studyFragment = new StudyFragment();
//        studyFragment = new StudyFragment();
        studyFragment.setArguments(null);
        SettingFragment settingFragment = new SettingFragment();
//        settingFragment = new SettingFragment();
        settingFragment.setArguments(null);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, homeFragment)
//                .add(R.id.container, examFragment)
//                .add(R.id.container, studyFragment)
//                .add(R.id.container, settingFragment)
//                .detach(examFragment)
//                .detach(studyFragment)
//                .detach(settingFragment)
                .commit();

    }
    public void TabItemSelector(){
            //(Fragment homeFragment,Fragment examFragment,Fragment studyFragment,Fragment settingFragment){
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
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

//    public void detach_attach_fragment(Fragment attach_fragment){
//        FragmentManager fm = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fm.beginTransaction();
//        if(last_fragment == 0){
//            fragmentTransaction.detach(homeFragment);
//        }else if(last_fragment ==1){
//            fragmentTransaction.detach(examFragment);
//        }else if(last_fragment==2){
//            fragmentTransaction.detach(studyFragment);
//        }else{
//            //last_fragment ==3
//            fragmentTransaction.detach(settingFragment);
//        }
//        fragmentTransaction.attach(attach_fragment);
////        fragmentTransaction.add(R.id.container, attach_fragment);
//        fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.commit();
//    }
    public void permission(){
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.INTERNET}, 0);
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, 0);
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
