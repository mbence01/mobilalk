package hu.mobilalk.trainticket.model;

import android.util.Log;

import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class TicketDao {
    private FirebaseFirestore fireStore;
    private CollectionReference collection;

    public TicketDao() {
        fireStore = FirebaseFirestore.getInstance();
        collection = fireStore.collection("Ticket");
    }

    public List<Ticket> findAll() {
        System.out.println("FINDALL");
        final List<Ticket>[] res = new List[]{new ArrayList<>()};
        System.out.println("FINDALL2");

        collection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                System.out.println("TASK IN");
                if(task.isSuccessful()) {
                    System.out.println("TASK SUCCESS");
                    for (QueryDocumentSnapshot doc : task.getResult())  {
                        res[0].add(doc.toObject(Ticket.class));
                    }
                } else {
                    System.out.println("TASK NOT SUCCESS");
                    res[0] = null;
                }
            }
        });
        return res[0];
    }

    public List<Ticket> findByOwner(int id) {
        final List<Ticket>[] res = new List[]{new ArrayList<>()};

        collection.whereEqualTo("owner", id).orderBy("buyDate", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult())  {
                        res[0].add(doc.toObject(Ticket.class));
                    }
                } else {
                    res[0] = null;
                }
            }
        });
        return res[0];
    }


}
