package com.storyvendingmachine.www.pp_law;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.storyvendingmachine.www.pp_law.UrlBase.base_url;

public class ExamViewTypeAActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    static ProgressBar Exam_type_A_progressBar;
    ProgressBar page_accomplished_prgressBar;
    ViewPager eviewPager;
    ExamViewTypeAViewPagerAdapter examViewTypeAViewPagerAdapter;
    static ArrayList<Integer> UserSelectedAnswer;
    DrawerLayout drawer;
    NavigationView navigationView;
    LinearLayout answer_sheet_element_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_view_type_a);
        toolbar();
        element_initializer();
        initializer();
    }
    public void element_initializer(){
        Exam_type_A_progressBar = (ProgressBar) findViewById(R.id.progressBar);
        page_accomplished_prgressBar = (ProgressBar) findViewById(R.id.page_accomplished_prgressBar);
        eviewPager = (ViewPager) findViewById(R.id.examViewPager);
        UserSelectedAnswer =new  ArrayList<Integer>();
        drawer = (DrawerLayout) findViewById(R.id.drawer);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        answer_sheet_element_layout = (LinearLayout) navigationView.findViewById(R.id.answer_element_layout);
    }

    public void initializer(){
        Intent intent = getIntent();
        String exam_placed_year = intent.getStringExtra("exam_placed_year");
        String major_type = intent.getStringExtra("major_type");
        String minor_type = intent.getStringExtra("minor_type");
        getSelectedExam(exam_placed_year, major_type, minor_type);
        viewPagerProgressbar();
        drawer_listener();
    }
    private void toolbar(){
        Toolbar tb = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_close);
        getSupportActionBar().setTitle("");  //해당 액티비티의 툴바에 있는 타이틀을 공백으로 처리
    }


    public void getSelectedExam(final String exam_placed_year, final String major_type, final String minor_type){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = base_url+"getExamList.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("exam selected response", response);
                        try {
                                JSONObject jsonObject = new JSONObject(response);
                                String access = jsonObject.getString("access");
                                if(access.equals("valid")){
                                    JSONObject response1 = jsonObject.getJSONObject("response1");
                                    String question_count_str = response1.getString("question_count");
                                    int question_count_int = Integer.parseInt(question_count_str);
                                    JSONArray exam_data = response1.getJSONArray("exam_data");

                                    examViewTypeAViewPagerAdapter = new ExamViewTypeAViewPagerAdapter(getSupportFragmentManager());
                                    examViewTypeAViewPagerAdapter.count = question_count_int;
                                    examViewTypeAViewPagerAdapter.jsonArray = exam_data;

                                    eviewPager.setAdapter(examViewTypeAViewPagerAdapter);
                                    eviewPager.setOffscreenPageLimit(question_count_int);
                                    eviewPager.setPageMargin(16);

                                    create_answer_sheet(exam_data.length(), exam_data, exam_placed_year, major_type, minor_type);
                                }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//
//                        examlistviewAdapter.notifyDataSetChanged();
                    }},
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("are we here 6", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                params.put("token", "passpop");
                params.put("type", "select");
                params.put("exam_placed_year", exam_placed_year);
                params.put("major_type", major_type);
                params.put("minor_type", minor_type);
                return params;
            }
        };
        queue.add(stringRequest);
    }
    public void create_answer_sheet(int count, JSONArray exam_data, String exam_placed_year, String major_type, String minor_type){
        TextView title_textView = new TextView(ExamViewTypeAActivity.this);
//        title_textView.setBackground(getResources().getDrawable(R.drawable.outline_round_orange));
        title_textView.setGravity(Gravity.CENTER);
        title_textView.setText("답안지");
        answer_sheet_element_layout.addView(title_textView);
        for(int i =0 ; i<count; i++){
            final int ii = i;
            View answer_sheet_element = getLayoutInflater().inflate(R.layout.container_exam_view_type_a_answer_sheet_element, null);
            answer_sheet_element.setId(i);

            TextView questionNumber = (TextView) answer_sheet_element.findViewById(R.id.questionNumber_textView);
            questionNumber.setText(String.valueOf(i+1)+".");

            ConstraintLayout layout = (ConstraintLayout) answer_sheet_element.findViewById(R.id.answer_sheet_element_container);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("page", "page:::"+String.valueOf(ii));
                    eviewPager.setCurrentItem(ii);
                    drawer.closeDrawer(Gravity.RIGHT);
                }
            });

            answer_sheet_element_layout.addView(answer_sheet_element);

        }
        Button submit_button = new Button(ExamViewTypeAActivity.this);
        examSubmitButtonProcess(submit_button, exam_data,  exam_placed_year,  major_type,  minor_type);// 시험 종료 및 체점 버튼 프로세스
        answer_sheet_element_layout.addView(submit_button);
    }
    public void examSubmitButtonProcess(Button submitButton, final JSONArray exam_data, final String exam_placed_year, final String major_type, final String minor_type){// 기출 시험에만 적용되는것
        submitButton.setText("시험 종료 및 체점");
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0,20,0,20);
        submitButton.setLayoutParams(params);
