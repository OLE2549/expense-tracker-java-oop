package com.finance.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Expense extends Transaction {
    public Expense() {}
    public Expense(long id, long userId, String title, BigDecimal amount, String category, LocalDate date, String note) {
        super(id,userId,title,amount,category,date,note);
    }
    @Override public String getType() { return "EXPENSE"; }
    @Override public BigDecimal getBalanceEffect() { return getAmount().negate(); }
}