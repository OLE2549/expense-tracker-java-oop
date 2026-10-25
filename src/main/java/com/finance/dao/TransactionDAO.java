package com.finance.dao;

import com.finance.config.DBConnection;
import com.finance.model.*;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class TransactionDAO {
    private Transaction map(ResultSet rs) throws SQLException {
        String type=rs.getString("type");
        long id=rs.getLong("id"), userId=rs.getLong("user_id");
        String title=rs.getString("title"), category=rs.getString("category"), note=rs.getString("note");
        BigDecimal amount=rs.getBigDecimal("amount");
        LocalDate date=rs.getDate("transaction_date").toLocalDate();
        return "INCOME".equals(type)
            ? new Income(id,userId,title,amount,category,date,note)
            : new Expense(id,userId,title,amount,category,date,note);
    }

    public List<Transaction> findByUser(long userId, String type, String month) throws SQLException {
        StringBuilder sql=new StringBuilder("SELECT * FROM transactions WHERE user_id=? ");
        if (type != null && !type.isBlank() && (type.equals("INCOME") || type.equals("EXPENSE"))) sql.append("AND type=? ");
        if (month != null && month.matches("\\d{4}-\\d{2}")) sql.append("AND DATE_FORMAT(transaction_date,'%Y-%m')=? ");
        sql.append("ORDER BY transaction_date DESC, id DESC");
        List<Transaction> list=new ArrayList<>();
        try(Connection c=DBConnection.getConnection(); PreparedStatement ps=c.prepareStatement(sql.toString())) {
            int i=1; ps.setLong(i++,userId);
            if(type!=null && !type.isBlank() && (type.equals("INCOME")||type.equals("EXPENSE"))) ps.setString(i++,type);
            if(month!=null && month.matches("\\d{4}-\\d{2}")) ps.setString(i,month);
            try(ResultSet rs=ps.executeQuery()){ while(rs.next()) list.add(map(rs)); }
        }
        return list;
    }

    public Transaction findById(long id, long userId) throws SQLException {
        try(Connection c=DBConnection.getConnection(); PreparedStatement ps=c.prepareStatement("SELECT * FROM transactions WHERE id=? AND user_id=?")) {
            ps.setLong(1,id); ps.setLong(2,userId);
            try(ResultSet rs=ps.executeQuery()){ if(rs.next()) return map(rs); }
        }
        return null;
    }

    public boolean insert(Transaction t) throws SQLException {
        String sql="INSERT INTO transactions(user_id,title,amount,type,category,transaction_date,note) VALUES(?,?,?,?,?,?,?)";
        try(Connection c=DBConnection.getConnection(); PreparedStatement ps=c.prepareStatement(sql)) {
            bind(ps,t); return ps.executeUpdate()==1;
        }
    }

    public boolean update(Transaction t) throws SQLException {
        String sql="UPDATE transactions SET title=?,amount=?,type=?,category=?,transaction_date=?,note=? WHERE id=? AND user_id=?";
        try(Connection c=DBConnection.getConnection(); PreparedStatement ps=c.prepareStatement(sql)) {
            ps.setString(1,t.getTitle()); ps.setBigDecimal(2,t.getAmount()); ps.setString(3,t.getType());
            ps.setString(4,t.getCategory()); ps.setDate(5,java.sql.Date.valueOf(t.getTransactionDate())); ps.setString(6,t.getNote());
            ps.setLong(7,t.getId()); ps.setLong(8,t.getUserId()); return ps.executeUpdate()==1;
        }
    }

    private void bind(PreparedStatement ps, Transaction t) throws SQLException {
        ps.setLong(1,t.getUserId()); ps.setString(2,t.getTitle()); ps.setBigDecimal(3,t.getAmount());
        ps.setString(4,t.getType()); ps.setString(5,t.getCategory());
        ps.setDate(6,java.sql.Date.valueOf(t.getTransactionDate())); ps.setString(7,t.getNote());
    }

    public boolean delete(long id, long userId) throws SQLException {
        try(Connection c=DBConnection.getConnection(); PreparedStatement ps=c.prepareStatement("DELETE FROM transactions WHERE id=? AND user_id=?")) {
            ps.setLong(1,id); ps.setLong(2,userId); return ps.executeUpdate()==1;
        }
    }

    public Map<String,BigDecimal> totals(long userId, String month) throws SQLException {
        String sql="SELECT type, COALESCE(SUM(amount),0) total FROM transactions WHERE user_id=? AND (? IS NULL OR DATE_FORMAT(transaction_date,'%Y-%m')=?) GROUP BY type";
        Map<String,BigDecimal> out=new HashMap<>(); out.put("INCOME",BigDecimal.ZERO); out.put("EXPENSE",BigDecimal.ZERO);
        try(Connection c=DBConnection.getConnection(); PreparedStatement ps=c.prepareStatement(sql)) {
            ps.setLong(1,userId); ps.setString(2,month); ps.setString(3,month);
            try(ResultSet rs=ps.executeQuery()){ while(rs.next()) out.put(rs.getString("type"),rs.getBigDecimal("total")); }
        }
        return out;
    }

    public Map<String,BigDecimal> monthlyTotals(long userId, int year) throws SQLException {
        String sql="SELECT DATE_FORMAT(transaction_date,'%Y-%m') month_key,type,SUM(amount) total FROM transactions WHERE user_id=? AND YEAR(transaction_date)=? GROUP BY month_key,type ORDER BY month_key";
        Map<String,BigDecimal> out=new LinkedHashMap<>();
        try(Connection c=DBConnection.getConnection(); PreparedStatement ps=c.prepareStatement(sql)) {
            ps.setLong(1,userId); ps.setInt(2,year);
            try(ResultSet rs=ps.executeQuery()){ while(rs.next()) out.put(rs.getString("month_key")+"_"+rs.getString("type"),rs.getBigDecimal("total")); }
        }
        return out;
    }
}