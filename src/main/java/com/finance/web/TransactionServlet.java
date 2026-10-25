package com.finance.web;

import com.finance.model.Transaction;
import com.finance.model.User;
import com.finance.service.TransactionManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/transactions")
public class TransactionServlet extends HttpServlet {
    private final TransactionManager manager=new TransactionManager();
    private User current(HttpServletRequest req) { return (User)req.getSession().getAttribute("user"); }

    @Override protected void doGet(HttpServletRequest req,HttpServletResponse resp) throws ServletException,IOException {
        try {
            User user=current(req);
            String action=req.getParameter("action");
            if("edit".equals(action)) {
                long id=Long.parseLong(req.getParameter("id"));
                Transaction t=manager.get(id,user.getId());
                if(t==null) { resp.sendError(404); return; }
                req.setAttribute("item",t);
            }
            req.getRequestDispatcher("/WEB-INF/views/transaction-form.jsp").forward(req,resp);
        } catch(NumberFormatException e) { resp.sendError(400); }
        catch(Exception e) { throw new ServletException("เปิดแบบฟอร์มไม่สำเร็จ",e); }
    }

    @Override protected void doPost(HttpServletRequest req,HttpServletResponse resp) throws ServletException,IOException {
        req.setCharacterEncoding("UTF-8");
        User user=current(req);
        String action=req.getParameter("action");
        try {
            if("delete".equals(action)) {
                long id=Long.parseLong(req.getParameter("id"));
                manager.delete(id,user.getId());
                req.getSession().setAttribute("flash","ลบรายการแล้ว");
                resp.sendRedirect(req.getContextPath()+"/dashboard"); return;
            }
            long id=0;
            if(req.getParameter("id")!=null && !req.getParameter("id").isBlank()) id=Long.parseLong(req.getParameter("id"));
            manager.save(id,user.getId(),req.getParameter("title"),req.getParameter("amount"),req.getParameter("type"),
                req.getParameter("category"),req.getParameter("transactionDate"),req.getParameter("note"));
            req.getSession().setAttribute("flash",id>0?"แก้ไขรายการแล้ว":"เพิ่มรายการแล้ว");
            resp.sendRedirect(req.getContextPath()+"/dashboard");
        } catch(IllegalArgumentException e) {
            req.setAttribute("error",e.getMessage());
            try {
                if(req.getParameter("id")!=null && !req.getParameter("id").isBlank()) {
                    Transaction t=manager.get(Long.parseLong(req.getParameter("id")),user.getId());
                    req.setAttribute("item",t);
                }
            } catch(Exception ignored) {}
            req.getRequestDispatcher("/WEB-INF/views/transaction-form.jsp").forward(req,resp);
        } catch(Exception e) { throw new ServletException("บันทึกรายการไม่สำเร็จ",e); }
    }
}