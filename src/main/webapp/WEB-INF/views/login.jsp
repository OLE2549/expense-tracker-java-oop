<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html><html lang="th"><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1"><title>เข้าสู่ระบบ | MyPocket</title><link rel="stylesheet" href="${pageContext.request.contextPath}/assets/style.css"></head>
<body class="auth-bg"><main class="auth-card"><div class="brand-mark">฿</div><h1>MyPocket</h1><p class="muted">จัดการเงินของคุณอย่างง่ายดาย</p>
<% if(request.getAttribute("error")!=null){ %><div class="alert"><%= request.getAttribute("error") %></div><% } %>
<% if(session.getAttribute("flash")!=null){ %><div class="success"><%= session.getAttribute("flash") %></div><% session.removeAttribute("flash"); } %>
<form method="post" action="${pageContext.request.contextPath}/login"><label>อีเมล</label><input type="email" name="email" required autocomplete="email"><label>รหัสผ่าน</label><input type="password" name="password" required autocomplete="current-password"><button class="btn primary full">เข้าสู่ระบบ</button></form>
<p class="center muted">ยังไม่มีบัญชี? <a href="${pageContext.request.contextPath}/register">สมัครสมาชิก</a></p></main></body></html>