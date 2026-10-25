package com.finance.service;

import com.finance.dao.TransactionDAO;
import com.finance.model.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class TransactionManager {
    private final TransactionDAO dao = new TransactionDAO();

    public List<Transaction> list(long userId, String type, String month) throws SQLException {
        return dao.findByUser(userId,type,month);
    }
    public Transaction get(long id,long userId) throws SQLException { return dao.findById(id,userId); }

    public void save(long id, long userId, String title, String amountText, String type,
                     String category, String dateText, String note) throws Exception {
        if(title==null || title.trim().isEmpty() || title.trim().length()>150) throw new IllegalArgumentException("กรุณากรอกชื่อรายการไม่เกิน 150 ตัวอักษร");
        BigDecimal amount;
        try { amount=new BigDecimal(amountText); } catch(Exception e) { throw new IllegalArgumentException("จำนวนเงินไม่ถูกต้อง"); }
        if(amount.compareTo(BigDecimal.ZERO)<=0 || amount.scale()>2) throw new IllegalArgumentException("จำนวนเงินต้องมากกว่า 0 และมีทศนิยมไม่เกิน 2 ตำแหน่ง");
        if(!"INCOME".equals(type) && !"EXPENSE".equals(type)) throw new IllegalArgumentException("ประเภทไม่ถูกต้อง");
        if(category==null || category.isBlank() || category.length()>80) throw new IllegalArgumentException("กรุณาระบุหมวดหมู่");
        LocalDate date;
        try { date=LocalDate.parse(dateText); } catch(Exception e) { throw new IllegalArgumentException("วันที่ไม่ถูกต้อง"); }
        String safeNote=(note==null ? "" : note.trim());
        Transaction t = "INCOME".equals(type)
            ? new Income(id,userId,title.trim(),amount,category.trim(),date,safeNote)
            : new Expense(id,userId,title.trim(),amount,category.trim(),date,safeNote);
        if(id>0) {
            if(dao.findById(id,userId)==null) throw new IllegalArgumentException("ไม่พบรายการนี้");
            if(!dao.update(t)) throw new IllegalStateException("แก้ไขรายการไม่สำเร็จ");
        } else if(!dao.insert(t)) throw new IllegalStateException("บันทึกรายการไม่สำเร็จ");
    }
    public boolean delete(long id,long userId) throws SQLException { return dao.delete(id,userId); }
    public java.util.Map<String,BigDecimal> totals(long userId,String month) throws SQLException { return dao.totals(userId,month); }
    public java.util.Map<String,BigDecimal> monthlyTotals(long userId,int year) throws SQLException { return dao.monthlyTotals(userId,year); }
}