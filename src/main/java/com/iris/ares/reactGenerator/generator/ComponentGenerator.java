package com.iris.ares.reactGenerator.generator;

import com.iris.ares.reactGenerator.config.FreemarkerConfig;
import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class ComponentGenerator {
    private static final String[] TEMPLATE_NAMES = {"select.jsx", "table.jsx", "login.jsx"};
    private static final String TEMPLATE_DIRECTORY = "/templates/ComponentTemplates";
    private static final String COMPONENTS_DIRECTORY = "src/components";
    public static void generateComponents(String reactProjectDirectory) {
        try {
            Configuration cfg = FreemarkerConfig.getConfig(ComponentGenerator.class, TEMPLATE_DIRECTORY);

            for (String templateName : TEMPLATE_NAMES) {
                Template template = cfg.getTemplate(templateName);
                String templateNameWithoutExtension = templateName.replaceFirst("[.][^.]+$", "");
                createComponentDirectory(reactProjectDirectory, templateNameWithoutExtension);
                String filePath = reactProjectDirectory + File.separator + COMPONENTS_DIRECTORY + File.separator + capitalizeFirstLetter(templateNameWithoutExtension) + File.separator + "index.jsx";
                generateFileIfNotExists(filePath, template);
            }
        } catch (IOException | freemarker.template.TemplateException e) {
            System.err.println("Error generating components: " + e.getMessage());
        }
    }
    private static void createComponentDirectory(String reactProjectDirectory, String templateName) {
        String componentDirectoryPath = reactProjectDirectory + File.separator + COMPONENTS_DIRECTORY + File.separator + capitalizeFirstLetter(templateName) ;
        File componentDirectory = new File(componentDirectoryPath);
        if (!componentDirectory.exists()) {
            componentDirectory.mkdirs();
        }
    }
    private static void generateFileIfNotExists(String filePath, Template template)
            throws IOException, freemarker.template.TemplateException {
        File file = new File(filePath);
        if (!file.exists()) {
            try (Writer writer = new FileWriter(file)) {
                Map<String, Object> data = new HashMap<>();
                template.process(data, writer);
            }
        }
    }
    private static String capitalizeFirstLetter(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
