package hu.mobilalk.trainticket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import hu.mobilalk.trainticket.model.Ticket;
import hu.mobilalk.trainticket.model.TicketDao;
import hu.mobilalk.trainticket.model.Train;
import hu.mobilalk.trainticket.model.TrainDao;
import hu.mobilalk.trainticket.model.User;
import hu.mobilalk.trainticket.model.UserDao;

public class TicketsActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private CollectionReference dbColl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tickets);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        dbColl = db.collection("Ticket");

        showTickets();
    }

    private void showTickets() {
        LinearLayout root = findViewById(R.id.linearMain);

        root.setOrientation(LinearLayout.VERTICAL);

        User[] user = { new User() };
        String[] name = { "" };

        db.collection("User");
        dbColl.whereEqualTo("email", auth.getCurrentUser().getEmail()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for(QueryDocumentSnapshot doc : task.getResult()) {
                        user[0] = doc.toObject(User.class);
                    }
                }
            }
        });

        db.collection("Ticket");
        dbColl.whereEqualTo("owner", user[0].getId()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for(QueryDocumentSnapshot doc : task.getResult()) {
                        Ticket ticket = doc.toObject(Ticket.class);

                        db.collection("Train");
                        dbColl.whereEqualTo("id", ticket.getTrainId()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()) {
                                    for (QueryDocumentSnapshot doc : task.getResult()) {
                                        name[0] = doc.toObject(Train.class).getName();
                                        break;
                                    }
                                }
                            }
                        });

                        TextView text = new TextView(TicketsActivity.this);

                        text.setText("#" + String.format("%03d", ticket.getId()) + ", " + user[0].getUsername() + ", " + name[0] + ", " + ticket.getBuyDate());
                        root.addView(text);
                    }
                }
            }
        });
    }
}