package com.finance.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Income extends Transaction {
    public Income() {}
    public Income(long id, long userId, String title, BigDecimal amount, String category, LocalDate date, String note) {
        super(id,userId,title,amount,category,date,note);
    }
    @Override public String getType() { return "INCOME"; }
    @Override public BigDecimal getBalanceEffect() { return getAmount(); }
}