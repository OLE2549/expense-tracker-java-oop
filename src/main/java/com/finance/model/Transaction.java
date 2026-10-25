package com.finance.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public abstract class Transaction {
    private long id;
    private long userId;
    private String title;
    private BigDecimal amount;
    private String category;
    private LocalDate transactionDate;
    private String note;

    protected Transaction() {}
    protected Transaction(long id, long userId, String title, BigDecimal amount,
                          String category, LocalDate transactionDate, String note) {
        this.id=id; this.userId=userId; this.title=title; this.amount=amount;
        this.category=category; this.transactionDate=transactionDate; this.note=note;
    }

    public abstract String getType();
    public abstract BigDecimal getBalanceEffect();

    public long getId() { return id; }
    public void setId(long id) { this.id=id; }
    public long getUserId() { return userId; }
    public void setUserId(long userId) { this.userId=userId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title=title; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount=amount; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category=category; }
    public LocalDate getTransactionDate() { return transactionDate; }
    public void setTransactionDate(LocalDate transactionDate) { this.transactionDate=transactionDate; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note=note; }
}