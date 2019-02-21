package com.storyvendingmachine.www.pp_law;

/**
 * Created by Administrator on 2019-02-21.
 */

public class ExamListTypeA {
    String identifier;
    String exam_placed_year;
    String minor_type;
    String minor_type_kor;
    int count_questions;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getExam_placed_year() {
        return exam_placed_year;
    }

    public void setExam_placed_year(String exam_placed_year) {
        this.exam_placed_year = exam_placed_year;
    }

    public String getMinor_type() {
        return minor_type;
    }

    public void setMinor_type(String minor_type) {
        this.minor_type = minor_type;
    }

    public String getMinor_type_kor() {
        return minor_type_kor;
    }

    public void setMinor_type_kor(String minor_type_kor) {
        this.minor_type_kor = minor_type_kor;
    }

    public int getCount_questions() {
        return count_questions;
    }

    public void setCount_questions(int count_questions) {
        this.count_questions = count_questions;
    }

    public ExamListTypeA(String identifier, String exam_placed_year, String minor_type, String minor_type_kor, int count_questions) {
        this.identifier = identifier;
        this.exam_placed_year = exam_placed_year;
        this.minor_type = minor_type;
        this.minor_type_kor = minor_type_kor;
        this.count_questions = count_questions;
    }
}
