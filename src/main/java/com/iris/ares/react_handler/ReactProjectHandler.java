package com.iris.ares.react_handler;

import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

@Component
public class ReactProjectHandler {
    private static final String LOCK_FILE = "react_project_created.lock";
    private static final String REACT_SCRIPT = "C:\\Program Files\\nodejs\\npx.cmd";
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
    private static boolean lockFileExists(){
        return Files.exists(Paths.get(LOCK_FILE));
    }

    private static void createLockFile() throws IOException{
        Files.createFile(Paths.get(LOCK_FILE));
    }

    private static void writeProjectDirectoryToFile(String projectDirectory) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("PGD.txt"))) {
            System.out.println(projectDirectory);
            writer.write(projectDirectory);
        } catch (IOException e) {
            System.err.println("Erreur lors de l'écriture du répertoire du projet dans le fichier : " + e.getMessage());
        }
    }

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
}
