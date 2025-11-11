# 🐳 Dockerfile minimal pour ton projet Java DevSecOps Demo

# 1️⃣ Utiliser une image Java officielle
FROM eclipse-temurin:17-jdk-alpine

# 2️⃣ Définir le répertoire de travail
WORKDIR /app

# 3️⃣ Copier le code source (par exemple ton App.java)
COPY src ./src

# 4️⃣ Compilation du projet
RUN javac src/main/java/com/example/App.java -d out

# 5️⃣ Commande d’exécution par défaut
CMD ["java", "-cp", "out", "com.example.App"]
