package com.example.loginregistration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Registration extends AppCompatActivity {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://gps-system-eeb27-default-rtdb.firebaseio.com/");
    private FirebaseAuth mAuth;
    private EditText editFullName, editEmail, editphone, editpassword, editconpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //final EditText fullname = findViewById( R.id.fullname);
       // final EditText email = findViewById(R.id.email);
        //final EditText phone = findViewById( R.id.phone);
       // final EditText password = findViewById(R.id.password);
        //final EditText conpassword = findViewById(R.id.conpassword);
        editFullName = (EditText) findViewById(R.id.fullname);
        editEmail = (EditText) findViewById(R.id.email);
        editphone = (EditText) findViewById(R.id.phone);
        editpassword = (EditText) findViewById(R.id.password);
        editconpassword = (EditText) findViewById(R.id.conpassword);


        mAuth = FirebaseAuth.getInstance();

        final Button RegisterBtn = findViewById(R.id.RegisterBtn);
        final TextView LoginNowBtn = findViewById(R.id.LoginNow);



        LoginNowBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });
    }
    public void RegisterUser(View view) {

        registerUser();
    }
    private void registerUser(){
        String email = editEmail.getText().toString().trim();
        String fullname = editFullName.getText().toString().trim();
        String phone = editphone.getText().toString().trim();
        String password = editpassword.getText().toString().trim();

        if(fullname.isEmpty()){
            editFullName.setError("Full name is required ");
            editFullName.requestFocus();
            return;
        }
        if(email.isEmpty()){
            editEmail.setError("Email is Required");
            editEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editEmail.setError("Please provide valid email");
            editEmail.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            editpassword.setError("Password is required");
            editpassword.requestFocus();
            return;
        }
        if (password.length() < 6) {
            editpassword.setError("password has to be more than 6 charcters");
            editpassword.requestFocus();
            return;
        }
        if (phone.isEmpty()) {
            editphone.setError("Phone number is required");
            editphone.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    User user = new User(fullname,email,phone,password);

                    FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(Registration.this, "You have been registred succsesfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Registration.this,Login.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(Registration.this, "Failed to register", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }
}