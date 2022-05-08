package hu.mobilalk.trainticket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import hu.mobilalk.trainticket.model.User;

public class HomeActivity extends AppCompatActivity {
    TextView welcomeText;

    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private CollectionReference dbColl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        dbColl = db.collection("User");
        FirebaseUser user = auth.getCurrentUser();

        if(user == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage(R.string.errorNotLoggedIn).setTitle("Unauthorized user").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    setLoginPage();
                }
            });
            builder.create().show();
        }

        dbColl.whereEqualTo("email", user.getEmail()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for(QueryDocumentSnapshot doc : task.getResult()) {
                        User userObj = doc.toObject(User.class);

                        welcomeText = findViewById(R.id.welcomeText);
                        welcomeText.setText("Welcome, " + userObj.getUsername());
                        break;
                    }
                }
            }
        });
    }

    public void navigateToTickets(View view) {
        startActivity(new Intent(this, TicketsActivity.class));
    }

    private void setLoginPage() {
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void doLogout(View view) {
        auth.signOut();
        setLoginPage();
    }
}