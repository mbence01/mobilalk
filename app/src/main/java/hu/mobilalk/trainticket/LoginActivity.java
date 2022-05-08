package hu.mobilalk.trainticket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    EditText email;
    EditText password;

    private FirebaseAuth auth;

    @Override
    protected void onStart() {
        super.onStart();

        auth.signOut();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        auth = FirebaseAuth.getInstance();
    }

    public void setRegPage(View view) {
        startActivity(new Intent(this, RegActivity.class));
    }
    public void setHomePage() {
        startActivity(new Intent(this, HomeActivity.class));
    }

    public void doLogin(View view) {
        String emailText = email.getText().toString();
        String passwordText = password.getText().toString();

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

        auth.signInWithEmailAndPassword(emailText, passwordText).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    builder.setMessage(R.string.messageLoginSuccess)
                            .setTitle("Success")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    setHomePage();
                                }
                            });
                    builder.create().show();
                } else {
                    builder.setMessage(R.string.messageLoginError).setTitle("Error").setPositiveButton("OK", null);
                    builder.create().show();
                }

            }
        });
    }
}