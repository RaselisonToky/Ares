package com.iris.genesis.generator;

import com.iris.genesis.annotations.Armagedon;
import com.iris.genesis.annotations.GeneratedCRUD;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;

import static com.iris.genesis.generator.ComponentGenerator.generateComponents;

public class CRUDGenerator {
    private static final String PGD_FILE = "PGD.txt";
    private static final String TEMPLATE_LIST = "list.ftl";
    private static final String TEMPLATE_ADD = "add.ftl";
    private static final String TEMPLATE_EDIT = "edit.ftl";
    private static final String PAGES_DIRECTORY = "src/pages";

    public static void generateCRUDPages() {
        try {
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);
            cfg.setClassForTemplateLoading(CSSGenerator.class, "/templates/CRUDTemplates");
            cfg.setDefaultEncoding("UTF-8");
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            Template listTemplate = cfg.getTemplate(TEMPLATE_LIST);
            Template addTemplate = cfg.getTemplate(TEMPLATE_ADD);
            Template editTemplate = cfg.getTemplate(TEMPLATE_EDIT);
            String reactProjectDirectory = readProjectDirectory();
            List<AnnotatedClassInfo> annotatedClasses = findAnnotatedClasses();
            List<String> entityNames = new ArrayList<>();
            System.out.println("Nombre de classes annotées trouvées : " + annotatedClasses.size());

            for (AnnotatedClassInfo classInfo : annotatedClasses) {
                generateClassPages(classInfo, reactProjectDirectory, listTemplate, addTemplate, editTemplate);
                entityNames.add(classInfo.className());
            }

            AppJSGenerator.generateAppJS(reactProjectDirectory, entityNames);
            generateComponents(reactProjectDirectory);
            EnveFileGenerator.generateEnvFileIfNeeded(reactProjectDirectory);
            PackageJsonModifier.addPostInstallCommands(reactProjectDirectory);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
    private static String readProjectDirectory() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(PGD_FILE))) {
            return reader.readLine();
        }
    }
    private static List<AnnotatedClassInfo> findAnnotatedClasses() {
        List<AnnotatedClassInfo> annotatedClasses = new ArrayList<>();
        try {
            PathMatchingResourcePatternResolver scanner = new PathMatchingResourcePatternResolver();
            MetadataReaderFactory readerFactory = new SimpleMetadataReaderFactory(scanner);
            String packageSearchPath = "classpath*:" + getPackageRootPath().replace('.', '/') + "/**/*.class";
            org.springframework.core.io.Resource[] resources = scanner.getResources(packageSearchPath);
            for (org.springframework.core.io.Resource resource : resources) {
                MetadataReader reader = readerFactory.getMetadataReader(resource);
                String className = reader.getClassMetadata().getClassName();
                Class<?> clazz = Class.forName(className);
                if (AnnotatedElementUtils.isAnnotated(clazz, GeneratedCRUD.class.getName())) {
                    List<FieldInfo> fields = new ArrayList<>();
                    for (Field field : clazz.getDeclaredFields()) {
                            fields.add(new FieldInfo(field.getName(), field.getType().getSimpleName()));
                    }
                    annotatedClasses.add(new AnnotatedClassInfo(className, fields));
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println(e.getMessage());
        }
        return annotatedClasses;
    }
    private static void generateClassPages(AnnotatedClassInfo classInfo, String projectDirectory, Template listTemplate, Template addTemplate, Template editTemplate) {
        try {
            String className = classInfo.className();
            String entityName = className.substring(className.lastIndexOf(".") + 1).toLowerCase();
            String entityDirectoryPath = projectDirectory + File.separator + PAGES_DIRECTORY + File.separator + entityName;
            File entityDirectory = new File(entityDirectoryPath);
            if (!entityDirectory.exists()) {
                entityDirectory.mkdirs();
            }
            String addDirectoryPath = entityDirectoryPath + File.separator + "add_" + entityName;
            String editDirectoryPath = entityDirectoryPath + File.separator + "edit_" + entityName;
            String listDirectoryPath = entityDirectoryPath + File.separator + "list_" + entityName;
            createSubdirectoriesIfNotExist(addDirectoryPath, editDirectoryPath, listDirectoryPath);
            generateFileIfNotExists(addDirectoryPath + File.separator + "page", addTemplate, classInfo);
            generateFileIfNotExists(editDirectoryPath + File.separator + "page", editTemplate, classInfo);
            generateFileIfNotExists(listDirectoryPath + File.separator + "page", listTemplate, classInfo);
        } catch (IOException | freemarker.template.TemplateException e) {
            System.err.println(e.getMessage());
        }
    }

    private static void generateFileIfNotExists(String filePath, Template template, AnnotatedClassInfo classInfo)
            throws IOException, freemarker.template.TemplateException {
        File file = new File(filePath + ".jsx");
        if (!file.exists()) {
            try (Writer writer = new FileWriter(file)) {
                Map<String, Object> data = new HashMap<>();
                // Retirer "com.aura.model." de className
                String entityName = classInfo.className().replace(getPackageRootPath() + ".", "");
                data.put("entityName", entityName);
                List<Map<String, String>> fieldList = new ArrayList<>();
                for (FieldInfo fieldInfo : classInfo.fields()) {
                    Map<String, String> fieldMap = new HashMap<>();
                    fieldMap.put("name", fieldInfo.name());
                    fieldMap.put("type", fieldInfo.type());
                    fieldMap.put("isArmagedon", String.valueOf(isFieldAnnotatedWithArmagedon(fieldInfo.name(), classInfo)));
                    fieldList.add(fieldMap);
                }
                data.put("fields", fieldList);
                template.process(data, writer);
            }
        }
    }
    private static boolean isFieldAnnotatedWithArmagedon(String fieldName, AnnotatedClassInfo classInfo) {
        for (FieldInfo fieldInfo : classInfo.fields()) {
            if (fieldInfo.name().equals(fieldName)) {
                try {
                    Class<?> clazz = Class.forName(classInfo.className());
                    Field field = clazz.getDeclaredField(fieldName);
                    if (field.isAnnotationPresent(Armagedon.class)) {
                        return true;
                    }
                } catch (ClassNotFoundException | NoSuchFieldException e) {
                    System.err.println(e.getMessage());                }
            }
        }
        return false;
    }
    private static void createSubdirectoriesIfNotExist(String... directoryPaths) {
        for (String directoryPath : directoryPaths) {
            File directory = new File(directoryPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }
        }
    }
    public static String getPackageRootPath() {
        String projectRootPath = null;
        try (InputStream input = new FileInputStream(".env")) {
            Properties properties = new Properties();
            properties.load(input);
            projectRootPath = properties.getProperty("PACKAGE_ROOT_PATH");
            if (projectRootPath == null) {
                System.err.println("PACKAGE_ROOT_PATH n'est pas défini dans le fichier .env. Veuillez définir la propriété.");
                System.exit(1);
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture du fichier .env : " + e.getMessage());
        }
        return projectRootPath;
    }
    private record AnnotatedClassInfo(String className, List<FieldInfo> fields) {
    }
    private record FieldInfo(String name, String type) {
    }
}
