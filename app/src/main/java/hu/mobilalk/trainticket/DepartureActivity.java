package hu.mobilalk.trainticket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hu.mobilalk.trainticket.model.Train;

public class DepartureActivity extends AppCompatActivity {
    private String from = null;
    private String to = null;
    private Date when = null;

    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private CollectionReference dbColl;

    private TableLayout recordList;
    private SearchView searchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_departure);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        dbColl = db.collection("Train");

        recordList = findViewById(R.id.records);
        searchBar = findViewById(R.id.search);

        uploadTableWithRecords("");

        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                uploadTableWithRecords(newText);
                return true;
            }
        });
    }

    private void uploadTableWithRecords(String needle) {
        recordList.removeAllViews();

        addFirstRow();

        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        int checked = radioGroup.getCheckedRadioButtonId();

        String field;

        if(checked != -1) {
            switch(checked) {
                case R.id.from: {
                    field = "from";
                    break;
                }

                case R.id.to: {
                    field = "to";
                    break;
                }

                default: {
                    field = "";
                }
            }
        } else return;

        if(needle == null || needle.length() == 0) {
            dbColl.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()) {
                        for(QueryDocumentSnapshot doc : task.getResult()) {
                            addItem(doc.toObject(Train.class));
                        }
                    } else {
                        System.err.println(task.getException().getMessage());
                    }
                }
            });
        } else {
            System.out.println("FIELD: " + field + ", NEEDLE: " + needle);
            dbColl.whereEqualTo(field, needle).orderBy("when").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()) {
                        for(QueryDocumentSnapshot doc : task.getResult()) {
                            addItem(doc.toObject(Train.class));
                        }
                    } else {
                        System.err.println(task.getException().getMessage());
                    }
                }
            });
        }
    }

    private void addFirstRow() {
        TableRow row = new TableRow(this);
        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.FILL_PARENT);

        row.setLayoutParams(params);
        row.setGravity(Gravity.CENTER_HORIZONTAL);

        TextView name = new TextView(DepartureActivity.this);
        name.setText("Name of train");
        name.setPadding(15, 15, 15, 15);
        name.setTextSize(18);

        TextView from = new TextView(DepartureActivity.this);
        from.setText("Departure station");
        from.setPadding(15, 15, 15, 15);
        from.setTextSize(18);

        TextView to = new TextView(DepartureActivity.this);
        to.setText("End station");
        to.setPadding(15, 15, 15, 15);
        to.setTextSize(18);

        TextView when = new TextView(DepartureActivity.this);
        when.setText("Departure time");
        when.setPadding(15, 15, 15, 15);
        when.setTextSize(18);

        row.addView(name);
        row.addView(from);
        row.addView(to);
        row.addView(when);
        recordList.addView(row);
    }

    private void addItem(Train train) {
        TableRow row = new TableRow(DepartureActivity.this);
        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.FILL_PARENT);

        row.setLayoutParams(params);
        row.setGravity(Gravity.CENTER_HORIZONTAL);

        TextView name = new TextView(DepartureActivity.this);
        name.setText(train.getName());
        name.setPadding(15, 15, 15, 15);

        TextView from = new TextView(DepartureActivity.this);
        from.setText(train.getFrom());
        from.setPadding(15, 15, 15, 15);

        TextView to = new TextView(DepartureActivity.this);
        to.setText(train.getTo());
        to.setPadding(15, 15, 15, 15);

        TextView when = new TextView(DepartureActivity.this);
        when.setText(train.getWhen());
        when.setPadding(15, 15, 15, 15);

        row.addView(name);
        row.addView(from);
        row.addView(to);
        row.addView(when);
        recordList.addView(row);
    }
}