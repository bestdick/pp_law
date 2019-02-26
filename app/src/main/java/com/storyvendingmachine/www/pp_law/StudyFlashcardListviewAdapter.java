package com.storyvendingmachine.www.pp_law;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Administrator on 2019-02-21.
 */

public class StudyFlashcardListviewAdapter extends BaseAdapter {
    private Context context;
    private List<StudyFlashcardList> list;


    public StudyFlashcardListviewAdapter(Context context, List<StudyFlashcardList> list) {
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

        TextView number_textView = v.findViewById(R.id.number_textView);
        TextView title_textView = v.findViewById(R.id.title_textView);
        TextView first_term_textView = v.findViewById(R.id.first_term_textView);
        TextView date_textView = v.findViewById(R.id.date_textView);

        ImageView author_thumbnail_imageView = v.findViewById(R.id.author_thumbnail_imageView);
        TextView author_textView = v.findViewById(R.id.author_textView);
        TextView hit_count_textView = v.findViewById(R.id.hit_count_textView);


        final String primary_key = list.get(i).getFlashcard_db_id();
        String title = list.get(i).getTitle();
        String date = list.get(i).getUpload_date();
        String first_term = list.get(i).getFlashcard_first_term();

        String author_nickname = list.get(i).getUser_nickname();
        String author_thumbnail = list.get(i).getUser_thumbnail();
        String hit_count = list.get(i).getHit_count();

        getThumbnailImageForAuthor(author_thumbnail_imageView, author_thumbnail);
        number_textView.setText("Flash Card "+(i+1));
        title_textView.setText(title);
        first_term_textView.setText(first_term);
        date_textView.setText(date);
        author_textView.setText(author_nickname);
        hit_count_textView.setText(hit_count);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, StudyFlashcardViewActivity.class);
                intent.putExtra("type", "regular");
                intent.putExtra("primary_key", primary_key);

//                ((MainActivity) context).startActivityForResult(intent, REQUEST_CODE_FOR_FLASHCARDFRAGMENT);
                context.startActivity(intent);
                slide_left_and_slide_in();
            }
        });

        return v;
    }
    public void slide_left_and_slide_in(){
        ((MainActivity) context).overridePendingTransition(R.anim.slide_in, R.anim.slide_left_bit); // 처음이 앞으로 들어올 activity 두번째가 현재 activity 가 할 애니매이션
    }

    public void getThumbnailImageForAuthor(ImageView imageView, String url){
        Picasso.with(context)
                .load(url)
                .transform(new CircleTransform())
                .into(imageView, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {

                    }
                    @Override
                    public void onError() {
                        Log.e("load image", "fail to load images ");
                    }
                });
    }
}
