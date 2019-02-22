package com.storyvendingmachine.www.pp_law;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;

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
import java.util.Map;

import static com.storyvendingmachine.www.pp_law.UrlBase.base_url;

public class StudyFlashcardViewActivity extends AppCompatActivity {

    StudyFlashcardViewViewPagerAdapter studyFlashcardViewViewPagerAdapter;
    ViewPager fviewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_flashcard_view);
        element_initializer();
        initializer();
    }
    public void element_initializer(){
        fviewPager = (ViewPager) findViewById(R.id.flashcardViewPager);
    }
    public void initializer(){
        Intent intent = getIntent();
        intent.getStringExtra("type");
        String primary_key = intent.getStringExtra("primary_key");
        getSelectedFlashcard((primary_key));
    }

    public void getSelectedFlashcard(final String primary_key){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = base_url+"getFlashcardList.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("flashcard  response", response);
                        try {
                                JSONObject jsonObject = new JSONObject(response);
                                String flashcard_title = jsonObject.getJSONObject("response1").getString("flashcard_title");
                                String flashcard_count = jsonObject.getJSONObject("response1").getString("flashcard_count");
                                int flashcard_page_count = (Integer.parseInt(flashcard_count)*2);
                                JSONObject flashcard_data_object = new JSONObject(Html.fromHtml(jsonObject.getJSONObject("response1").getString("flashcard_data")).toString());
                                JSONArray flashcard_data_array = flashcard_data_object.getJSONArray("flashcard");

                                ArrayList<String> flashcards = new ArrayList<>();
                                for(int i = 0 ; i<flashcard_data_array.length(); i++){
                                    flashcards.add(flashcard_data_array.getJSONObject(i).getString("term"));
                                    flashcards.add(flashcard_data_array.getJSONObject(i).getString("def"));
                                }

                                studyFlashcardViewViewPagerAdapter = new StudyFlashcardViewViewPagerAdapter(getSupportFragmentManager());
                                studyFlashcardViewViewPagerAdapter.count = flashcard_page_count;
                                studyFlashcardViewViewPagerAdapter.flashcard_array = flashcards;
                                studyFlashcardViewViewPagerAdapter.solo_page = true;
                                studyFlashcardViewViewPagerAdapter.flashcard_or_folder="flashcard";

                                fviewPager.setAdapter(studyFlashcardViewViewPagerAdapter);
                                fviewPager.setOffscreenPageLimit(flashcard_page_count);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
                params.put("type", "selected");
                params.put("primary_key", primary_key);
                return params;
            }
        };
        queue.add(stringRequest);
    }
}
