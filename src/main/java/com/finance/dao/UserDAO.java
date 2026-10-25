package com.finance.dao;

import com.finance.config.DBConnection;
import com.finance.model.User;
import java.sql.*;

public class UserDAO {
    public User findByEmail(String email) throws SQLException {
        String sql = "SELECT id, full_name, email, password_hash FROM users WHERE email = ?";
        try (Connection c=DBConnection.getConnection(); PreparedStatement ps=c.prepareStatement(sql)) {
            ps.setString(1,email);
            try (ResultSet rs=ps.executeQuery()) {
                if (rs.next()) return new User(rs.getLong("id"),rs.getString("full_name"),rs.getString("email"),rs.getString("password_hash"));
            }
        }
        return null;
    }
    public boolean create(String name, String email, String passwordHash) throws SQLException {
        String sql = "INSERT INTO users(full_name,email,password_hash) VALUES(?,?,?)";
        try (Connection c=DBConnection.getConnection(); PreparedStatement ps=c.prepareStatement(sql)) {
            ps.setString(1,name); ps.setString(2,email); ps.setString(3,passwordHash);
            return ps.executeUpdate()==1;
        }
    }
}