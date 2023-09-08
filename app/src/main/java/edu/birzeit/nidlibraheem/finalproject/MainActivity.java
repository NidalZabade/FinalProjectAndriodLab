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

public class MainActivity extends AppCompatActivity {

    EditText emailTF, passwordTF;
    Button loginButton;
    TextView signupText, loginText;

    public static User loggedInUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        emailTF = findViewById(R.id.email);
        passwordTF = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        loginText = findViewById(R.id.loginText_main);
        signupText = findViewById(R.id.signupText_main);

        signupText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        ORM orm = ORM.getInstance(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailText = emailTF.getText().toString();
                String passwordText = passwordTF.getText().toString();
                if (emailText.isEmpty() || passwordText.isEmpty()) {
                    passwordTF.setError("This field is required");
                    emailTF.setError("This field is required");
                    Toast toast = Toast.makeText(getApplicationContext(), "Please fill all fields", Toast.LENGTH_SHORT);
//                    loginText.setText("Please fill all fields");
                    return;
                }
                User user = orm.logInUser(emailText, passwordText);
                if (user == null) {
//                    loginText.setText("Incorrect email or password");
                    emailTF.setError("Incorrect email or password");
                    passwordTF.setError("Incorrect email or password");
                    Toast toast = Toast.makeText(getApplicationContext(), "Incorrect email or password", Toast.LENGTH_SHORT);
                    return;
                }
                Intent intent = new Intent(MainActivity.this, NotesActivity.class);
                loggedInUser = user;

                Toast toast = Toast.makeText(getApplicationContext(), "Welcome " + user.getFirstName(), Toast.LENGTH_SHORT);

                startActivity(intent);
            }
        });
    }
}