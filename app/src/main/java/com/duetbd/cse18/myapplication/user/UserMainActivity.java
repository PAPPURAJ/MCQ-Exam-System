package com.duetbd.cse18.myapplication.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.duetbd.cse18.myapplication.LoginActivity;
import com.duetbd.cse18.myapplication.QusData;
import com.duetbd.cse18.myapplication.R;
import com.duetbd.cse18.myapplication.admin.AdminListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class UserMainActivity extends AppCompatActivity {

    private String firebaseField="Questions";
    private ArrayList<QusData> arrayList=new ArrayList<>();
    private int count=0;
    private TextView totalCountTv;
    RadioButton r1,r2,r3,r4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        totalCountTv=findViewById(R.id.userQusCountTv);
        r1=findViewById(R.id.UserRadio1);
        r2=findViewById(R.id.UserRadio2);
        r3=findViewById(R.id.UserRadio3);
        r4=findViewById(R.id.UserRadio4);

        loadData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.menu_logout){
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(getApplicationContext(),"Signed out!",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        return super.onOptionsItemSelected(item);

    }



    void loadData(){
        FirebaseFirestore.getInstance().collection(firebaseField)
                .get().addOnSuccessListener(qns -> {
            arrayList.clear();

            for(int i=0;i<qns.size();i++){
                String qus=qns.getDocuments().get(i).getString("question");
                String op1=qns.getDocuments().get(i).getString("option1");
                String op2=qns.getDocuments().get(i).getString("option2");
                String op3=qns.getDocuments().get(i).getString("option3");
                String op4=qns.getDocuments().get(i).getString("option4");

                QusData myUserData=new QusData(
                        qns.getDocuments().get(i).getReference(),
                        qus,
                        op1,
                        op2,
                        op3,
                        op4
                );

                arrayList.add(myUserData);
            }

        });
    }

    public void nextQusClick(View view) {

        count++;

        if(count>arrayList.size()){
            count=0;
        }
        totalCountTv.setText(count+"/"+arrayList.size());


    }

    public void prevQusClick(View view) {
        count--;

        if(count<0){
            count=arrayList.size();
        }
        totalCountTv.setText(count+"/"+arrayList.size());
    }

    public void submitClick(View view) {
        String selectedStr=(r1.isSelected()?r1.getText().toString():"")
                +(r2.isSelected()?r2.getText().toString():"")
                +(r3.isSelected()?r3.getText().toString():"")
                +(r4.isSelected()?r4.getText().toString():"");

        Toast.makeText(getApplicationContext(), selectedStr, Toast.LENGTH_SHORT).show();

    }
}