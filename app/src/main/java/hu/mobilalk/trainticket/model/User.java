package hu.mobilalk.trainticket.model;

public class User {
    private int id;
    private String username;
    private String email;
    private int balance;

    public User() {
        this.username = "NULL";
        this.email = "NULL@NULL.COM";
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
