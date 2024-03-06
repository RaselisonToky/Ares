package com.iris.ares.config;

import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;

public class FreemarkerConfig {
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
}
