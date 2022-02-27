package com.duetbd.cse18.myapplication.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.duetbd.cse18.myapplication.LoginActivity;
import com.duetbd.cse18.myapplication.QusData;
import com.duetbd.cse18.myapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class AdminMainActivity extends AppCompatActivity {

    private String firebaseField="Questions";

    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private AdminListAdapter adapter;
    private ArrayList<QusData> arrayList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        fab=findViewById(R.id.admin_mainFloatingButton);

        recyclerView=findViewById(R.id.admin_mainRecycler);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new AdminListAdapter(this,arrayList);
        recyclerView.setAdapter(adapter);

        fab.setOnClickListener(view -> {
           addQuestion();
        });

        loadData();
    }

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


    void addQuestion(){
        View view=LayoutInflater.from(this).inflate(R.layout.single_addqus,null);

        EditText qusEt=view.findViewById(R.id.addQusEt);
        EditText op1Et=view.findViewById(R.id.addQusOp1Et);
        EditText op2Et=view.findViewById(R.id.addQusOp2Et);
        EditText op3Et=view.findViewById(R.id.addQusOp3Et);
        EditText op4Et=view.findViewById(R.id.addQusOp4Et);

        Spinner spinner=view.findViewById(R.id.addQusAns);


        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setView(view)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String qus=qusEt.getText().toString();
                        String op1=op1Et.getText().toString();
                        String op2=op2Et.getText().toString();
                        String op3=op3Et.getText().toString();
                        String op4=op4Et.getText().toString();


                        if(qus.isEmpty() || op1.isEmpty() || op2.isEmpty() || op3.isEmpty() || op4.isEmpty())
                        {
                            Toast.makeText(getApplicationContext(),"Please enter first!",Toast.LENGTH_SHORT).show();
                            return;
                        }

                        String ans="";

                        switch (spinner.getSelectedItem().toString()){
                            case "1":
                                ans=op1;
                                break;
                            case "2":
                                ans=op2;
                                break;
                            case "3":
                                ans=op3;
                                break;
                            case "4":
                                ans=op4;
                                break;
                        }

                        QusData qusData=new QusData(null,qus,op1,op2,op3,op4,ans);

                        FirebaseFirestore.getInstance().collection(firebaseField)
                                .add(qusData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference docRef) {
                                Toast.makeText(getApplicationContext(), "Question added!", Toast.LENGTH_SHORT).show();
                                adapter.addItem(qusData);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "Question not added!", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }).setNegativeButton("Cancel",null)
                .create().show();

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
                        String ans=qns.getDocuments().get(i).getString("ans");

                        QusData myUserData=new QusData(
                                qns.getDocuments().get(i).getReference(),
                                qus,
                                op1,
                                op2,
                                op3,
                                op4,
                                ans
                        );

                        arrayList.add(myUserData);
                    }

                adapter.setData(arrayList);
                });
    }
}