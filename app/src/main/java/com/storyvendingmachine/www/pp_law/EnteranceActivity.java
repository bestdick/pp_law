package com.storyvendingmachine.www.pp_law;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kakao.auth.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import static com.storyvendingmachine.www.pp_law.LoginActivity.callback;

public class EnteranceActivity extends AppCompatActivity {
    SharedPreferences login_remember;
    SharedPreferences.Editor editor;
    static String LATEST_VERSION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enterance);
        getKeyHash();
        //full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //full screen

        login_remember = getSharedPreferences("setting", 0);
        editor = login_remember.edit();
        String LT = login_remember.getString("login_type", "");
        Log.e("login_type:::", LT);

//        if(LT.equals("kakao")){
//            new Handler().postDelayed(new Runnable() {// 1 초 후에 실행
//                @Override
//                public void run() {
//                    //****************kakao login **************************
//                    callback = new SessionCallback(EnteranceActivity.this, "enterance_activity", editor);
//                    callback.pb = (ProgressBar) findViewById(R.id.progress_bar);
//
//                    Session.getCurrentSession().addCallback(callback);
//                    if (Session.getCurrentSession().checkAndImplicitOpen()) {
//                        // 액세스토큰 유효하거나 리프레시 토큰으로 액세스 토큰 갱신을 시도할 수 있는 경우
//                    } else {
//                        // 무조건 재로그인을 시켜야 하는 경우
//                        //            Session.getCurrentSession().clearCallbacks();
//                    }
//                    //****************kakao login **************************
//
//                }
//            }, 800);
//        }else
            if(LT.equals("normal")){
            new Handler().postDelayed(new Runnable() {// 1 초 후에 실행
                @Override
                public void run() {
                    LoginRemember();
                }
            }, 800);
        }else{
            new Handler().postDelayed(new Runnable() {// 1 초 후에 실행
                @Override
                public void run() {
                    Intent intent = new Intent(EnteranceActivity.this, MainActivity.class);
                    String str_null="null";
                    intent.putExtra("login_type", str_null);
                    intent.putExtra("user_id", str_null);
                    intent.putExtra("member_level", str_null);
                    intent.putExtra("user_nickname", str_null);
                    intent.putExtra("user_thumbnail", str_null);
                    intent.putExtra("user_selected_last_exam_code", str_null);
                    intent.putExtra("user_selected_last_exam_name", str_null);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    finish();
                }
            }, 800);
        }

    }

    public void LoginRemember(){
//        login_remember = getSharedPreferences("setting", 0);
//        editor = login_remember.edit();
        new Handler().postDelayed(new Runnable() {// 1 초 후에 실행
            @Override
            public void run() {
                // 실행할 동작 코딩
                //front_cover.setVisibility(View.GONE);

                boolean login_success = login_remember.getBoolean("id_pw_match", true);
                if(login_success){
                    String user_email = login_remember.getString("user_email", "");
                    String user_password = login_remember.getString("user_password", "");
                    if(user_email.trim().length() == 0 || user_password.trim().length()==0){
                        Toast.makeText(EnteranceActivity.this, "preference empty 1", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(FrontActivity.this, LoginActivity.class);
//                        startActivity(intent);
//                        finish();
//                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    }else{
                        Toast.makeText(EnteranceActivity.this, "login check", Toast.LENGTH_SHORT).show();
                        loginCheck(user_email, user_password);
                    }

                }else{

                    Toast.makeText(EnteranceActivity.this, "preference empty 2", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EnteranceActivity.this, MainActivity.class);
                    String str_null="null";
                    intent.putExtra("login_type", str_null);
                    intent.putExtra("user_id", str_null);
                    intent.putExtra("member_level", str_null);
                    intent.putExtra("user_nickname", str_null);
                    intent.putExtra("user_thumbnail", str_null);
                    intent.putExtra("user_selected_last_exam_code", str_null);
                    intent.putExtra("user_selected_last_exam_name", str_null);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    finish();

                }
            }
        }, 800);


    }
    private void loginCheck(final String input_user_email, final String input_user_password){
        RequestQueue queue = Volley.newRequestQueue(EnteranceActivity.this);
//        String url = "http://www.storyvendingmachine.com/android/front_page_normal_login_check.php";
        String url = "http://www.joonandhoon.com/pp/PassPop/android/server/front_page_normal_login_check.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("enterance response", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("response");
                            JSONObject temp = jsonArray.getJSONObject(0);

                            String login_success_fail = temp.getString("call");
                            if(login_success_fail.equals("login_success")){
                                String user_email = temp.getString("user_email");
                                String member_level = temp.getString("member_level");
                                String user_nickname = temp.getString("user_nickname");
                                String user_thumbnail = temp.getString("user_thumbnail");
                                String user_selected_last_exam_code = temp.getString("user_selected_last_exam_code");
                                String user_selected_last_exam_name = temp.getString("user_selected_last_exam_name");

                                editor.putBoolean("id_pw_match", true);
                                editor.putString("user_email", input_user_email);
                                editor.putString("user_password", input_user_password);
                                editor.commit();


                                Intent intent = new Intent(EnteranceActivity.this, MainActivity.class);
                                intent.putExtra("login_type", "normal");
                                intent.putExtra("user_id", user_email);
                                intent.putExtra("member_level", member_level);
                                intent.putExtra("user_nickname", user_nickname);
                                intent.putExtra("user_thumbnail", user_thumbnail);
                                intent.putExtra("user_selected_last_exam_code", user_selected_last_exam_code);
                                intent.putExtra("user_selected_last_exam_name", user_selected_last_exam_name);
                                startActivity(intent);
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                finish();


//                                Log.e("check success:::", "true");
//                                Log.e("login check ::::", LoginType+"//"+G_user_id);


                            }else if(login_success_fail.equals("login_fail")){
                                editor.putString("login_type", "null");
                                editor.putBoolean("id_pw_match", false);
                                editor.putString("user_email", "");
                                editor.putString("user_password", "");
                                editor.commit();

                                String str_null="null";
                                Intent intent = new Intent(EnteranceActivity.this, MainActivity.class);
                                intent.putExtra("login_type", str_null);
                                intent.putExtra("user_id", str_null);
                                intent.putExtra("member_level", str_null);
                                intent.putExtra("user_nickname", str_null);
                                intent.putExtra("user_thumbnail", str_null);
                                intent.putExtra("user_selected_last_exam_code", str_null);
                                intent.putExtra("user_selected_last_exam_name", str_null);
                                startActivity(intent);
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                finish();

                            }else{
                                Log.e("알수 없는 에러 발생", "php 를 켜서 확인 하세요");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", input_user_email);
                params.put("password", input_user_password);
                return params;
            }
        };
        queue.add(stringRequest);
    }




    private void getKeyHash() {

        PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo("com.storyvendingmachine.www.pp_law", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.e("hash key", something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }
    }
}
