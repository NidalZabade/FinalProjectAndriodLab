package edu.birzeit.nidlibraheem.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import edu.birzeit.nidlibraheem.finalproject.models.User;

public class SignUpActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        EditText emailTF = findViewById(R.id.signupEmail);
        EditText firstNameTF = findViewById(R.id.firstName);
        EditText lastNameTF = findViewById(R.id.lastName);
        EditText passwordTF = findViewById(R.id.signupPassword);
        EditText confirmPasswordTF = findViewById(R.id.signupConfirm);
        Button signupButton = findViewById(R.id.signupButton);
        TextView loginText = findViewById(R.id.loginText_signup);
        TextView signupText = findViewById(R.id.signupText_signup);

        ORM orm = ORM.getInstance(this);


        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                create a user object in try catch

                String email = emailTF.getText().toString();
                String firstName = firstNameTF.getText().toString();
                String lastName = lastNameTF.getText().toString();
                String password = passwordTF.getText().toString();
                String confirmPassword = confirmPasswordTF.getText().toString();

                boolean error_flag = false;

//                make sure no fields are empty

                if( email.isEmpty() ) {
                    emailTF.setError("This field is required");
                    error_flag = true;
                }
                if( firstName.isEmpty() ) {
                    firstNameTF.setError("This field is required");
                    error_flag = true;
                }

                if( lastName.isEmpty() ) {
                    lastNameTF.setError("This field is required");
                    error_flag = true;
                }
                if ( password.isEmpty() ) {
                    passwordTF.setError("This field is required");
                    error_flag = true;
                }
                if ( confirmPassword.isEmpty() ) {
                    confirmPasswordTF.setError("This field is required");
                    error_flag = true;
                }

                try {
                    User.validateEmail(email);
                } catch (IllegalArgumentException e) {
                    emailTF.setError(e.getMessage());
                    error_flag = true;
                }

                try {
                    User.validateName(firstName, "First Name");
                } catch (IllegalArgumentException e) {
                    firstNameTF.setError(e.getMessage());
                    error_flag = true;

                }

                try {
                    User.validateName(lastName, "Last Name");
                } catch (IllegalArgumentException e) {
                    lastNameTF.setError(e.getMessage());
                    error_flag = true;

                }

                try {
                    User.validatePassword(password, confirmPassword);
                } catch (IllegalArgumentException e) {
                    passwordTF.setError(e.getMessage());
                    confirmPasswordTF.setError(e.getMessage());
                    error_flag = true;
                }

                if (error_flag) {
                    return;
                }

                User user = new User(email, firstName, lastName, password, confirmPassword);

                long id = orm.insertUser(user);

                if (id == -1) {
                    emailTF.setError("User with this email already exists");
                    return;
                }

                MainActivity.loggedInUser = user;

                Toast toast = Toast.makeText(getApplicationContext(), "Welcome " + firstName, Toast.LENGTH_SHORT);

                Intent intent = new Intent(SignUpActivity.this, NotesActivity.class);
                startActivity(intent);

            }
        });


    }
}