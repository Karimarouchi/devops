package com.example;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// ✅ Classe factice pour remplacer HttpServletRequest
class FakeRequest {
    public String getParameter(String name) {
        if (name.equals("username")) return "admin";
        if (name.equals("password")) return "1234";
        return "";
    }
}

public class App {
    public static void main(String[] args) {
        System.out.println("✅ Application DevSecOps Demo is running!");
    }

    // 🔥 Vulnérabilité 1 : log d’un mot de passe
    public void insecureLog(FakeRequest request) {
        System.out.println("Password: " + request.getParameter("password"));
    }

    // 🔥 Vulnérabilité 2 : Injection SQL
    public void getUser(FakeRequest request, Connection conn) throws SQLException {
        String user = request.getParameter("username");
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE username = '" + user + "'");
        while (rs.next()) {
            System.out.println(rs.getString(1));
        }
    }
}
