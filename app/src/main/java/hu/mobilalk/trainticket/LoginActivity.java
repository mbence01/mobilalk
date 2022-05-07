package hu.mobilalk.trainticket;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {
    EditText username;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
    }

    public void setRegPage(View view) {
        startActivity(new Intent(this, RegActivity.class));
    }

    public void doLogin(View view) {
        String HARDCODED_USER = "admin";
        String HARDCODED_PASS = "password";

        String usernameText = username.getText().toString();
        String passwordText = password.getText().toString();

        if(usernameText.length() == 0) {

        }

        if(passwordText.length() == 0) {

        }

        if(!HARDCODED_USER.equals(usernameText) || !HARDCODED_PASS.equals(passwordText)) {

        }

        System.out.println("LOGIN BTN");
    }
}