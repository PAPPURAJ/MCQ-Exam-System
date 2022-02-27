package com.duetbd.cse18.myapplication;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;

public class QusData {
    private DocumentReference reference;
    private String Question, option1,option2,option3,option4,ans;

    public QusData() {
    }

    public QusData(DocumentReference reference, String Question, String option1, String option2, String option3, String option4,String ans) {
        this.reference = reference;
        this.Question = Question;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.ans=ans;
    }


    public void setReference(DocumentReference reference) {
        this.reference = reference;
    }

    public DocumentReference getReference() {
        return reference;
    }

    public String getQuestion() {
        return Question;
    }

    public String getOption1() {
        return option1;
    }

    public String getOption2() {
        return option2;
    }

    public String getOption3() {
        return option3;
    }

    public String getOption4() {
        return option4;
    }

    public String getAns() {
        return ans;
    }
}
