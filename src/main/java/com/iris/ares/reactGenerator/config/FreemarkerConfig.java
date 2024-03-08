package com.iris.ares.reactGenerator.config;

import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;


/**
 * FreemarkerConfig
 * Utility class for configuring Freemarker templates.
 */
public class FreemarkerConfig {


    /**
     * getConfig
     * Configures the Freemarker template engine.
     *
     * @param loaderClass  The class used to load templates.
     * @param templatePath The path to the directory containing the templates.
     * @return A Configuration object for Freemarker.
     */
    public static Configuration getConfig(Class<?> loaderClass, String templatePath) {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);
        try {
            cfg.setClassForTemplateLoading(loaderClass, templatePath);
        } catch (Exception e) {
            System.err.println("Erreur lors de la configuration de Freemarker : " + e.getMessage());
        }
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        return cfg;
    }

    /**
     * Default Constructor
     */
    public FreemarkerConfig(){

    }
}
