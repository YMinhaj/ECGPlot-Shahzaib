package com.ssuet.yousufminhaj.ecg;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.ssuet.yousufminhaj.ecg.R.id.register;

public class register extends AppCompatActivity {
    Button Register;
    FirebaseAuth fauth;
    DatabaseReference dbref;
    EditText fname,lname,email,pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }

    private void init() {
        Register = (Button) findViewById(register);
        fname = (EditText) findViewById(R.id.editText2);
        lname = (EditText) findViewById(R.id.editText3);
        email = (EditText) findViewById(R.id.editText4);
        pass = (EditText) findViewById(R.id.editText6);
        fauth = FirebaseAuth.getInstance();
        listner();

    }

    private void listner() {
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fauth.createUserWithEmailAndPassword(email.getText().toString(),pass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            dbref = FirebaseDatabase.getInstance().getReferenceFromUrl("https://ecgplot.firebaseio.com/");
                            //DatabaseReference dbr = dbref.push();
                            FirebaseData fd =new  FirebaseData(fname.getText().toString(),lname.getText().toString(),email.getText().toString(),pass.getText().toString());
                            dbref.child("Member").push().setValue(fd);
                            Toast.makeText(register.this, "Created", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(register.this,thnks.class);
                            startActivity(i);
                        }
                        else
                        {
                            Toast.makeText(register.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
