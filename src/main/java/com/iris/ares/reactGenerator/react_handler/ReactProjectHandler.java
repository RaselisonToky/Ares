package com.iris.ares.reactGenerator.react_handler;

import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;


/**
 * ReactProjectHandler
 * Utility class for handling React project creation and directory management.
 */
@Component
public class ReactProjectHandler {
    private static final String LOCK_FILE = "react_project_created.lock";
    private static final String REACT_SCRIPT = "C:\\Program Files\\nodejs\\npx.cmd";


    /**
     * createReactProject
     * Creates a new React project if it doesn't already exist.
     */
    public static void createReactProject() {
        if (lockFileExists()) {
            return;
        }
        try {
            String directory = getValueFromEnv("PROJECT_ROOT_PATH");
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("npx create-react-app : ");
            String projectName = reader.readLine();
            File reactDirectory = new File(directory, projectName);
            ProcessBuilder processBuilder = new ProcessBuilder(REACT_SCRIPT, "create-react-app", projectName);
            processBuilder.directory(new File(directory));
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            BufferedReader processReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = processReader.readLine()) != null) {
                System.out.println(line);
            }
            process.waitFor();
            System.out.println("Le projet React '" + projectName + "' a été créé avec succès à l'emplacement : " + reactDirectory.getAbsolutePath());
            createLockFile();
            writeProjectDirectoryToFile(reactDirectory.getAbsolutePath());

        } catch (Exception e) {
            System.err.println("Erreur lors de la création ou vérification du projet React : " + e.getMessage());
        }
    }


    /**
     * lockFileExists
     * Checks if the lock file exists.
     *
     * @return true if the lock file exists, false otherwise.
     */
    private static boolean lockFileExists(){ return Files.exists(Paths.get(LOCK_FILE)); }


    /**
     * createLockFile
     * Creates the lock file to indicate that the React project has been created.
     *
     * @throws IOException if an I/O error occurs.
     */
    private static void createLockFile() throws IOException{ Files.createFile(Paths.get(LOCK_FILE)); }



    /**
     * writeProjectDirectoryToFile
     * Writes the new React project directory to a PGD.txt file for future reference.
     *
     * @param projectDirectory The directory of the React project.
     */
    private static void writeProjectDirectoryToFile(String projectDirectory) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("PGD.txt"))) {
            System.out.println(projectDirectory);
            writer.write(projectDirectory);
        } catch (IOException e) {
            System.err.println("Erreur lors de l'écriture du répertoire du projet dans le fichier : " + e.getMessage());
        }
    }


    /**
     * getValueFromEnv
     * Gets the value of a key from the .env file.
     *
     * @param key The key whose value is to be retrieved.
     * @return The value corresponding to the key.
     */
    public static String getValueFromEnv(String key) {
        String projectRootPath = null;
        try (InputStream input = new FileInputStream(".env")) {
            Properties properties = new Properties();
            properties.load(input);
            projectRootPath = properties.getProperty(key);
            if (projectRootPath == null) {
                System.err.println(key + " n'est pas défini dans le fichier .env. Veuillez définir la propriété.");
                System.exit(1);
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture du fichier .env : " + e.getMessage());
        }
        return projectRootPath;
    }


    /**
     Default Constructor
     */
    public ReactProjectHandler(){

    }
}
