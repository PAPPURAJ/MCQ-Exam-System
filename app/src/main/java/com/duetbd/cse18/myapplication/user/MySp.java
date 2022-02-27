package com.duetbd.cse18.myapplication.user;

import android.content.Context;
import android.content.SharedPreferences;

public class MySp {
    private SharedPreferences sp;

    public MySp(Context context) {
        sp=context.getSharedPreferences("ansDB",Context.MODE_PRIVATE);
    }

    void incRightAns(){
        sp.edit().putInt("right",getRightAns()+1).apply();
    }

    void incWrongAns(){
        sp.edit().putInt("wrong",getWrongAns()+1).apply();
    }

    int getRightAns(){
        return sp.getInt("right",0);
    }
    int getWrongAns(){
        return sp.getInt("wrong",0);
    }

    void resetAns(){
        sp.edit().putInt("right",0).apply();
        sp.edit().putInt("wrong",0).apply();
    }

}
