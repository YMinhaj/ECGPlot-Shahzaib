package com.ssuet.yousufminhaj.ecg;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.InterpolatorRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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

import java.util.List;


public class MainActivity extends AppCompatActivity {
    Button Login,Register;
    EditText name,pass;
    FirebaseAuth fauth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        Login= (Button) findViewById(R.id.login);
        Register = (Button) findViewById(R.id.register);
        name= (EditText) findViewById(R.id.editText);
        pass = (EditText) findViewById(R.id.pass);
        fauth = FirebaseAuth.getInstance();
        listner();

    }

    private void listner() {
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fauth.signInWithEmailAndPassword(name.getText().toString(),pass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            // some where
                            Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(MainActivity.this,enter_details_and_exit.class);
                            startActivity(i);

                        }
                        else
                        {
                            Toast.makeText(MainActivity.this, "Invalid", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,register.class);
                startActivity(i);
            }
        });
    }
}