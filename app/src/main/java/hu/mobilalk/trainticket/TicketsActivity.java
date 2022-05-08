package hu.mobilalk.trainticket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import hu.mobilalk.trainticket.model.Ticket;
import hu.mobilalk.trainticket.model.Train;
import hu.mobilalk.trainticket.model.User;

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
        TableLayout root = findViewById(R.id.tickets);

        addFirstRow(root);

        Task<QuerySnapshot> userTask = db.collection("User").whereEqualTo("email", auth.getCurrentUser().getEmail()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for(QueryDocumentSnapshot doc : task.getResult()) {
                        User user = doc.toObject(User.class);

                        db.collection("Ticket").whereEqualTo("owner", user.getId()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> ticketTask) {
                                for(QueryDocumentSnapshot ticketDoc : ticketTask.getResult()) {
                                    Ticket ticket = ticketDoc.toObject(Ticket.class);

                                    if(ticket == null) {
                                        System.err.println("TICKET IS NULL");
                                        continue;
                                    }

                                    db.collection("Train").whereEqualTo("id", ticket.getTrainId()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> trainTask) {
                                            Train train = new Train();
                                            System.out.println(user.getUsername());

                                            for (QueryDocumentSnapshot trainDoc : trainTask.getResult()) {
                                                train = trainDoc.toObject(Train.class);
                                                break;
                                            }

                                            String trainName = train.getName() + " (" + train.getFrom() + " - " + train.getTo() + ")";

                                            TableRow row = new TableRow(TicketsActivity.this);

                                            TextView id = new TextView(TicketsActivity.this);
                                            id.setText("#" + String.format("%05d", ticket.getId()));
                                            id.setPadding(15, 15, 15, 15);

                                            TextView ticketUser = new TextView(TicketsActivity.this);
                                            ticketUser.setText(user.getUsername());
                                            ticketUser.setPadding(15, 15, 15, 15);

                                            TextView trainText = new TextView(TicketsActivity.this);
                                            trainText.setText(trainName);
                                            trainText.setPadding(15, 15, 15, 15);

                                            TextView date = new TextView(TicketsActivity.this);
                                            date.setText(ticket.getBuyDate());
                                            date.setPadding(15, 15, 15, 15);

                                            Button deleteBtn = new Button(TicketsActivity.this);
                                            deleteBtn.setText("Delete ticket " + ticket.getId());
                                            deleteBtn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    int ticketId = extractIdFromButton((Button)v);

                                                    db.collection("Ticket").whereEqualTo("id", ticketId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<QuerySnapshot> btnTask) {
                                                            for(QueryDocumentSnapshot d : btnTask.getResult()) {
                                                                d.getReference().delete();
                                                            }

                                                            AlertDialog.Builder builder = new AlertDialog.Builder(TicketsActivity.this);
                                                            builder.setMessage("Ticket with ID #" + (String.format("%05d", ticketId)) + " has successfully been deleted.")
                                                                    .setTitle("Info")
                                                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(DialogInterface dialog, int which) {
                                                                            reloadActivity();
                                                                        }
                                                                    });
                                                            builder.create().show();
                                                        }
                                                    });
                                                }
                                            });

                                            row.addView(id);
                                            row.addView(ticketUser);
                                            row.addView(trainText);
                                            row.addView(date);
                                            row.addView(deleteBtn);
                                            root.addView(row);
                                        }
                                    });
                                }
                            }
                        });
                        break;
                    }
                } else {
                    System.err.println("TASK FAILED");
                }
            }
        });
    }

    private void reloadActivity() {
        startActivity(new Intent(this, TicketsActivity.class));
    }

    private int extractIdFromButton(Button btn) {
        String text = btn.getText().toString();
        String[] splt = text.split(" ");

        if(splt.length != 3)
            return 0;

        try {
            return Integer.parseInt(splt[2]);
        } catch(NumberFormatException nfe) {
            nfe.printStackTrace();
            return 0;
        }
    }

    private void addFirstRow(TableLayout root) {
        TableRow row = new TableRow(TicketsActivity.this);

        TextView id = new TextView(TicketsActivity.this);
        id.setText("Ticket ID");
        id.setPadding(15, 15, 15, 15);
        id.setTextSize(18);

        TextView ticketUser = new TextView(TicketsActivity.this);
        ticketUser.setText("Customer");
        ticketUser.setPadding(15, 15, 15, 15);
        ticketUser.setTextSize(18);

        TextView train = new TextView(TicketsActivity.this);
        train.setText("Train");
        train.setPadding(15, 15, 15, 15);
        train.setTextSize(18);

        TextView date = new TextView(TicketsActivity.this);
        date.setText("Purchase date");
        date.setPadding(15, 15, 15, 15);
        date.setTextSize(18);

        TextView deleteBtn = new TextView(TicketsActivity.this);
        deleteBtn.setText("Delete ticket");
        deleteBtn.setTextSize(18);


        row.addView(id);
        row.addView(ticketUser);
        row.addView(train);
        row.addView(date);
        row.addView(deleteBtn);
        root.addView(row);
    }
}