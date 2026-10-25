# Expense Tracker — Java Web + OOP + MySQL

โปรเจกต์ตัวอย่างเว็บไซต์บันทึกรายรับ–รายจ่าย ใช้ Java 17, Jakarta Servlet/JSP, Maven, Tomcat 10.1 และ MySQL 8+

## ความสามารถ
- สมัครสมาชิก/เข้าสู่ระบบ/ออกจากระบบ (รหัสผ่าน hash ด้วย BCrypt)
- แยกข้อมูลธุรกรรมตามเจ้าของบัญชี
- เพิ่ม แก้ไข ลบ และค้นหาตามเดือน/ประเภท
- Dashboard สรุปรายรับ รายจ่าย เงินสุทธิ และกราฟรายเดือน
- DAO ใช้ PreparedStatement

## วิธีติดตั้ง
1. ติดตั้ง JDK 17+, Maven, MySQL 8+ และ Tomcat 10.1+.
2. เปิด MySQL Workbench หรือ mysql CLI แล้วรัน `database/schema.sql`.
3. ตั้งค่า environment variables ก่อนเริ่ม Tomcat (หรือแก้ค่าใน `src/main/java/com/finance/config/DBConnection.java`):
   - `DB_URL` (ค่าเริ่มต้น `jdbc:mysql://localhost:3306/expense_tracker?useSSL=false&serverTimezone=Asia/Bangkok`)
   - `DB_USER` (ค่าเริ่มต้น `root`)
   - `DB_PASSWORD` (ค่าเริ่มต้นว่าง)
4. ในโฟลเดอร์โปรเจกต์ รัน `mvn clean package`.
5. นำ `target/expense-tracker.war` ไปวางใน `TOMCAT_HOME/webapps/` แล้วเปิด Tomcat.
6. เข้า `http://localhost:8080/expense-tracker/`.

## หมายเหตุ
- โปรเจกต์ใช้ `jakarta.*` จึงต้องใช้ Tomcat 10.1+ ไม่ใช่ Tomcat 9.
- เปลี่ยนรหัสผ่านฐานข้อมูลเป็นค่าที่ปลอดภัย อย่า commit credentials จริงขึ้น Git.
- ก่อนใช้งานจริง ควรเพิ่ม CSRF protection, rate limiting, การตรวจสอบอีเมล และการทดสอบอัตโนมัติ.
- Chart.js โหลดจาก CDN จึงต้องมีอินเทอร์เน็ตเพื่อแสดงกราฟ; ส่วนระบบหลักทำงานได้โดยไม่ต้องใช้กราฟ.
