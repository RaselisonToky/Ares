package com.iris.ares.reactGenerator.generator;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


public class EnveFileGenerator {
    public static void generateEnvFileIfNeeded(String directoryPath) {
        String envFilePath = directoryPath + File.separator + ".env";
        File envFile = new File(envFilePath);
        if (!envFile.exists()) {
            generateEnvFile(envFilePath);
        } else {
            System.out.println(".env file already exists at: " + envFilePath);
        }
    }

    private static void generateEnvFile(String envFilePath) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(envFilePath))) {
            // Ajoutez les paires clé-valeur nécessaires à votre fichier .env
            writer.println("REACT_APP_API=http://localhost:8080");
            // Ajoutez d'autres variables d'environnement selon vos besoins

            System.out.println(".env file generated successfully at: " + envFilePath);
        } catch (IOException e) {
            System.err.println("Error generating .env file: " + e.getMessage());
        }
    }
}
