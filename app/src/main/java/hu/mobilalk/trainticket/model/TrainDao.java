package hu.mobilalk.trainticket.model;

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

public class TrainDao {
    private FirebaseFirestore fireStore;
    private CollectionReference collection;

    public TrainDao() {
        fireStore = FirebaseFirestore.getInstance();
        collection = fireStore.collection("Train");
    }

    public int getNextId() {
        final int[] id = {1};

        collection.orderBy("id", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        id[0] = doc.toObject(Train.class).getId() + 1;
                        break;
                    }
                } else {
                    id[0] = 1;
                }
            }
        });
        return id[0];
    }

    public List<Train> findAll() {
        final List<Train>[] res = new List[]{new ArrayList<>()};

        collection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult())  {
                        res[0].add(doc.toObject(Train.class));
                    }
                } else {
                    res[0] = null;
                }
            }
        });
        return res[0];
    }

    public List<Train> findById(int id) {
        final List<Train>[] res = new List[]{new ArrayList<>()};

        collection.whereEqualTo("id", id).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult())  {
                        res[0].add(doc.toObject(Train.class));
                    }
                } else {
                    res[0] = null;
                }
            }
        });
        return res[0];
    }

    public List<Train> findByFrom(String from) {
        final List<Train>[] res = new List[]{new ArrayList<>()};

        collection.whereEqualTo("from", from).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult())  {
                        res[0].add(doc.toObject(Train.class));
                    }
                } else {
                    res[0] = null;
                }
            }
        });
        return res[0];
    }

    public List<Train> findByTo(String to) {
        final List<Train>[] res = new List[]{new ArrayList<>()};

        collection.whereEqualTo("to", to).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult())  {
                        res[0].add(doc.toObject(Train.class));
                    }
                } else {
                    res[0] = null;
                }
            }
        });
        return res[0];
    }

    public List<Train> findByFromAndTo(String from, String to) {
        final List<Train>[] res = new List[]{new ArrayList<>()};

        collection.whereEqualTo("from", from).whereEqualTo("to", to).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult())  {
                        res[0].add(doc.toObject(Train.class));
                    }
                } else {
                    res[0] = null;
                }
            }
        });
        return res[0];
    }
}
