package com.storyvendingmachine.www.pp_law;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

/**
 * Created by Administrator on 2019-02-20.
 */

public class ExamSubClassViewPagerAdapter extends FragmentStatePagerAdapter {

    public ExamSubClassViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Log.e("item pointer", Integer.toString(position));
        if (position == 0) {
            return ExamSubClassFragment.newInstance("major_1001", null);
        } else if(position == 1){
            return ExamSubClassFragment.newInstance("major_1002", null);
        }else{
            return ExamSubClassFragment.newInstance("major_1003", null);
        }
    }
    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }

}
