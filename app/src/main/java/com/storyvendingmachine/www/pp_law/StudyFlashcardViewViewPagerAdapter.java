package com.storyvendingmachine.www.pp_law;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import java.util.ArrayList;

public class StudyFlashcardViewViewPagerAdapter extends FragmentStatePagerAdapter {
    int count;
    ArrayList<String> flashcard_array;
    boolean solo_page;
    String flashcard_or_folder;

    public StudyFlashcardViewViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Log.e("item pointer", Integer.toString(position));
        int length_of_flashcard = flashcard_array.size();
        String temr_and_def = flashcard_array.get(position);
        return StudyFlashcardViewFragment.newInstance(position, temr_and_def, solo_page, flashcard_or_folder, length_of_flashcard);
    }
    @Override
    public int getCount() {
        // Show 3 total pages.
        return count;
    }

}
