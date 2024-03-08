package com.iris.ares.reactGenerator.generator;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;


/**
 * AppJSGenerator
 * Utility class for generating App.js file for React applications.
 */
public class AppJSGenerator {


    /**
     * Default Constructor
     */
    public AppJSGenerator(){

    }


    /**
     * generateAppJs
     * Generates the App.js file based on the provided entity names.
     * @param projectDirectory The project directory where the App.js file will be generated.
     * @param entityNames The list of entity names to generate routes for.
     */
    public static void generateAppJS(String projectDirectory, List<String> entityNames) {
        String appJSPath = projectDirectory + File.separator + "src" + File.separator + "App.js";
        String templatePath = "templates/App.js";
        String navigationPath = projectDirectory + File.separator + "src" + File.separator + "Navigation.js";
        try {
            String imports = generateImports(entityNames);
            String routes = generateRoutes(entityNames);
            if (Files.exists(Paths.get(navigationPath))) {
                List<String> navigationLines = Files.readAllLines(new File(navigationPath).toPath(), StandardCharsets.UTF_8);
                String manualImports = extractManualImports(navigationLines);
                String manualRoutes = extractManualRoutes(navigationLines);
                imports += manualImports;
                routes += manualRoutes;
            }
            String updatedContent = replaceMarkers(templatePath, imports, routes);
            Files.writeString(new File(appJSPath).toPath(), updatedContent);
            System.out.println("App.js file updated successfully: " + appJSPath);
        } catch (IOException e) {
            System.err.println("An error occurred while generating App.js file: " + e.getMessage());
        }
    }


    /**
     * generateImports
     * Generates import statements based on the provided entity names.
     * @param entityNames The list of entity names to generate imports for.
     * @return The generated import statements as a String.
     */
    private static String generateImports(List<String> entityNames) {
        StringBuilder imports = new StringBuilder();
        for (String entityName : entityNames) {
            String simplifiedEntityName = entityName.substring(entityName.lastIndexOf(".") + 1);
            imports.append("import List").append(simplifiedEntityName).append(" from './pages/").append(simplifiedEntityName.toLowerCase()).append("/list_").append(simplifiedEntityName.toLowerCase()).append("/page';\n");
            imports.append("import Add").append(simplifiedEntityName).append(" from './pages/").append(simplifiedEntityName.toLowerCase()).append("/add_").append(simplifiedEntityName.toLowerCase()).append("/page';\n");
            imports.append("import Edit").append(simplifiedEntityName).append(" from './pages/").append(simplifiedEntityName.toLowerCase()).append("/edit_").append(simplifiedEntityName.toLowerCase()).append("/page';\n");
        }
        imports.append("import Login from './pages/auth/signin/page';\n");
        return imports.toString();
    }


    /**
     * generateRoutes
     * Generates route declarations based on the provided entity names.
     * @param entityNames The list of entity names to generate routes for.
     * @return The generated route declarations as a String.
     */
    private static String generateRoutes(List<String> entityNames) {
        StringBuilder routes = new StringBuilder();
        for (String entityName : entityNames) {
            String simplifiedEntityName = entityName.substring(entityName.lastIndexOf(".") + 1);
            routes.append("<Route path='/list_").append(simplifiedEntityName.toLowerCase()).append("' element={<List").append(simplifiedEntityName).append(" />} />\n");
            routes.append("<Route path='/add_").append(simplifiedEntityName.toLowerCase()).append("' element={<Add").append(simplifiedEntityName).append(" />} />\n");
            routes.append("<Route path='/edit_").append(simplifiedEntityName.toLowerCase()).append("/:id' element={<Edit").append(simplifiedEntityName).append(" />} />\n");
        }
        routes.append("<Route path='/login' element={<Login/>} />");
        return indent(routes.toString(), 18);
    }





    /**
     * extractManualRoutes
     * Extracts manual route declarations from the provided navigation lines.
     * @param navigationLines The list of lines from the navigation file.
     * @return The extracted manual route declarations as a String.
     */
    private static String extractManualRoutes(List<String> navigationLines) {
        StringBuilder manualRoutes = new StringBuilder();
        for (String line : navigationLines) {
            if (line.trim().startsWith("<Route path")) {
                manualRoutes.append(line).append("\n");
            }
        }
        return indent(manualRoutes.toString(),0) ;
    }





    /**
     * extractManualImports
     * Extracts manual import statements from the provided navigation lines.
     * @param navigationLines The list of lines from the navigation file.
     * @return The extracted manual import statements as a String.
     */
    private static String extractManualImports(List<String> navigationLines) {
        StringBuilder manualImports = new StringBuilder();
        for (String line : navigationLines) {
            if (line.startsWith("import") && !line.contains("React from 'react'") && !line.contains("from 'react-router-dom'")) {
                manualImports.append(line).append("\n");
            }
        }
        return manualImports.toString();
    }







    /**
     * replaceMarkers
     * Replaces markers in the template file with the provided imports and routes.
     * @param templatePath The path to the template file.
     * @param imports The import statements to replace.
     * @param routes The route declarations to replace.
     * @return The updated content with replaced markers.
     * @throws IOException If an I/O error occurs while reading the template file.
     */
    private static String replaceMarkers(String templatePath, String imports, String routes) throws IOException {
        InputStream inputStream = AppJSGenerator.class.getClassLoader().getResourceAsStream(templatePath);
        if (inputStream == null) {
            throw new IllegalArgumentException("Resource not found: " + templatePath);
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            return reader.lines()
                    .map(line -> line.replace("${IMPORTS}", imports).replace("${ROUTES}", routes))
                    .collect(Collectors.joining("\n"));
        }
    }




    /**
     * indent
     * Adds indentation to each line of the provided string.
     * @param str The string to indent.
     * @param spaces The number of spaces to indent.
     * @return The indented string.
     */
    private static String indent(String str, int spaces) {
        StringBuilder sb = new StringBuilder();
        String[] lines = str.split("\n");
        for (String line : lines) {
            sb.append(" ".repeat(spaces)).append(line).append("\n");
        }
        return sb.toString();
    }
}
