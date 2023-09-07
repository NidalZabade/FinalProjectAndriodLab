package edu.birzeit.nidlibraheem.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SignUpActivity extends AppCompatActivity {

    EditText email,firstName,lastName,password,confirmPassword;
    Button signupButton;
    TextView loginText, signupText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        email = findViewById(R.id.signupEmail);
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        password = findViewById(R.id.signupPassword);
        confirmPassword = findViewById(R.id.signupConfirm);
        signupButton = findViewById(R.id.signupButton);
        loginText = findViewById(R.id.loginText_signup);
        signupText = findViewById(R.id.signupText_signup);



        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


    }
}