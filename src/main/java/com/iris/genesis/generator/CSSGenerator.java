package com.iris.genesis.generator;

import java.io.*;

public class CSSGenerator {

    private static final String TEMPLATE_CSS = "templates/CSSTemplates/page.module.css";
    private static final String PGD_FILE = "PGD.txt";

    public static String readProjectDirectory() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(PGD_FILE))) {
            return reader.readLine();
        }
    }

    public static void generateCSSFile() {
        try {
            String projectDirectory = readProjectDirectory();
            String cssFilePath = projectDirectory + File.separator + "src" + File.separator + "page.module.css";
            File file = new File(cssFilePath);
            if (!file.exists()) {
                createCSSFile(cssFilePath);
            } else {
                System.out.println("CSS file already exists: " + cssFilePath);
            }
        } catch (IOException e) {
            System.err.println("An error occurred while generating CSS file: " + e.getMessage());
        }
    }

    private static void createCSSFile(String cssFilePath) throws IOException {
        File file = new File(cssFilePath);
        if (file.createNewFile()) {
            FileWriter writer = new FileWriter(file);
            writer.write(readTemplate(TEMPLATE_CSS));
            writer.close();
            System.out.println("CSS file created successfully: " + cssFilePath);
        } else {
            System.err.println("Failed to create CSS file. File already exists: " + cssFilePath);
        }
    }

    public static String readTemplate(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        try (InputStream inputStream = CSSGenerator.class.getClassLoader().getResourceAsStream(filePath)) {
            assert inputStream != null;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }
            }
        }
        return content.toString();
    }
}