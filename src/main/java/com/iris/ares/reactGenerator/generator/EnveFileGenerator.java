package com.iris.ares.reactGenerator.generator;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * EnvFileGenerator
 * Utility class for generating .env files.
 */
public class EnveFileGenerator {


    /**
     * generateEnvFileIfNeeded
     * Generates a .env file if it does not exist in the specified directory.
     *
     * @param directoryPath The path of the directory where the .env file should be generated.
     */
    public static void generateEnvFileIfNeeded(String directoryPath) {
        String envFilePath = directoryPath + File.separator + ".env";
        File envFile = new File(envFilePath);
        if (!envFile.exists()) {
            generateEnvFile(envFilePath);
        } else {
            System.out.println(".env file already exists at: " + envFilePath);
        }
    }


    /**
     * generateEnvFile
     * Generates a .env file with default environment variables.
     *
     * @param envFilePath The path of the .env file to be generated.
     */
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

    /**
     * Default Constructor
     */
    public EnveFileGenerator(){

    }
}
