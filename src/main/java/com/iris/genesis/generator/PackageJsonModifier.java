package com.iris.genesis.generator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class PackageJsonModifier {

    public static void addPostInstallCommands(String projectDirectory) {
        String packageJsonPath = projectDirectory + File.separator + "package.json";
        try {
            String postInstallCommands = "\"postinstall\": \"npm install axios && npm install @mui/material @emotion/react @emotion/styled && npm install react-router-dom\"";
            Path path = Paths.get(packageJsonPath);
            String content = new String(Files.readAllBytes(path));
            if (!content.contains("postinstall")) {
                content = content.replace("\"scripts\": {", "\"scripts\": {" + System.lineSeparator() + postInstallCommands + ",");
                Files.write(path, content.getBytes(), StandardOpenOption.WRITE);
                System.out.println("Post-install commands added to package.json successfully.");
            } else {
                System.out.println("Post-install commands already exist in package.json. Skipping addition.");
            }
        } catch (IOException e) {
            System.err.println("Error adding post-install commands to package.json: " + e.getMessage());
        }
    }


}
