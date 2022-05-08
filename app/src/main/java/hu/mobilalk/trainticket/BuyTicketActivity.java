package hu.mobilalk.trainticket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import hu.mobilalk.trainticket.model.Ticket;
import hu.mobilalk.trainticket.model.Train;
import hu.mobilalk.trainticket.model.User;
import hu.mobilalk.trainticket.model.UserDao;

public class BuyTicketActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private CollectionReference dbColl;
    private LinearLayout root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_ticket);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        dbColl = db.collection("Train");

        root = findViewById(R.id.cardContainer);

        initializeDepartureCards();
    }

    private void initializeDepartureCards() {
        dbColl.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for(QueryDocumentSnapshot doc : task.getResult()) {
                        Train tmp = doc.toObject(Train.class);

                        CardView card = new CardView(BuyTicketActivity.this);
                        LinearLayout linearLayout = new LinearLayout(BuyTicketActivity.this);

                        card.setPadding(20, 20, 20, 20);

                        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        card.setLayoutParams(cardParams);

                        ViewGroup.MarginLayoutParams margins = (ViewGroup.MarginLayoutParams) card.getLayoutParams();
                        margins.setMargins(0, 30, 0, 30);
                        card.requestLayout();

                        linearLayout.setOrientation(LinearLayout.VERTICAL);

                        TextView trainName = new TextView(BuyTicketActivity.this);
                        trainName.setText(tmp.getName());
                        trainName.setTextSize(20);

                        TextView route = new TextView(BuyTicketActivity.this);
                        route.setText(tmp.getFrom() + " - " + tmp.getTo());
                        route.setTextSize(16);

                        TextView priceAndDate = new TextView(BuyTicketActivity.this);
                        priceAndDate.setText(tmp.getPrice() + " Ft * " + tmp.getWhen());
                        priceAndDate.setTextSize(12);

                        linearLayout.addView(trainName);
                        linearLayout.addView(route);
                        linearLayout.addView(priceAndDate);

                        card.addView(linearLayout);

                        card.setClickable(true);
                        card.setMinimumHeight(tmp.getId());

                        card.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int trainId = v.getMinimumHeight();

                                db.collection("Train").whereEqualTo("id", trainId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> trainTask) {
                                        if(trainTask.isSuccessful()) {
                                            for(QueryDocumentSnapshot doc : trainTask.getResult()) {
                                                Train tmp = doc.toObject(Train.class);

                                                db.collection("User").whereEqualTo("email", auth.getCurrentUser().getEmail()).limit(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> userTask) {
                                                        for (QueryDocumentSnapshot d : userTask.getResult()) {
                                                            User user = d.toObject(User.class);

                                                            if(user == null)
                                                                return;

                                                            if(tmp.getPrice() > user.getBalance()) {
                                                                AlertDialog.Builder builder = new AlertDialog.Builder(BuyTicketActivity.this);

                                                                builder.setMessage("You have insufficient funds in your account").setTitle("Error").setPositiveButton("OK", null);
                                                                builder.create().show();
                                                                return;
                                                            }

                                                            user.setBalance(user.getBalance() - tmp.getPrice());
                                                            d.getReference().set(user);


                                                            db.collection("Ticket").orderBy("id", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<QuerySnapshot> task3) {
                                                                    for (QueryDocumentSnapshot doc : task3.getResult()) {
                                                                        Ticket newTicket = new Ticket();
                                                                        Ticket t = doc.toObject(Ticket.class);
                                                                        int newId = 0;

                                                                        newId = t.getId() + 1;

                                                                        newTicket.setId(newId);
                                                                        newTicket.setBuyDate(Timestamp.now().toDate().toString());
                                                                        newTicket.setOwner(user.getId());
                                                                        newTicket.setTrainId(tmp.getId());

                                                                        db.collection("Ticket").add(newTicket).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<DocumentReference> ticketTask2) {
                                                                                if(ticketTask2.isSuccessful()) {
                                                                                    AlertDialog.Builder builder = new AlertDialog.Builder(BuyTicketActivity.this);

                                                                                    builder.setMessage("Purchase completed!").setTitle("Info").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                                        @Override
                                                                                        public void onClick(DialogInterface dialog, int which) {
                                                                                            navigateToMyTickets();
                                                                                        }
                                                                                    });
                                                                                    builder.create().show();
                                                                                } else {
                                                                                    AlertDialog.Builder builder = new AlertDialog.Builder(BuyTicketActivity.this);

                                                                                    builder.setMessage("Purchase failed!").setTitle("Error").setPositiveButton("OK", null);
                                                                                    builder.create().show();
                                                                                }
                                                                            }
                                                                        });
                                                                        break;
                                                                    }
                                                                }
                                                            });
                                                            break;
                                                        }
                                                    }
                                                });
                                            }
                                        } else {
                                            System.err.println(task.getException().getMessage().toString());
                                        }
                                    }
                                });
                            }
                        });

                        root.addView(card);
                    }
                }
            }
        });
    }

    private void navigateToMyTickets() {
        startActivity(new Intent(this, TicketsActivity.class));
    }
}