package com.example.loginregistration;

import androidx.annotation.IdRes;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
    //DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://gps-system-eeb27-default-rtdb.firebaseio.com/");
    private EditText editTextEmail, editTextPassword;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //final EditText phone = findViewById(R.id.phone);
        //final EditText password = findViewById( R.id.password);
        final Button LoginBtn = findViewById(R.id.LoginBtn);
        final TextView RegisterNowBtn = findViewById(R.id.RegisterNowBtn);

        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPassword = (EditText) findViewById(R.id.password);
        mAuth = FirebaseAuth.getInstance();



            RegisterNowBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                    public void onClick (View v){
                    startActivity(new Intent(Login.this, Registration.class));
                }

            });

        }

    public void SignIn(View view) {
        String email = editTextEmail.getText().toString().trim();
        String passsword = editTextPassword.getText().toString().trim();

        if(email.isEmpty()){
            editTextEmail.setError("Email is Required");
            editTextEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Enter a Valid Email");
            editTextEmail.requestFocus();
            return;
        }

        if(passsword.isEmpty()){
            editTextPassword.setError("Password is Required");
            editTextPassword.requestFocus();
            return;
        }
        if(passsword.length()<6){
            editTextPassword.setError("Password should be more than 6 characters");
            editTextPassword.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email,passsword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    reference = FirebaseDatabase.getInstance().getReference("Users");
                    userID = user.getUid();

                    if(user.isEmailVerified()){
                        // directiing to activity
                        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                startActivity(new Intent(Login.this,MainActivity.class));
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }else{
                        user.sendEmailVerification();
                        Toast.makeText(Login.this, "Check your Email to verify account", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(Login.this, "Failed to Login", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}