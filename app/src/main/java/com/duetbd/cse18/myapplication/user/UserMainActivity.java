package com.duetbd.cse18.myapplication.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

    private String firebaseField = "Questions", radioSelected = "";
    private ArrayList<QusData> arrayList = new ArrayList<>();
    private int count = 0;
    private TextView totalCountTv, qusTv, reportTv;
    private RadioButton r1, r2, r3, r4;
    private RadioGroup radioGroup;
    private int rightAns = 0, wrongAns = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        totalCountTv = findViewById(R.id.userQusCountTv);
        qusTv = findViewById(R.id.singleUserQueEdiText);
        reportTv = findViewById(R.id.userQusReportTv);
        radioGroup = findViewById(R.id.singleUserRadioGroup);
        r1 = findViewById(R.id.UserRadio1);
        r2 = findViewById(R.id.UserRadio2);
        r3 = findViewById(R.id.UserRadio3);
        r4 = findViewById(R.id.UserRadio4);

        loadData();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.UserRadio1:
                        radioSelected = r1.getText().toString();
                        break;
                    case R.id.UserRadio2:
                        radioSelected = r2.getText().toString();
                        break;
                    case R.id.UserRadio3:
                        radioSelected = r3.getText().toString();
                        break;
                    case R.id.UserRadio4:
                        radioSelected = r4.getText().toString();
                        break;

                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_logout) {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(getApplicationContext(), "Signed out!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        return super.onOptionsItemSelected(item);

    }


    void loadData() {
        FirebaseFirestore.getInstance().collection(firebaseField)
                .get().addOnSuccessListener(qns -> {
            arrayList.clear();

            for (int i = 0; i < qns.size(); i++) {
                String qus = qns.getDocuments().get(i).getString("question");
                String op1 = qns.getDocuments().get(i).getString("option1");
                String op2 = qns.getDocuments().get(i).getString("option2");
                String op3 = qns.getDocuments().get(i).getString("option3");
                String op4 = qns.getDocuments().get(i).getString("option4");
                String ans = qns.getDocuments().get(i).getString("ans");

                QusData myUserData = new QusData(
                        qns.getDocuments().get(i).getReference(),
                        qus,
                        op1,
                        op2,
                        op3,
                        op4,
                        ans
                );

                arrayList.add(myUserData);
                refreshQus();
            }

        });
    }

    public void nextQusClick(View view) {

        count++;

        if (count >= arrayList.size()) {
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setTitle("Result sheet")
                    .setMessage("Number of right ans is "+rightAns+"\nNumber of wrong ans is "+wrongAns)
                    .create().show();
            count=rightAns=wrongAns=0;
            reportTv.setText("Right: " + rightAns + "\nWrong: " + wrongAns);

        }
        refreshQus();

    }

    public void prevQusClick(View view) {
        count--;

        if (count < 0) {
            Toast.makeText(getApplicationContext(), "No more question!", Toast.LENGTH_SHORT).show();
            count = 0;
        }

        refreshQus();
    }

    void refreshQus() {
        totalCountTv.setText(count + 1 + "/" + arrayList.size());
        qusTv.setText(arrayList.get(count).getQuestion());
        r1.setText(arrayList.get(count).getOption1());
        r2.setText(arrayList.get(count).getOption2());
        r3.setText(arrayList.get(count).getOption3());
        r4.setText(arrayList.get(count).getOption4());
    }

    public void submitClick(View view) {
        if (arrayList.get(count).getAns().equals(radioSelected))
            rightAns++;
        else
            wrongAns++;

        //Log.e("========", "DB: " + arrayList.get(count).getAns() + "   Spinner: " + radioSelected);

        reportTv.setText("Right: " + rightAns + "\nWrong: " + wrongAns);
        nextQusClick(null);

    }
}