package com.storyvendingmachine.www.pp_law;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.storyvendingmachine.www.pp_law.UrlBase.base_url;

public class ExamViewTypeAActivity extends AppCompatActivity {

    ViewPager eviewPager;
    ExamViewTypeAViewPagerAdapter examViewTypeAViewPagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_view_type_a);
        element_initializer();
        initializer();
    }
    public void element_initializer(){
        eviewPager = (ViewPager) findViewById(R.id.examViewPager);
    }

    public void initializer(){
        Intent intent = getIntent();
        String exam_placed_year = intent.getStringExtra("exam_placed_year");
        String major_type = intent.getStringExtra("major_type");
        String minor_type = intent.getStringExtra("minor_type");
        getSelectedExam(exam_placed_year, major_type, minor_type);
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

                                    ExamViewTypeAViewPagerAdapter examViewTypeAViewPagerAdapter = new ExamViewTypeAViewPagerAdapter(getSupportFragmentManager());
                                    examViewTypeAViewPagerAdapter.count = question_count_int;
                                    examViewTypeAViewPagerAdapter.jsonArray = exam_data;

                                    eviewPager.setAdapter(examViewTypeAViewPagerAdapter);
                                    eviewPager.setOffscreenPageLimit(question_count_int);

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

    public class BackgroundTask extends AsyncTask<Void, Void, String> {
        Context context;
        int count;
        JSONArray jsonArray;
        ViewPager viewPager;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids){
            ExamViewTypeAViewPagerAdapter examViewTypeAViewPagerAdapter = new ExamViewTypeAViewPagerAdapter(getSupportFragmentManager());
            examViewTypeAViewPagerAdapter.count = count;
            examViewTypeAViewPagerAdapter.jsonArray = jsonArray;

            viewPager.setAdapter(examViewTypeAViewPagerAdapter);
            viewPager.setOffscreenPageLimit(count);
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... voids) {
            //** INPUT VALUSE IS THE MIDDLE VOID **
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }
    }
}
