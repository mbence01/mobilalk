package hu.mobilalk.trainticket.model;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class UserDao {
    private FirebaseFirestore fireStore;
    private CollectionReference collection;

    private User tmp = null;

    public UserDao() {
        fireStore = FirebaseFirestore.getInstance();
        collection = fireStore.collection("User");
    }

    public int getNextId() {
        final int[] id = {1};

        collection.orderBy("id", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        id[0] = doc.toObject(User.class).getId() + 1;
                        break;
                    }
                } else {
                    id[0] = 1;
                }
            }
        });
        return id[0];
    }

    public List<User> findAll() {
        final List<User>[] res = new List[]{new ArrayList<>()};

        collection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult())  {
                        res[0].add(doc.toObject(User.class));
                    }
                } else {
                    res[0] = null;
                }
            }
        });
        return res[0];
    }

    public User findByEmail(String email) {
        tmp = null;

        collection.whereEqualTo("email", email).limit(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult())  {
                        UserDao.this.tmp = doc.toObject(User.class);
                        System.out.println("FROM DAO: " + UserDao.this.tmp.getUsername());
                        break;
                    }
                } else {
                    System.out.println("FROM DAO: IS SUCCESSFUL FALSE");
                }
            }
        });
        System.out.println("USER[0].USERNAME: " + (tmp==null ? "NULL" : tmp.getUsername()));
        return tmp;
    }

    public boolean insert(User user) {
        final boolean[] result = new boolean[1];

        collection.add(user).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                result[0] = task.isSuccessful();
            }
        });
        return result[0];
    }

    public boolean update(User user) {
        final boolean[] result = new boolean[1];


        collection.whereEqualTo("id", user.getId()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        doc.getReference().set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                result[0] = task.isSuccessful();
                                return;
                            }
                        });
                        break;
                    }
                } else {
                    result[0] = false;
                    return;
                }
            }
        });
        return result[0];
    }
}
