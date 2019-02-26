package com.storyvendingmachine.www.pp_law;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.storyvendingmachine.www.pp_law.MainActivity.G_user_id;
import static com.storyvendingmachine.www.pp_law.MainActivity.LoginType;
import static com.storyvendingmachine.www.pp_law.MainActivity.REQUEST_CODE_FOR_FLASHCARD_WRITE;
import static com.storyvendingmachine.www.pp_law.UrlBase.base_url;


///**
// * A simple {@link Fragment} subclass.
// * Activities that contain this fragment must implement the
// * {@link StudySubClassFragment.OnFragmentInteractionListener} interface
// * to handle interaction events.
// * Use the {@link StudySubClassFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
public class StudySubClassFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

//    private OnFragmentInteractionListener mListener;
//
//    public StudySubClassFragment() {
//        // Required empty public constructor
//    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StudySubClassFragment.
     */
    // TODO: Rename and change types and number of parameters

    ListView listView;
    StudyFlashcardListviewAdapter studyFlashcardListviewAdapter;
    List<StudyFlashcardList> studyFlashcardLists;

    int total_list_count;
    int page;
    public static StudySubClassFragment newInstance(String param1, String param2) {
        StudySubClassFragment fragment = new StudySubClassFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        total_list_count=0;
        page=0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview =inflater.inflate(R.layout.fragment_study_sub_class, container, false);
        initializer(rootview);
        return rootview;
    }

    public void initializer(View rootview){
        listView = rootview.findViewById(R.id.listView);
        studyFlashcardLists = new ArrayList<StudyFlashcardList>();
        studyFlashcardListviewAdapter= new StudyFlashcardListviewAdapter(getActivity(), studyFlashcardLists);
        listView.setAdapter(studyFlashcardListviewAdapter);
        swiper(rootview);
        getFlashcardList();
        flashcard_list_header();
    }

    public void swiper(View rootview){
        final SwipeRefreshLayout mSwipeRefreshLayout = (SwipeRefreshLayout) rootview.findViewById(R.id.swiper);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page=0;
                studyFlashcardLists.clear();
                getFlashcardList();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }
    public void flashcard_list_header(){
        View header_view = getLayoutInflater().inflate(R.layout.container_flashcard_listview_header, null);
        final TextView view_category_textView = (TextView) header_view.findViewById(R.id.view_category_textView);
        TextView create_flashcard_textView = (TextView) header_view.findViewById(R.id.create_flashcard_textView);
        view_category_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectAlertDialog(view_category_textView);
            }
        });
        create_flashcard_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(LoginType == null|| G_user_id == null){
                    String message = "플래시카드 작성을 하시려면 로그인 후 작성해주시기 바람니다.";
                    String positive_message = "확인";
                    notifier(message, positive_message);
                }else{
                    Intent intent = new Intent(getActivity(), FlashCardWriteActivity.class);
                    intent.putExtra("type", "new");
                    startActivityForResult(intent, REQUEST_CODE_FOR_FLASHCARD_WRITE);
//                    slide_left_and_slide_in();
                }
            }
        });
        listView.addHeaderView(header_view);
    }
    public void notifier(String message, String positive_message){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message)
                .setPositiveButton(positive_message, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create()
                .show();
    }
    private void getFlashcardList(){
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = base_url+"getFlashcardList.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("exam list response", response);
                        try {

                                JSONObject jsonObject = new JSONObject(response);
                                total_list_count = Integer.parseInt(jsonObject.getString("total_count"));
                                JSONArray jsonArray = jsonObject.getJSONArray("response1");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    String primary_key = jsonArray.getJSONObject(i).getString("primary_key");
                                    String major_type = jsonArray.getJSONObject(i).getString("major_type");
                                    String minor_type = jsonArray.getJSONObject(i).getString("minor_type");
                                    String login_type = jsonArray.getJSONObject(i).getString("login_type");
                                    String user_id = jsonArray.getJSONObject(i).getString("user_id");
                                    String user_nickname = jsonArray.getJSONObject(i).getString("user_nickname");
                                    String user_thumbnail = jsonArray.getJSONObject(i).getString("user_thumbnail");
                                    String upload_date = jsonArray.getJSONObject(i).getString("upload_date");
                                    String upload_time = jsonArray.getJSONObject(i).getString("upload_time");
                                    String revised_date = jsonArray.getJSONObject(i).getString("revised_date");
                                    String revised_time = jsonArray.getJSONObject(i).getString("revised_time");
                                    String flashcard_hit = jsonArray.getJSONObject(i).getString("flashcard_hit");
                                    String title = jsonArray.getJSONObject(i).getString("title");
                                    String count = jsonArray.getJSONObject(i).getString("count");
                                    String first_term =jsonArray.getJSONObject(i).getString("first_term");

                                    StudyFlashcardList item = new StudyFlashcardList(primary_key, major_type, null, minor_type, null,
                                            login_type, user_id, user_nickname, user_thumbnail, upload_date, upload_time, null, null, flashcard_hit, null,
                                            title, count, first_term);
                                    studyFlashcardLists.add(item);

                                }
                                studyFlashcardListviewAdapter.notifyDataSetChanged();

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
                params.put("type", "list");
                params.put("sub_type", "recent");
                params.put("page", String.valueOf(page));
                return params;
            }
        };
        queue.add(stringRequest);
    }

    public void selectAlertDialog(final TextView view_category_textView){
        final CharSequence list[] = new CharSequence[]{"#최신순", "#인기순", "#나의카드", "#나의폴더"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("목록 정렬");
        builder.setItems(list, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.e("value is", "" + which);
                switch (which) {
                    case 0:
                        view_category_textView.setText(list[0].toString());
                        break;
                    case 1:
                        view_category_textView.setText(list[1].toString());
                        break;
                    case 2:
                        view_category_textView.setText(list[2].toString());
                        break;
                    case 3:
                        view_category_textView.setText(list[3].toString());
                        break;
                }
            }
        });
        builder.show();
    }


//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
}
