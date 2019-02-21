package com.storyvendingmachine.www.pp_law;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2019-02-21.
 */

public class StudyFlashcardListviewAdapter extends BaseAdapter {
    private Context context;
    private List<ExamListTypeA> list;


    public StudyFlashcardListviewAdapter(Context context, List<ExamListTypeA> list) {
        this.context = context;
        this.list= list;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v =View.inflate(context, R.layout.container_flashcard_listview_element, null);
        TextView title_textView = v.findViewById(R.id.title_textView);
        TextView author_textView = v.findViewById(R.id.author_textView);

        return v;
    }
}
