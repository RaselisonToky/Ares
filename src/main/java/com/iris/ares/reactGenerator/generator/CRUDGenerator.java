package com.iris.ares.reactGenerator.generator;

import com.iris.ares.reactGenerator.annotations.Armagedon;
import com.iris.ares.reactGenerator.annotations.GeneratedCRUD;
import com.iris.ares.reactGenerator.config.FreemarkerConfig;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;

import static com.iris.ares.reactGenerator.generator.AppJSGenerator.generateAppJS;
import static com.iris.ares.reactGenerator.generator.ComponentGenerator.generateComponents;
import static com.iris.ares.reactGenerator.generator.LoginPageGenerator.generateLoginPage;
import static com.iris.ares.reactGenerator.react_handler.ReactProjectHandler.getValueFromEnv;


/**
 * CRUDGenerator
 * Utility class for generating CRUD pages based on classes annotated with @GeneratedCRUD.
 */

public class CRUDGenerator {
    private static final String PGD_FILE = "PGD.txt";
    private static final String[] TEMPLATES = {"list.ftl", "add.ftl", "edit.ftl"};
    private static final String[] DIRECTORY_PREFIXES = {"list_", "add_", "edit_"};
    private static final String PACKAGE_ROOT_PATH = getValueFromEnv("PACKAGE_ROOT_PATH");
    private static final String PAGES_DIRECTORY = "src/pages";


    /**
     * generateCRUDPages
     * Generates CRUD pages based on annotated classes annotated with @GeneratedCRUD..
     */
    public static void generateCRUDPages() {
        try {
            Configuration cfg = FreemarkerConfig.getConfig(CSSGenerator.class, "/templates/CRUDTemplates");
            Template[] templates = new Template[TEMPLATES.length];
            for (int i = 0; i < TEMPLATES.length; i++) {
                templates[i] = cfg.getTemplate(TEMPLATES[i]);
            }
            String reactProjectDirectory = readProjectDirectory();
            List<AnnotatedClassInfo> annotatedClasses = findAnnotatedClasses(GeneratedCRUD.class.getName());
            List<String> entityNames = new ArrayList<>();
            for (AnnotatedClassInfo classInfo : annotatedClasses) {
                generateClassPages(classInfo, reactProjectDirectory, templates);
                entityNames.add(classInfo.className());
            }

            generateAppJS(reactProjectDirectory, entityNames);
            generateLoginPage(reactProjectDirectory);
            generateComponents(reactProjectDirectory);
            EnveFileGenerator.generateEnvFileIfNeeded(reactProjectDirectory);
            PackageJsonModifier.addPostInstallCommands(reactProjectDirectory);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * readProjectDirectory
     * Reads the project directory from a file.
     *
     * @return The project directory path.
     * @throws IOException if an I/O error occurs.
     */
    private static String readProjectDirectory() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(PGD_FILE))) {
            return reader.readLine();
        }
    }


    /**
     * findAnnotatedClasses
     * Finds classes annotated with the specified annotation.
     *
     * @param string The fully qualified name of the annotation.
     * @return A list of AnnotatedClassInfo objects.
     */
    private static List<AnnotatedClassInfo> findAnnotatedClasses(String string) {
        List<AnnotatedClassInfo> annotatedClasses = new ArrayList<>();
        try {
            PathMatchingResourcePatternResolver scanner = new PathMatchingResourcePatternResolver();
            MetadataReaderFactory readerFactory = new SimpleMetadataReaderFactory(scanner);
            String packageSearchPath = "classpath*:" + PACKAGE_ROOT_PATH.replace('.', '/') + "/**/*.class";
            org.springframework.core.io.Resource[] resources = scanner.getResources(packageSearchPath);
            for (org.springframework.core.io.Resource resource : resources) {
                MetadataReader reader = readerFactory.getMetadataReader(resource);
                String className = reader.getClassMetadata().getClassName();
                Class<?> clazz = Class.forName(className);
                if (AnnotatedElementUtils.isAnnotated(clazz, string)) {
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


    /**
     * generateClassPages
     * Generates CRUD pages for a given annotated class.
     *
     * @param classInfo            AnnotatedClassInfo object representing the annotated class.
     * @param projectDirectory     The directory of the React project.
     * @param templates            Array of Template objects representing the CRUD templates.
     */
    private static void generateClassPages(AnnotatedClassInfo classInfo, String projectDirectory, Template[] templates) {
        try {
            String className = classInfo.className();
            String entityName = className.substring(className.lastIndexOf(".") + 1).toLowerCase();
            String entityDirectoryPath = projectDirectory + File.separator + PAGES_DIRECTORY + File.separator + entityName;
            File entityDirectory = new File(entityDirectoryPath);
            if (!entityDirectory.exists()) {
                entityDirectory.mkdirs();
            }

            for (int i = 0; i < templates.length; i++) {
                String directoryPath = entityDirectoryPath + File.separator + DIRECTORY_PREFIXES[i] + entityName;
                createSubdirectoriesIfNotExist(directoryPath);
                generateFileIfNotExists(directoryPath + File.separator + "page", templates[i], classInfo);
            }
        } catch (IOException | freemarker.template.TemplateException e) {
            System.err.println(e.getMessage());
        }
    }


    /**
     * generateFileIfNotExists
     * Generates a file if it does not exist.
     *
     * @param filePath  The path of the file to be generated.
     * @param template  The Template object representing the template file.
     * @param classInfo AnnotatedClassInfo object representing the annotated class.
     * @throws IOException if an I/O error occurs.
     * @throws freemarker.template.TemplateException if an error occurs while processing the template.
     */
    private static void generateFileIfNotExists(String filePath, Template template, AnnotatedClassInfo classInfo)
            throws IOException, freemarker.template.TemplateException {
        File file = new File(filePath + ".jsx");
        if (!file.exists()) {
            try (Writer writer = new FileWriter(file)) {
                Map<String, Object> data = new HashMap<>();
                // Retirer "com.aura.model." de className
                String entityName = classInfo.className().replace(PACKAGE_ROOT_PATH + ".", "");
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

    /**
     * isFieldAnnotatedWithArmagedon
     * Checks if a field is annotated with @Armagedon.
     *
     * @param fieldName The name of the field.
     * @param classInfo AnnotatedClassInfo object representing the annotated class.
     * @return true if the field is annotated with @Armagedon, false otherwise.
     */
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



    /**
     * createSubdirectoriesIfNotExist
     * Creates subdirectories if they do not exist.
     *
     * @param directoryPaths The paths of the directories to be created.
     */
    private static void createSubdirectoriesIfNotExist(String... directoryPaths) {
        for (String directoryPath : directoryPaths) {
            File directory = new File(directoryPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }
        }
    }


    /**
     * AnnotatedClassInfo
     * Represents information about an annotated class.
     */
    private record AnnotatedClassInfo(String className, List<FieldInfo> fields) {
    }

    /**
     * FieldInfo
     * Represents information about a field in a class.
     */
    private record FieldInfo(String name, String type) {
    }

    /**
     * Default Constructor
     */
    public CRUDGenerator(){

    }
}
