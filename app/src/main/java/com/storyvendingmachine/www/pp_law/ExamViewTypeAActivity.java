package com.storyvendingmachine.www.pp_law;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

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
                                JSONArray jsonArray = jsonObject.getJSONArray("response");

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
}
