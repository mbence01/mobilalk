package hu.mobilalk.trainticket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegActivity extends AppCompatActivity {
    private final String PREFIX = this.getClass().getName();

    EditText email;
    EditText password1;
    EditText password2;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        email = findViewById(R.id.email);
        password1 = findViewById(R.id.password);
        password2 = findViewById(R.id.password2);

        auth = FirebaseAuth.getInstance();
    }

    public void setLoginPage(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void doRegister(View view) {
        String emailText = email.getText().toString();
        String passwordText = password1.getText().toString();
        String password2Text = password2.getText().toString();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if(emailText == null || emailText.length() == 0) {
            builder.setMessage(R.string.errorMessageEmptyEmail).setTitle("Warning").setPositiveButton("OK", null);
            builder.create().show();
            return;
        }

        if(passwordText == null || passwordText.length() == 0) {
            builder.setMessage(R.string.errorMessageEmptyPassword).setTitle("Warning").setPositiveButton("OK", null);
            builder.create().show();
            return;
        }

        if(password2Text == null || password2Text.length() == 0) {
            builder.setMessage(R.string.errorMessageEmptyPasswordRepeat).setTitle("Warning").setPositiveButton("OK", null);
            builder.create().show();
            return;
        }

        if(!passwordText.equals(password2Text)) {
            builder.setMessage(R.string.errorMessagePasswordNotTheSame).setTitle("Warning").setPositiveButton("OK", null);
            builder.create().show();
            return;
        }

        if(passwordText.length() < 6) {
            builder.setMessage(R.string.errorMessagePasswordLength).setTitle("Warning").setPositiveButton("OK", null);
            builder.create().show();
            return;
        }

        auth.createUserWithEmailAndPassword(emailText, passwordText).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    builder.setMessage(R.string.messageRegSuccess).setTitle("Success").setPositiveButton("OK", null);
                    builder.create().show();

                    setLoginPage(findViewById(android.R.id.content).getRootView());
                } else {
                    builder.setMessage(R.string.messageRegError).setTitle("Error").setPositiveButton("OK", null);
                    builder.create().show();

                    Log.e(PREFIX, task.getException().getMessage());
                }

            }
        });
    }
}