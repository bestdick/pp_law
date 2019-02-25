package com.storyvendingmachine.www.pp_law;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class ExamResultTypeAActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_result_type_a);
        initializer();
    }

    public void initializer(){
        Intent intent = getIntent();
        String exam_placed_year = intent.getStringExtra("exam_placed_year");
        String major_type = intent.getStringExtra("major_type");
        String minor_type = intent.getStringExtra("minor_type");
        String exam_data = intent.getStringExtra("exam_data");
        ArrayList<Integer> user_selected_answer = intent.getIntegerArrayListExtra("UserSelectedAnswer");

        TextView exam_title_textView = (TextView) findViewById(R.id.exam_title_textView);
        ProgressBar percent_bar = (ProgressBar) findViewById(R.id.percent_bar);
        TextView percent_textView = (TextView) findViewById(R.id.percent_textView);
        TextView fraction_textView = (TextView) findViewById(R.id.fraction_textView);
        exam_title_textView.setText(exam_placed_year+"년도 "+ major_minor_selector(major_type)+ "  "+ major_minor_selector(minor_type));
        calculateResult(exam_data, user_selected_answer, percent_bar, percent_textView, fraction_textView);
    }

    public void calculateResult(String exam_data, ArrayList<Integer> user_selected_answer, ProgressBar percent_bar, TextView percent_textView, TextView fraction_textView){
        try {
            JSONArray jsonArray = new JSONArray(exam_data);
            int total_question = jsonArray.length();
            int total_correct = 0 ;
            for(int i = 0 ; i < jsonArray.length(); i++){
                String correct_answer = jsonArray.getJSONObject(i).getString("correct_answer");
                int correct_answer_int = Integer.parseInt(correct_answer);
                if(correct_answer_int == user_selected_answer.get(i)){
                    total_correct ++;
                }
                float float_correct = total_correct;
                float float_total_question = total_question;
                float float_percent = (float_correct/float_total_question)*100;
                String percent_str =  String.format("%.2f", float_percent);

                percent_bar.setProgress((int) float_percent);
                percent_textView.setText(percent_str+" %");
                fraction_textView.setText(total_correct + " / "+ total_question);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String major_minor_selector(String input){
        switch (input){
            case "major_1001":
                return "기록형";
            case "major_1002":
                return "사례형";
            case "major_1003":
                return "선택형";
            case "minor_2001":
                return "공법";
            case "minor_2002":
                return "민사법";
            case "minor_2003":
                return "형사법";
            case "minor_2004":
                return "선택형";
                default:
                    return null;
        }
    }



    public class ExamResult_BackgroundTask extends AsyncTask<Void, Void, String> {
        String exam_data;
        ArrayList<Integer> user_selected_answer;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids){

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