//        submitButton.setBackground(getResources().getDrawable(R.drawable.solid_round_orange));
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean is = checkifAllAnswersAreSelected();
                if(is){
                    String message = "채점 하시겠습니까?";
                    String pos_message = "네";
                    String neg_message = "아니요";
                    notifier_examSubmitButtonProcess(message, pos_message, neg_message, exam_data, exam_placed_year,  major_type,  minor_type);
                }else{
                    String message = "답을 선택하지 않은 문제가 존재합니다.\n그래도 채점 하시겠습니까?";
                    String pos_message = "네";
                    String neg_message = "아니요";
                    notifier_examSubmitButtonProcess(message, pos_message, neg_message, exam_data,exam_placed_year,  major_type,  minor_type);
                }
            }
        });
    }
    public void notifier_examSubmitButtonProcess(String message, String positive_message, String negative_message, final JSONArray exam_data, final String exam_placed_year, final String major_type, final String minor_type){
        AlertDialog.Builder builder = new AlertDialog.Builder(ExamViewTypeAActivity.this);
        builder.setMessage(message)
                .setPositiveButton(positive_message, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //positive button
                        sendExamResult(exam_data, exam_placed_year,  major_type,  minor_type);
                        drawer.closeDrawer(Gravity.RIGHT);
                    }
                })
                .setNegativeButton(negative_message, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //negative button
                        drawer.closeDrawer(Gravity.RIGHT);
                    }
                })
                .create()
                .show();
    }
    public boolean checkifAllAnswersAreSelected(){
        boolean isEverythingSelected = true;
        for(int l = 0 ; l <UserSelectedAnswer.size(); l++){
            int user_selected_answer_choice = UserSelectedAnswer.get(l);
            if(user_selected_answer_choice==-1){
//                Toast.makeText(ExamViewActivity.this, String.valueOf(l)+"번 미싱", Toast.LENGTH_SHORT).show();
                isEverythingSelected = false;
            }
        }
        return isEverythingSelected;
    }
    public void viewPagerProgressbar(){
        eviewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                page_accomplished_prgressBar.setProgress(position);
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    public void drawer_listener(){
        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
//                Toast.makeText(ExamViewActivity.this, "on slide", Toast.LENGTH_SHORT).show();
//                Log.e("drawer state ", "on slide");
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                    makeAnswerSheet();
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
//                Toast.makeText(ExamViewActivity.this, "close", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDrawerStateChanged(int newState) {
//                Log.e("drawer state ", "on change");
//                Toast.makeText(ExamViewActivity.this, "on change", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void makeAnswerSheet(){// 기출 시험에만 적용되는것
        int size = UserSelectedAnswer.size();
        for(int i = 0; i<size; i++){
            View v = findViewById(i);
            TextView one = (TextView) v.findViewById(R.id.answerChoice_1_textView);
            TextView two = (TextView) v.findViewById(R.id.answerChoice_2_textView);
            TextView three = (TextView) v.findViewById(R.id.answerChoice_3_textView);
            TextView four = (TextView) v.findViewById(R.id.answerChoice_4_textView);
            TextView five = (TextView) v.findViewById(R.id.answerChoice_5_textView);

            int answer1 = UserSelectedAnswer.get(i);

            if(answer1 ==1){
                one.setTextColor(getResources().getColor(R.color.colorWhite));
                one.setBackground(getResources().getDrawable(R.drawable.answer_selected_container));
                two.setBackground(null);
                three.setBackground(null);
                four.setBackground(null);
                five.setBackground(null);
            }else if(answer1==2){
                one.setBackground(null);
                two.setTextColor(getResources().getColor(R.color.colorWhite));
                two.setBackground(getResources().getDrawable(R.drawable.answer_selected_container));
                three.setBackground(null);
                four.setBackground(null);
                five.setBackground(null);
            }else if(answer1==3){
                one.setBackground(null);
                two.setBackground(null);
                three.setTextColor(getResources().getColor(R.color.colorWhite));
                three.setBackground(getResources().getDrawable(R.drawable.answer_selected_container));
                four.setBackground(null);
                five.setBackground(null);
            }else if(answer1==4){
                one.setBackground(null);
                two.setBackground(null);
                three.setBackground(null);
                four.setTextColor(getResources().getColor(R.color.colorWhite));
                four.setBackground(getResources().getDrawable(R.drawable.answer_selected_container));
                five.setBackground(null);
            }else if(answer1==5){
                one.setBackground(null);
                two.setBackground(null);
                three.setBackground(null);
                four.setBackground(null);
                five.setTextColor(getResources().getColor(R.color.colorWhite));
                five.setBackground(getResources().getDrawable(R.drawable.answer_selected_container));
            }
        }
    }

    public void sendExamResult(JSONArray exam_data, String exam_placed_year, String major_type, String minor_type){
        Intent intent = new Intent(ExamViewTypeAActivity.this, ExamResultTypeAActivity.class);
        intent.putExtra("exam_placed_year", exam_placed_year);
        intent.putExtra("major_type", major_type);
        intent.putExtra("minor_type", minor_type);
        intent.putExtra("exam_data", exam_data.toString());
        intent.putExtra("UserSelectedAnswer", UserSelectedAnswer);
        startActivity(intent);
        finish();
    }


    public void progressbar_visible(){
        Exam_type_A_progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
    public void progressbar_invisible(){
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        Exam_type_A_progressBar.setVisibility(View.GONE);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.exam_answer_sheet, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.answer_sheet_menu){
            //기출 시험 응시
            // drawer
            if (drawer.isDrawerOpen(Gravity.RIGHT)) {
                //drawer is open
//                drawer.closeDrawer(Gravity.LEFT);
                drawer.closeDrawer(Gravity.RIGHT);

            } else {
                //drawer is closed
                drawer.openDrawer(Gravity.RIGHT);
            }
        }else{
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

}
