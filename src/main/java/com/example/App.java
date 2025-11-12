package com.example;

public class App {
    public static void main(String[] args) {
        System.out.println("Hello from DevSecOps pipeline!");
        greetUser("Karim");
    }

    public static void greetUser(String name) {
        // Exemple de bonne pratique : pas de hardcoded secret ni données sensibles
        if (name == null || name.isEmpty()) {
            System.out.println("Nom invalide !");
            return;
        }

        // Aucun secret, clé API ou mot de passe en clair ici
        String message = "Bienvenue, " + name + " 👋";
        System.out.println(message);
    }
}
