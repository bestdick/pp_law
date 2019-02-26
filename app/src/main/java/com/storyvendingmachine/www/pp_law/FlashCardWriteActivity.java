package com.storyvendingmachine.www.pp_law;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.melnykov.fab.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.storyvendingmachine.www.pp_law.MainActivity.G_user_id;
import static com.storyvendingmachine.www.pp_law.MainActivity.LoginType;
import static com.storyvendingmachine.www.pp_law.UrlBase.base_url;

public class FlashCardWriteActivity extends AppCompatActivity {
    final static int RESULT_CODE_SELECT_EXAM = 30001;
    final static int RESULT_CODE_SELECT_SUBJECT = 30002;
    ListView listView;
    static List<FlashCardWriteList> flashcardwriteList;
    static FlashCardWriteListAdapter flashCardWriteListAdapter;

    FloatingActionButton fab;
    EditText flashcard_title_editText;
    Button flashcard_select_exam_button;
    Button flashcard_select_minor_type;

    String selected_exam_name;
    String selected_exam_code;

    String subject_name;
    String subject_code;
    String subject_number;
    String flashcard_db_id;

    String minor_type;

    JSONObject header_jsonObject;
    JSONObject jsonObjectTotal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_card_write);
        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        if(type.equals("revise")){
            //type = revise
            Log.e("enter", "revise");
            toolbar();
            initializer_revise(intent);
        }else{
            //type = new
            Log.e("enter", "new");
            toolbar();
            initializer();
        }
    }
    public void initializer(){
        flashcard_db_id="null";
        selected_exam_name = "null";
        selected_exam_code = "null";
        minor_type = "all";
        subject_name="전체";
        subject_code="0";
        subject_number="all_subjects";


        listView = (ListView) findViewById(R.id.flashcardwrite_listView);
        flashcardwriteList = new ArrayList<FlashCardWriteList>();
        flashCardWriteListAdapter = new FlashCardWriteListAdapter(this, flashcardwriteList);
        listView.setAdapter(flashCardWriteListAdapter);
        fab = (FloatingActionButton) findViewById(R.id.floating_action_button);
        fab.attachToListView(listView);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listView.smoothScrollToPosition(0);
            }
        });

        header_jsonObject = new JSONObject();
        jsonObjectTotal = new JSONObject();

        headerContent();
        uploadButtonProcessDeco("new", "작성");
        addFlashCardContainer();
    }
    public void initializer_revise(Intent intent){
        subject_code="0";
        listView = (ListView) findViewById(R.id.flashcardwrite_listView);
        flashcardwriteList = new ArrayList<FlashCardWriteList>();
        flashCardWriteListAdapter = new FlashCardWriteListAdapter(this, flashcardwriteList);
        listView.setAdapter(flashCardWriteListAdapter);
        fab = (FloatingActionButton) findViewById(R.id.floating_action_button);
        fab.attachToListView(listView);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listView.smoothScrollToPosition(0);
            }
        });
        header_jsonObject = new JSONObject();
        jsonObjectTotal = new JSONObject();
        flashcard_db_id = intent.getStringExtra("flashcard_db_id");
        getSelectedFlashCard(flashcard_db_id);

        uploadButtonProcessDeco("revise", "수정");

    }
    public void toolbar(){
        Toolbar tb = (Toolbar) findViewById(R.id.FlashCardWrite_Toolbar);
        tb.setElevation(5);
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_close);
        getSupportActionBar().setTitle("");  //해당 액티비티의 툴바에 있는 타이틀을 공백으로 처리
    }
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_flashcard_container_add, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.flashcard_Add){
            addFlashCardContainer();
        }else{
            onBackPressed();
        }
        return true;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_CODE_SELECT_EXAM) {
            if (resultCode == RESULT_OK) {
//                selected_exam_name = data.getStringExtra("exam_name");
//                selected_exam_code = data.getStringExtra("exam_code");
//                flashcard_select_exam_button.setText("시험 선택 : "+selected_exam_name);
//                try {
//                    jsonObjectTotal.put("exam_name", selected_exam_name);
//                    jsonObjectTotal.put("exam_code", selected_exam_code);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }

            }
        }else if(requestCode == RESULT_CODE_SELECT_SUBJECT){
            if (resultCode == RESULT_OK) {
//                subject_name = data.getStringExtra("subject_name");
//                subject_code = data.getStringExtra("subject_code");
//                subject_number = data.getStringExtra("subject_number");
//                flashcard_select_minor_type.setText("과목 선택 : "+subject_name);
//                try {
//                    jsonObjectTotal.put("subject_number", subject_number);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
            }
        }
    }
    @Override
    public void onBackPressed() {
//        Session.getCurrentSession().removeCallback(callback);
        super.onBackPressed();  // optional depending on your needs
        setResult(RESULT_CANCELED);
        finish();
        overridePendingTransition(R.anim.slide_right_bit, R.anim.slide_out); // 처음이 앞으로 들어올 activity 두번째가 현재 activity 가 할 애니매이션
    }


    // ************************      new write flashcard ***************************
    public void addFlashCardContainer(){
        FlashCardWriteList elements = new FlashCardWriteList("", "");
        flashcardwriteList.add(elements);
        flashCardWriteListAdapter.notifyDataSetChanged();
        listView.smoothScrollToPosition(flashCardWriteListAdapter.getCount());
    }
    public void headerContent(){
        View headerView = getLayoutInflater().inflate(R.layout.container_flashcard_write_header, null);
        flashcard_title_editText = (EditText) headerView.findViewById(R.id.flashcard_write_title_editText);
        flashcard_select_minor_type = (Button) headerView.findViewById(R.id.flashcard_select_minor_type);

        flashcard_select_minor_type.setText("- 전체 -");
        flashcard_select_minor_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectAlertDialog(flashcard_select_minor_type);
            }
        });

        listView.addHeaderView(headerView);
    }
    public void selectAlertDialog(final TextView view_category_textView){
        final CharSequence list[] = new CharSequence[]{"#전체", "#공법", "#민사법", "#형사법", "#선택형"};

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(FlashCardWriteActivity.this);
        builder.setTitle("목록 정렬");
        builder.setItems(list, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.e("value is", "" + which);
                switch (which) {
                    case 0:
                        view_category_textView.setText(list[0].toString());
                        minor_type = "all";
                        break;
                    case 1:
                        view_category_textView.setText(list[1].toString());
                        minor_type = "minor_2001";
                        break;
                    case 2:
                        view_category_textView.setText(list[2].toString());
                        minor_type = "minor_2002";
                        break;
                    case 3:
                        view_category_textView.setText(list[3].toString());
                        minor_type = "minor_2003";
                        break;
                    case 4:
                        view_category_textView.setText(list[4].toString());
                        minor_type = "minor_2004";
                        break;
                }
            }
        });
        builder.show();
    }
    public void uploadButtonProcessDeco(final String flashcard_write_type, String button_name){
        final Button upload_button = new Button(this);
        upload_button.setText(button_name);
        upload_button.setBackgroundColor(getResources().getColor(R.color.colorCrimsonRed));
        upload_button.setTextColor(getResources().getColor(R.color.colorWhite));

        listView.addFooterView(upload_button);

        upload_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 1) TITLE( 제목 ) 이 작성되었는지 확인한다.
                // 2) FLASHCARD ( 플레시카드 ) 모든 플래시카드에 빈 공간이 없는지 확인한다.
                // 1) 2) 중 하나라도 EMPTY 가 존재하면 업로드를 ALERT MESSAGE 를 띠운다.
                if(flashcard_title_editText.getText().toString().length() <= 1 ){
                    // 만약 제목의 길이가 1보다 작거나 같으면 ALERT 메시지를 띄워서 업로드를 막는다.
                    String message = "제목을 입력해주세요. 또는 제목을 두 글자 이상 입력해주세요";
                    String confirm_button = "확인";
                    empty_notifier(message, confirm_button);
                }else{
                    if(checkifmissingcontainer()){
                        //모든 플래시카드 컨테이너들에 내용이 들어가있다.
                        JSONArray jsonArray = new JSONArray();
                        for(int i = 0; i < flashcardwriteList.size(); i++){
                            JSONObject jsonObject = new JSONObject();
                            String term = flashcardwriteList.get(i).getTerm();
                            String def = flashcardwriteList.get(i).getDef();
                            try {
                                jsonObject.put("term", changeLineTransform(term));
                                jsonObject.put("def", changeLineTransform(def));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            jsonArray.put(jsonObject);
                        }
                        try {
                            jsonObjectTotal.put("title", changeLineTransform(flashcard_title_editText.getText().toString()));
                            jsonObjectTotal.put("flashcard", jsonArray);
                            if(flashcard_write_type.equals("new")){
                                String message = "플래시카드를 업로드 하시겠습니까?";
                                String positive_message = "확인";
                                String negative_message = "취소";
                                notifier_private_public( message,  positive_message,
                                        negative_message,   jsonObjectTotal, flashcard_write_type);
                            }else{
                                String message = "플래시카드를 수정 하시겠습니까?";
                                String positive_message = "확인";
                                String negative_message = "취소";
                                notifier_private_public( message,  positive_message,
                                        negative_message,   jsonObjectTotal, flashcard_write_type);

                                Toast.makeText(FlashCardWriteActivity.this, "수정", Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else{
                        //플래시카드 컨테이너에 "빈" 곳이있다.
                        String message = "내용이 없는 플래시카드가 존재합니다. 내용을 입력해주세요.";
                        String confirm_button = "확인";
                        empty_notifier( message,  confirm_button);
                    }
                }
            }
        });
    }
    public void notifier_private_public(String message, String positive_message,
                                        String negative_message, final JSONObject jsonObjectTotal, final String flashcard_write_type){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setPositiveButton(positive_message, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        uploadWrittenFlashCard(jsonObjectTotal, flashcard_write_type);
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

    public boolean checkifmissingcontainer(){
        boolean isNotEmpty = true;
        for(int i = 0; i < flashcardwriteList.size(); i++){
            String term = flashcardwriteList.get(i).getTerm();
            String def = flashcardwriteList.get(i).getDef();
            if(term.length() <=0 || def.length() <=0){
                isNotEmpty = false;
            }
        }
        return isNotEmpty;
    }
    public void uploadWrittenFlashCard(final JSONObject jsonObject, final String flashcard_write_type){
        final String jsonObject_str = jsonObject.toString();
        RequestQueue queue = Volley.newRequestQueue(FlashCardWriteActivity.this);
//        String url = "http://www.joonandhoon.com/pp/PassPop/android/server/uploadWrittenFlashCard.php";
        String url =base_url+"uploadWrittenFlashCard.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("response :::", response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String access_token = jsonObject.getString("access");
                            if(access_token.equals("valid")){
                                String upload_result = jsonObject.getString("response");
                                if(upload_result.equals("upload_success")){
                                    String mes = "플래시 카드를 성공적으로 업로드 하였습니다.";
                                    String pos_mes = "확인";
                                    notifier_new_revise_confirm(mes, pos_mes);
                                }else if(upload_result.equals("update_success")){
                                    String mes = "플래시 카드를 성공적으로 업데이트 하였습니다.";
                                    String pos_mes = "확인";
                                    notifier_new_revise_confirm(mes, pos_mes);
                                }else{
                                    // upload_ update_ fail
                                }

                            }else if(access_token.equals("invalid")){
                                Toast.makeText(FlashCardWriteActivity.this,"잘못된 접근입니다", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(FlashCardWriteActivity.this,"error", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(getActivity(), "volley error", Toast.LENGTH_LONG).show();
//                        String message = "인터넷 연결 에러.. 다시 한번 시도해 주세요...ㅠ ㅠ";
//                        toast(message);
//                        getExamNameAndCode(input_exam_name); // 인터넷 에러가 났을시 다시 한번 시도한다.
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("token", "passpop");
                params.put("major_type", "all");
                params.put("minor_type", minor_type);
                params.put("login_type", LoginType);
                params.put("user_id", G_user_id);
                params.put("flashcard_data", jsonObject_str);
                params.put("flashcard_write_type", flashcard_write_type);
                params.put("flashcard_db_id", flashcard_db_id);
//                params.put("public_private", public_private);
                return params;
            }
        };
        queue.add(stringRequest);
    }
    // ************************      new write flashcard ***************************


    // ************************      revise write flashcard ***************************
    public void getSelectedFlashCard(final String flashcard_db_id){
//        String url_getSelectedExam = "http://www.joonandhoon.com/pp/PassPop/android/server/getSelectedFlashCard.php";
        String url = base_url+"getFlashcardList.php";
        RequestQueue queue = Volley.newRequestQueue(FlashCardWriteActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.e("flashcard_response", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String access_token = jsonObject.getString("access");
                            if(access_token.equals("valid")){
                                JSONObject object = jsonObject.getJSONObject("response1");
                                String primary_key = object.getString("primary_key");
                                String major_type = object.getString("major_type");
                                String g_minor_type = object.getString("minor_type");
                                String minor_type_kor = object.getString("minor_type_kor");
                                String flashcard_title = object.getString("flashcard_title");
                                String user_login_type = object.getString("user_login_type");
                                String user_id = object.getString("user_id");
                                String user_nickname = object.getString("user_nickname");
                                String flashcard_count = object.getString("flashcard_count");

                                minor_type = g_minor_type;



                                revise_headerContent( flashcard_title, minor_type_kor,  minor_type);
                                JSONObject flashcard_data_object = new JSONObject(Html.fromHtml(object.getString("flashcard_data")).toString());
                                JSONArray flashcard_json = flashcard_data_object.getJSONArray("flashcard");

                                for(int i = 0 ; i<flashcard_json.length(); i++){
                                    String term = flashcard_json.getJSONObject(i).getString("term");
//                                    String definition = flashcard_json.getJSONObject(i).getString("definition");
                                    String definition = flashcard_json.getJSONObject(i).getString("def");
                                    Log.e("term ::", term+"/ def ::"+definition);
                                    FlashCardWriteList list = new FlashCardWriteList(term.replace("<br>", "\n"), definition.replace("<br>", "\n"));
                                    flashcardwriteList.add(list);
                                }
                                flashCardWriteListAdapter.notifyDataSetChanged();
                            }else if(access_token.equals("invalid")){

                            }else{

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
                params.put("type", "selected");
                params.put("primary_key", flashcard_db_id);
                return params;
            }
        };
        queue.add(stringRequest);
    }
    public void revise_headerContent(String title, String minor_type_kor, String minor_type){
        View headerView = getLayoutInflater().inflate(R.layout.container_flashcard_write_header, null);
        flashcard_title_editText = (EditText) headerView.findViewById(R.id.flashcard_write_title_editText);
        flashcard_select_minor_type = (Button) headerView.findViewById(R.id.flashcard_select_minor_type);

        flashcard_title_editText.setText(title);

        flashcard_select_minor_type.setText(minor_type_kor);
        flashcard_select_minor_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectAlertDialog(flashcard_select_minor_type);
            }
        });

        listView.addHeaderView(headerView);

    }
    // ************************      revise write flashcard ***************************







    public void notifier_new_revise_confirm(String message, String positivie_message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setPositiveButton(positivie_message, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        setResult(RESULT_OK);
                        finish();
                        overridePendingTransition(R.anim.slide_right_bit, R.anim.slide_out); // 처음이 앞으로 들어올 activity 두번째가 현재 activity 가 할 애니매이션
                    }
                })
                .create()
                .show();
    }
    public void empty_notifier(String message, String confirm_button){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setPositiveButton(confirm_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create()
                .show();
    }
    public String changeLineTransform(String input_str){
        String output_str = input_str.replace("\n", "<br>");
        return output_str;
    }
    public void slide_left_and_slide_in(){//opening new activity
        overridePendingTransition(R.anim.slide_in, R.anim.slide_left_bit); // 처음이 앞으로 들어올 activity 두번째가 현재 activity 가 할 애니매이션
    }
}
