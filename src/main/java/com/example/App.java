package com.example;

import java.sql.*;
import javax.servlet.http.HttpServletRequest;

public class App {
    public static void main(String[] args) {
        System.out.println("✅ Application DevSecOps Demo is running!");
    }

    // 🔥 Vulnérabilité 1 : Log de mot de passe (insecure logging)
    public void insecureLog(HttpServletRequest request) {
        // Semgrep doit détecter l'affichage du password
        System.out.println("Password: " + request.getParameter("password"));
    }

    // 🔥 Vulnérabilité 2 : Injection SQL (concatenation non-sanitized)
    public void getUser(HttpServletRequest request, Connection conn) throws SQLException {
        String user = request.getParameter("username");
        Statement stmt = conn.createStatement();
        // Requête vulnérable (SQL injection)
        ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE username = '" + user + "'");
        while (rs.next()) {
            System.out.println(rs.getString(1));
        }
    }
}
