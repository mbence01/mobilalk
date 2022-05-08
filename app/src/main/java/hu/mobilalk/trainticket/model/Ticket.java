package hu.mobilalk.trainticket.model;

import com.google.firebase.Timestamp;

public class Ticket {
    private int id;
    private int owner;
    private int trainId;
    private Timestamp buyDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public int getTrainId() {
        return trainId;
    }

    public void setTrainId(int trainId) {
        this.trainId = trainId;
    }

    public String getBuyDate() {
        return buyDate.toDate().toString();
    }

    public void setBuyDate(Timestamp buyDate) {
        this.buyDate = buyDate;
    }
}
