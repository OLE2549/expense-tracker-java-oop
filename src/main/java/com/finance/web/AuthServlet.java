package com.finance.web;

import com.finance.model.User;
import com.finance.service.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet(urlPatterns={"/login","/register"})
public class AuthServlet extends HttpServlet {
    private final AuthService authService=new AuthService();

    @Override protected void doGet(HttpServletRequest req,HttpServletResponse resp) throws ServletException,IOException {
        String path=req.getServletPath();
        if(req.getSession(false)!=null && req.getSession(false).getAttribute("user")!=null) { resp.sendRedirect(req.getContextPath()+"/dashboard"); return; }
        req.getRequestDispatcher(path.equals("/register")?"/WEB-INF/views/register.jsp":"/WEB-INF/views/login.jsp").forward(req,resp);
    }

    @Override protected void doPost(HttpServletRequest req,HttpServletResponse resp) throws ServletException,IOException {
        req.setCharacterEncoding("UTF-8");
        try {
            if(req.getServletPath().equals("/register")) {
                String error=authService.register(req.getParameter("fullName"),req.getParameter("email"),req.getParameter("password"));
                if(error!=null) { req.setAttribute("error",error); req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req,resp); return; }
                req.getSession().setAttribute("flash","สมัครสมาชิกสำเร็จ กรุณาเข้าสู่ระบบ");
                resp.sendRedirect(req.getContextPath()+"/login");
            } else {
                User user=authService.login(req.getParameter("email"),req.getParameter("password"));
                if(user==null) { req.setAttribute("error","อีเมลหรือรหัสผ่านไม่ถูกต้อง"); req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req,resp); return; }
                req.getSession().invalidate();
                HttpSession session=req.getSession(true);
                session.setAttribute("user",user);
                resp.sendRedirect(req.getContextPath()+"/dashboard");
            }
        } catch(Exception e) { throw new ServletException("ดำเนินการกับบัญชีไม่สำเร็จ",e); }
    }
}