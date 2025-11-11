package com.example;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// Classe factice pour remplacer HttpServletRequest dans l'exemple
class FakeRequest {
    public String getParameter(String name) {
        if ("username".equals(name)) return "admin";
        if ("password".equals(name)) return "P@ssw0rd"; // mot de passe codé en dur (exposé)
        return "";
    }
}

public class App {
    public static void main(String[] args) {
        System.out.println("✅ Application DevSecOps Demo is running!");
        App a = new App();
        FakeRequest r = new FakeRequest();
        // appel des méthodes vulnérables pour que l'analyseur les voie
        a.insecureLog(r);

        // NOTE: getUser n'est pas exécuté (pas de vraie connexion), mais présent pour la détection statique
    }

    // 🔥 Vulnérabilité 1 : log d’un mot de passe en clair
    public void insecureLog(FakeRequest request) {
        // Semgrep règle: recherche System.out.println("Password: " + $X)
        System.out.println("Password: " + request.getParameter("password"));
    }

    // 🔥 Vulnérabilité 2 : Injection SQL via concaténation
    public void getUser(FakeRequest request, Connection conn) throws SQLException {
        String user = request.getParameter("username");
        Statement stmt = conn.createStatement();
        // Semgrep règle: pattern de concaténation dans executeQuery
        ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE username = '" + user + "'");
        while (rs.next()) {
            System.out.println(rs.getString(1));
        }
    }
}
