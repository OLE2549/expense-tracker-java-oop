package com.finance.service;

import com.finance.dao.UserDAO;
import com.finance.model.User;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.SQLException;

public class AuthService {
    private final UserDAO userDAO = new UserDAO();

    public User login(String email, String password) throws SQLException {
        User user=userDAO.findByEmail(email.trim().toLowerCase());
        return user != null && BCrypt.checkpw(password,user.getPasswordHash()) ? user : null;
    }

    public String register(String name, String email, String password) throws SQLException {
        if(name==null || name.trim().length()<2) return "กรุณากรอกชื่ออย่างน้อย 2 ตัวอักษร";
        if(email==null || !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) return "รูปแบบอีเมลไม่ถูกต้อง";
        if(password==null || password.length()<8) return "รหัสผ่านต้องมีอย่างน้อย 8 ตัวอักษร";
        if(userDAO.findByEmail(email.trim().toLowerCase())!=null) return "อีเมลนี้ถูกใช้งานแล้ว";
        userDAO.create(name.trim(),email.trim().toLowerCase(),BCrypt.hashpw(password,BCrypt.gensalt(12)));
        return null;
    }
}