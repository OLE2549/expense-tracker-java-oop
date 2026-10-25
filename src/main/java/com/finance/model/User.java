package com.finance.model;

public class User {
    private long id;
    private String fullName;
    private String email;
    private String passwordHash;

    public User() {}
    public User(long id, String fullName, String email, String passwordHash) {
        this.id = id; this.fullName = fullName; this.email = email; this.passwordHash = passwordHash;
    }
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
}