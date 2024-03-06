package com.iris.ares.generator;
import com.iris.ares.config.FreemarkerConfig;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import java.io.*;
import java.util.HashMap;

public class LoginPageGenerator {
    private static final String TEMPLATE_NAME = "login.ftl";
    private static final String PAGE_NAME = "page";
    public static void generateLoginPage(String string) {
        try {
            Configuration cfg = FreemarkerConfig.getConfig(LoginPageGenerator.class, "/templates/Auth");
            String output_directory = string + File.separator + "src/pages/auth/signin";

            // Loading the template
            Template template = cfg.getTemplate(TEMPLATE_NAME);

            // Creating the output directory if it does not exist
            File outputDirectory = new File(output_directory);
            if (!outputDirectory.exists()) {
                outputDirectory.mkdirs();
            }

            // Full path for the output file
            String outputFile = output_directory + File.separator + PAGE_NAME + ".jsx";

            // Generating the output file from the template
            try (Writer writer = new FileWriter(outputFile)) {
                template.process(new HashMap<>(), writer);
            }
            System.out.println("Page de connexion générée avec succès : " + outputFile);
        } catch (Exception e) {
            System.err.println("Erreur lors de la génération de la page de connexion : " + e.getMessage());
        }
    }

}