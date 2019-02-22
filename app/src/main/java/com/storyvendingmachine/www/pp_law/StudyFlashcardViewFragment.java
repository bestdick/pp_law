package com.storyvendingmachine.www.pp_law;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

//
///**
// * A simple {@link Fragment} subclass.
// * Activities that contain this fragment must implement the
// * {@link StudyFlashcardViewFragment.OnFragmentInteractionListener} interface
// * to handle interaction events.
// * Use the {@link StudyFlashcardViewFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
public class StudyFlashcardViewFragment extends Fragment {

    int page, total_length;
    String term_and_def, flashcard_or_folder;
    boolean solo_page;

    public static StudyFlashcardViewFragment newInstance(int count, String term_and_def,  boolean solo_page, String flashcard_or_folder, int total_length) {
        StudyFlashcardViewFragment fragment = new StudyFlashcardViewFragment();
        Bundle args = new Bundle();
        args.putInt("page", count);
        args.putString("term_and_def", term_and_def);
        args.putBoolean("solo_page", solo_page);
        args.putString("flashcard_or_folder", flashcard_or_folder);
        args.putInt("total_length", total_length);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setHasOptionsMenu(true);
        page = getArguments().getInt("page");
        term_and_def =getArguments().getString("term_and_def");
        solo_page = getArguments().getBoolean("solo_page");
        flashcard_or_folder = getArguments().getString("flashcard_or_folder");
        total_length = getArguments().getInt("total_length");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(page%2 == 0){
            //cover flashcard
            View rootView = inflater.inflate(R.layout.fragment_study_flashcard_view, container, false);
            TextView term_and_def_textView = (TextView) rootView.findViewById(R.id.term_and_def_textView);
            TextView page_textView = (TextView) rootView.findViewById(R.id.flashcard_page_textView);
            TextView exam_name_textView = (TextView) rootView.findViewById(R.id.exam_name_textView);

            page_textView.setText(String.valueOf((page/2)+1)+" / "+(total_length/2));
            term_and_def_textView.setGravity(Gravity.CENTER);
            term_and_def_textView.setText(term_and_def.replace("<br>", "\n"));
//            exam_name_textView.setText(flashcard_exam_name + " "+ flashcard_subject_name);
            return rootView;
        }else{
            //back flashcard
            View rootView = inflater.inflate(R.layout.fragment_study_flashcard_view_odd, container, false);
            TextView term_and_def_textView = (TextView) rootView.findViewById(R.id.term_and_def_textView);

            term_and_def_textView.setGravity(Gravity.LEFT);
            term_and_def_textView.setText(term_and_def.replace("<br>", "\n"));
            return rootView;
        }
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
