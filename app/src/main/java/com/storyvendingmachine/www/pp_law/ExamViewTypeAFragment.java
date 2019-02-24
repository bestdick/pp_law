package com.storyvendingmachine.www.pp_law;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class ExamViewTypeAFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private Bundle mParam1;
    private int mParam2;

    public static ExamViewTypeAFragment newInstance(Bundle param1, int param2) {
        ExamViewTypeAFragment fragment = new ExamViewTypeAFragment();
        Bundle args = new Bundle();
        args.putBundle(ARG_PARAM1, param1);
        args.putInt(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getBundle(ARG_PARAM1);
            mParam2 = getArguments().getInt(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_exam_view_type_a, container, false);
        intialize_elements(rootview);

        return rootview;
    }
    public void intialize_elements(View rootview){
        TextView question_textView = (TextView) rootview.findViewById(R.id.question_textView);
        TextView example_1_textView = (TextView) rootview.findViewById(R.id.example_1_textView);
        TextView example_2_textView = (TextView) rootview.findViewById(R.id.example_2_textView);
        TextView answer_1_textView = (TextView) rootview.findViewById(R.id.answer_1_textView);
        TextView answer_2_textView = (TextView) rootview.findViewById(R.id.answer_2_textView);
        TextView answer_3_textView = (TextView) rootview.findViewById(R.id.answer_3_textView);
        TextView answer_4_textView = (TextView) rootview.findViewById(R.id.answer_4_textView);
        TextView answer_5_textView = (TextView) rootview.findViewById(R.id.answer_5_textView);

        String question_number = mParam1.getString("question_number");
        String question_context = mParam1.getString("question_context");
        String question_example_1_exist =  mParam1.getString("question_example_1_exist");
        String question_example_1_context = mParam1.getString("question_example_1_context");
        String question_example_2_exist =  mParam1.getString("question_example_2_exist");
        String question_example_2_context = mParam1.getString("question_example_2_context");
        String answer_context = mParam1.getString("answer_context");
        String correct_answer = mParam1.getString("correct_answer");


        make_question_and_example( question_textView,  example_1_textView,  example_2_textView,
                 question_context,  question_example_1_exist,  question_example_1_context,
                question_example_2_exist,  question_example_2_context);
        make_answer_choice( answer_context,  answer_1_textView,  answer_2_textView,  answer_3_textView,
                 answer_4_textView,  answer_5_textView);


    }

    public void make_question_and_example(TextView question_textView, TextView example_1_textView, TextView example_2_textView,
                                          String question_context, String question_example_1_exist, String question_example_1_context,
                                          String question_example_2_exist, String question_example_2_context){
        if(question_example_1_exist.equals("true") && question_example_2_exist.equals("true")){
//            Spanned html_question = Html.fromHtml(question_context);
//            Spanned html_example_1 = Html.fromHtml(question_example_1_context);
//            Spanned html_example_2 = Html.fromHtml(question_example_2_context);
            String str_question = "[ "+(mParam2+1) + " ] "+question_context.replace("<br>","\n");
            String str_example_1 = question_example_1_context.replace("<br>","\n");
            String str_example_2 = question_example_2_context.replace("<br>","\n");

            question_textView.setText(str_question);
            example_1_textView.setText(str_example_1);
            example_2_textView.setText(str_example_2);

        }else if(question_example_1_exist.equals("true") && question_example_2_exist.equals("false")){
//            Spanned html_question = Html.fromHtml(question_context);
//            Spanned html_example_1 = Html.fromHtml(question_example_1_context);
            String str_question = "[ "+mParam2 + " ] "+question_context.replace("<br>","\n");
            String str_example_1 = question_example_1_context.replace("<br>","\n");

            question_textView.setText(str_question);
            example_1_textView.setText(str_example_1);
            example_2_textView.setVisibility(View.GONE);
        }else {
//            question_example_1_exist.equals("false") || question_example_2_exist.equals("false")
//            Spanned html_question = Html.fromHtml(question_context);
            String str_question = "[ "+mParam2 + " ] "+question_context.replace("<br>","\n");

            question_textView.setText(str_question);
            example_1_textView.setVisibility(View.GONE);
            example_2_textView.setVisibility(View.GONE);
        }
    }
    public void make_answer_choice(String answer_context, TextView answer_1_textView, TextView answer_2_textView, TextView answer_3_textView,
                                   TextView answer_4_textView, TextView answer_5_textView){
        String[] answer_array = answer_context.split("##");
        String answer_1 = "①" +answer_array[0];
        String answer_2 = "②" +answer_array[1];
        String answer_3 = "③" +answer_array[2];
        String answer_4 = "④" +answer_array[3];
        String answer_5 = "⑤" +answer_array[4];

        answer_1_textView.setText(answer_1);
        answer_2_textView.setText(answer_2);
        answer_3_textView.setText(answer_3);
        answer_4_textView.setText(answer_4);
        answer_5_textView.setText(answer_5);



    }

}
