package com.finance.web;

import com.finance.model.User;
import com.finance.service.TransactionManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Year;
import java.time.YearMonth;
import java.util.Map;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
    private final TransactionManager manager=new TransactionManager();
    @Override protected void doGet(HttpServletRequest req,HttpServletResponse resp) throws ServletException,IOException {
        try {
            User user=(User)req.getSession().getAttribute("user");
            String month=req.getParameter("month");
            if(month==null || !month.matches("\\d{4}-\\d{2}")) month=YearMonth.now().toString();
            Map<String,BigDecimal> totals=manager.totals(user.getId(),month);
            BigDecimal income=totals.getOrDefault("INCOME",BigDecimal.ZERO);
            BigDecimal expense=totals.getOrDefault("EXPENSE",BigDecimal.ZERO);
            req.setAttribute("selectedMonth",month);
            req.setAttribute("income",income);
            req.setAttribute("expense",expense);
            req.setAttribute("balance",income.subtract(expense));
            req.setAttribute("transactions",manager.list(user.getId(),null,month));
            Map<String,BigDecimal> yearly=manager.monthlyTotals(user.getId(),Year.now().getValue());
            String[] labels=new String[12], incomes=new String[12], expenses=new String[12];
            for(int i=1;i<=12;i++) {
                String key=String.format("%d-%02d",Year.now().getValue(),i);
                labels[i-1]=String.format("%02d",i);
                incomes[i-1]=yearly.getOrDefault(key+"_INCOME",BigDecimal.ZERO).toPlainString();
                expenses[i-1]=yearly.getOrDefault(key+"_EXPENSE",BigDecimal.ZERO).toPlainString();
            }
            req.setAttribute("chartLabels",String.join(",",labels));
            req.setAttribute("chartIncomes",String.join(",",incomes));
            req.setAttribute("chartExpenses",String.join(",",expenses));
            req.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(req,resp);
        } catch(Exception e) { throw new ServletException("โหลด Dashboard ไม่สำเร็จ",e); }
    }
}