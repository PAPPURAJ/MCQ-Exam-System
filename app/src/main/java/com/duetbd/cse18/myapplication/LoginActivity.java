package com.duetbd.cse18.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.Printer;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.duetbd.cse18.myapplication.admin.AdminMainActivity;
import com.duetbd.cse18.myapplication.user.UserMainActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    private DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private ProgressBar progressLogin,progressSignup;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sp=getSharedPreferences("Login", MODE_PRIVATE);
        progressLogin=findViewById(R.id.loginProgress);
        progressLogin.setVisibility(View.INVISIBLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
      //   startActivity(new Intent(getApplicationContext(),AdminMainActivity.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser=mAuth.getCurrentUser();
        if(firebaseUser!=null){
            if(sp.getBoolean("admin",false))
                startActivity(new Intent(getApplicationContext(), AdminMainActivity.class));
            else
                startActivity(new Intent(getApplicationContext(), UserMainActivity.class));
            finish();
        }
    }

    public void createAccountClick(View view){
        setContentView(R.layout.activity_signup);
        progressSignup=findViewById(R.id.signupProgress);
        progressSignup.setVisibility(View.INVISIBLE);

    }

    public void signUpClick(View view){

        
        final String name,mail,phone,ins,pass;
        boolean isAdmin=((Spinner)findViewById(R.id.signup_isAdminSp)).getSelectedItem().toString().equals("Admin")?true:false;
        name=((EditText)findViewById(R.id.signup_nameEt)).getText().toString();
        mail=((EditText)findViewById(R.id.signup_emailEt)).getText().toString();
        phone=((EditText)findViewById(R.id.signup_phoneEt)).getText().toString();
        ins=((EditText)findViewById(R.id.signup_instituteEt)).getText().toString();
        pass=((EditText)findViewById(R.id.signup_passEt)).getText().toString().toLowerCase().replace(" ","");;
        

        if(name.equals("") || mail.equals("") || phone.equals("") || ins.equals("") || pass.equals("")){
            Toast.makeText(getApplicationContext(),"Please input first",Toast.LENGTH_SHORT).show();
            return;
        }
        if(pass.length()<8){
            Toast.makeText(getApplicationContext(),"Minimum password length is 8",Toast.LENGTH_SHORT).show();
            return;
        }

        progressSignup.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(mail, pass)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(getApplicationContext(),"User registered: "+user.getEmail(),Toast.LENGTH_SHORT).show();
                        databaseReference.child("Users").push().setValue(new MyUserData(name,mail,phone,ins,isAdmin));
                        progressSignup.setVisibility(View.INVISIBLE);
                        alreadyAccountClick(null);
                    } else {
                        Toast.makeText(LoginActivity.this, "You already created an account!",
                                Toast.LENGTH_SHORT).show();
                        progressSignup.setVisibility(View.INVISIBLE);
                    }

                });


    }

    public void alreadyAccountClick(View view){
        setContentView(R.layout.activity_login);
        progressLogin=findViewById(R.id.loginProgress);
        progressLogin.setVisibility(View.INVISIBLE);
    }

    public void loginClick(View view){

        EditText emailEt=findViewById(R.id.loginEmailEt);


        final String email, pass;
        email=emailEt.getText().toString().replace(" ","").toLowerCase();
        pass=((EditText)findViewById(R.id.loginPassEt)).getText().toString();

        if(email.equals("") || pass.equals(""))
        {
            Toast.makeText(getApplicationContext(),"Login failed!",Toast.LENGTH_SHORT).show();
            return;
        }


        progressLogin.setVisibility(View.VISIBLE);


        mAuth.signInWithEmailAndPassword(email,pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(getApplicationContext(),"Login Success!",Toast.LENGTH_SHORT).show();
                FirebaseDatabase.getInstance().getReference("Users").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        MyUserData myUserData=snapshot.getValue(MyUserData.class);
                        if(email.equals(myUserData.getEmail())){
                            Log.e("=====",myUserData.isAdmin()?"Admin":"User");
                            sp.edit().putBoolean("admin",myUserData.isAdmin()).apply();
                            if(myUserData.isAdmin())
                                startActivity(new Intent(getApplicationContext(), AdminMainActivity.class));
                            else
                                startActivity(new Intent(getApplicationContext(), UserMainActivity.class));
                            progressLogin.setVisibility(View.INVISIBLE);
                            finish();
                        }

                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Failed!",Toast.LENGTH_SHORT).show();
                progressLogin.setVisibility(View.INVISIBLE);
            }
        });
    }


    public void forgotPassClick(View view) {
        final EditText forgetMail=new EditText(this);
        forgetMail.setHint("Email");
        forgetMail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        String inputMail=((EditText)findViewById(R.id.loginEmailEt)).getText().toString();
        if(!inputMail.equals(""))
            forgetMail.setText(inputMail);

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Reset password")
                .setView(forgetMail)
                .setNegativeButton("Cancel",null)
                .setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String m=forgetMail.getText().toString().replace(" ","");

                        if(m.equals("")){
                            Toast.makeText(getApplicationContext(),"Please input first!",Toast.LENGTH_SHORT).show();
                            return;
                        }

                        mAuth.sendPasswordResetEmail(m).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(),"Reset link sent to your email!",Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(),"Password reset failed!",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).show();




    }
}