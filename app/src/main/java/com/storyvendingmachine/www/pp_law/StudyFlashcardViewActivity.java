package com.storyvendingmachine.www.pp_law;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import java.util.Map;

import static com.storyvendingmachine.www.pp_law.MainActivity.G_user_id;
import static com.storyvendingmachine.www.pp_law.MainActivity.LoginType;
import static com.storyvendingmachine.www.pp_law.UrlBase.base_url;

public class StudyFlashcardViewActivity extends AppCompatActivity {
    final static int REQUEST_CODE_FLASHCARD_REVISE = 30003;// flashcard revise


    StudyFlashcardViewViewPagerAdapter studyFlashcardViewViewPagerAdapter;
    ViewPager fviewPager;
    String flashcard_view_type;//공용 변수

    String author_of_this_flashcard, flashcard_db_id;

    int page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_flashcard_view);
        toolbar();
        element_initializer();
        initializer();
    }
    private void toolbar(){
        Toolbar tb = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_close);
        getSupportActionBar().setTitle("");  //해당 액티비티의 툴바에 있는 타이틀을 공백으로 처리
    }
    public void element_initializer(){
        fviewPager = (ViewPager) findViewById(R.id.flashcardViewPager);
    }
    public void initializer(){
        page = 0 ;
        Intent intent = getIntent();
        flashcard_view_type = intent.getStringExtra("type");
        String primary_key = intent.getStringExtra("primary_key");
        flashcard_db_id = primary_key;
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
                                String author_login_type =jsonObject.getJSONObject("response1").getString("user_login_type");
                                String author_id = jsonObject.getJSONObject("response1").getString("user_id");
                                author_of_this_flashcard = author_id;
                                String author_nickname = jsonObject.getJSONObject("response1").getString("user_nickname");
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

    public void getUpdatedFlashCard(){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = base_url+"getFlashcardList.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("flashcard  response", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String author_login_type =jsonObject.getJSONObject("response1").getString("user_login_type");
                            String author_id = jsonObject.getJSONObject("response1").getString("user_id");
                            author_of_this_flashcard = author_id;
                            String author_nickname = jsonObject.getJSONObject("response1").getString("user_nickname");
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
                params.put("primary_key", flashcard_db_id);
                return params;
            }
        };
        queue.add(stringRequest);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 20001){
//            if (resultCode == RESULT_OK) {
//                comment_layout.removeAllViews();
//                getSelectedFlashCardComments_And_Others();
//            }else if(resultCode == RESULT_CANCELED){
//
//            }
        }else if(requestCode == REQUEST_CODE_FLASHCARD_REVISE){
            if(resultCode ==RESULT_OK){
                getUpdatedFlashCard();
                Log.e("revise", "result ok");
            }else if(resultCode == RESULT_CANCELED){
                Log.e("revise", "result cancel");
            }else{
                Log.e("revise else", String.valueOf(resultCode));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        if(flashcard_view_type.equals("regular")){
            getMenuInflater().inflate(R.menu.flashcard_scrap_add_menu, menu);
        }else{
            getMenuInflater().inflate(R.menu.flashcard_solo_view, menu);
        }

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.flashcard_scrap_add_menu){
//            if (drawer.isDrawerOpen(Gravity.RIGHT)) {
//                //drawer is open
//                drawer.closeDrawer(Gravity.LEFT);
//                drawer.closeDrawer(Gravity.RIGHT);
//
//            } else {
//                //drawer is closed
//                drawer.openDrawer(Gravity.RIGHT);
//            }
        }else if(id == R.id.solo_view_menu) {
//            if (flashcard_view_type.equals("regular")) {
//                Intent intent = new Intent(this, FlashcardSoloViewActivity.class);
//                intent.putExtra("solo_page", true);
//                intent.putExtra("flashcard_or_folder", "flashcard");
//                startActivity(intent);
//                slide_left_and_slide_in();
//            } else {
//                Intent intent = new Intent(this, FlashcardSoloViewActivity.class);
//                intent.putExtra("solo_page", true);
//                intent.putExtra("flashcard_or_folder", "folder");
//                startActivity(intent);
//                slide_left_and_slide_in();
//            }

        }else if(id == R.id.flashcard_more_options){

        }else if(id == R.id.delete_flashcard_option){
            if(author_of_this_flashcard.equals(G_user_id)){
                // the user is the author of this flashcard
                String mes = "이 플래시 카드를 정말 삭제하시겠습니까?";
                String pos_mes = "확인";
                String neg_mes = "취소";
                notifier_delete(mes, pos_mes, neg_mes);
                Log.e("who is the author", "im the author");
            }else{
                String mes = "본인이 작성하신 플래시카드만 삭제가 가능합니다";
                String pos_mes = "확인";
                notifier(mes, pos_mes);
                Log.e("who is the author", author_of_this_flashcard);
            }
        }else if(id == R.id.revise_flashcard_option){
            if(author_of_this_flashcard.equals(G_user_id)){
                // the user is the author of this flashcard
                // when this click change page to revise
                Intent intent = new Intent(StudyFlashcardViewActivity.this, FlashCardWriteActivity.class);
                intent.putExtra("type", "revise");
                intent.putExtra("flashcard_db_id", flashcard_db_id);
                startActivityForResult(intent, REQUEST_CODE_FLASHCARD_REVISE);
//                startActivity(intent);
                slide_left_and_slide_in();
                Log.e("who is the author", "im the author");
            }else{
                String mes = "본인이 작성하신 플래시카드만 수정이 가능합니다";
                String pos_mes = "확인";
                notifier(mes, pos_mes);
                Log.e("who is the author", author_of_this_flashcard);
            }
        }else{
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void notifier(String message, String positive_message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setPositiveButton(positive_message, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create()
                .show();
    }
    public void notifier_delete(String message, String positive_message, String negative_message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setPositiveButton(positive_message, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        flashcard_delete();
                    }
                })
                .setNegativeButton(negative_message, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create()
                .show();
    }
    public void flashcard_delete(){
        String url_getSelectedExam = "http://www.joonandhoon.com/pp/PassPop/android/server/flashcard_delete.php";
        RequestQueue queue = Volley.newRequestQueue(StudyFlashcardViewActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_getSelectedExam,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("scrap response", response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String access_token = jsonObject.getString("access");
                            if(access_token.equals("valid")){
                                boolean result = jsonObject.getBoolean("response");
                                if(result == true){
                                    setResult(RESULT_OK);
                                    finish();
                                    slide_left_and_slide_in();
//                                    onBackPressed();
                                }else{
                                    Toast.makeText(StudyFlashcardViewActivity.this, "삭제 실패", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                //access invalid
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getActivity(), "volley error", Toast.LENGTH_LONG).show();
                //                        String message = "인터넷 연결 에러.. 다시 한번 시도해 주세요...ㅠ ㅠ";
                //                        toast(message);
                //                        getExamNameAndCode(); // 인터넷 에러가 났을시 다시 한번 시도한다.

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("token", "passpop");
                params.put("login_type", LoginType);
                params.put("user_id", G_user_id);
                params.put("flashcard_db_id", flashcard_db_id);
                return params;
            }
        };
        queue.add(stringRequest);
    }


    public void slide_left_and_slide_in(){//opening new activity
        overridePendingTransition(R.anim.slide_in, R.anim.slide_left_bit); // 처음이 앞으로 들어올 activity 두번째가 현재 activity 가 할 애니매이션
    }
}
