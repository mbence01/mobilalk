package hu.mobilalk.trainticket.model;

import com.google.firebase.Timestamp;

public class Train {
    private int id;
    private String name;
    private String from;
    private String to;
    private Timestamp when;
    private int price;

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getWhen() {
        return when.toDate().toString();
    }

    public void setWhen(Timestamp when) {
        this.when = when;
    }
}
