package com.finance.web;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebFilter(urlPatterns={"/dashboard","/transactions/*","/transactions","/logout"})
public class AuthFilter implements Filter {
    @Override public void doFilter(ServletRequest request,ServletResponse response,FilterChain chain) throws IOException,ServletException {
        HttpServletRequest req=(HttpServletRequest)request;
        HttpServletResponse resp=(HttpServletResponse)response;
        if(req.getSession(false)==null || req.getSession(false).getAttribute("user")==null) {
            resp.sendRedirect(req.getContextPath()+"/login"); return;
        }
        chain.doFilter(request,response);
    }
}