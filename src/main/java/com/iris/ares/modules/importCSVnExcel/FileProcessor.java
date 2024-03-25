package com.iris.ares.modules.importCSVnExcel;

import com.iris.ares.reactGenerator.annotations.ExcelColumn;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class FileProcessor {


    public <T> List<T> processCSV(MultipartFile file, Class<T> clazz) throws Exception {
        List<T> dataList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(reader)
                    .withType(clazz)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            dataList = csvToBean.parse();
        }

        return dataList;
    }

    public <T> List<T> processExcel(MultipartFile file, Class<T> clazz) throws Exception{
        List<T> dataList = new ArrayList<>();

        try (InputStream inputStream = file.getInputStream()) {
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0); // Obtenir la première feuille de calcul

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Ignorer la ligne d'en-têtes
                T obj = clazz.getDeclaredConstructor().newInstance(); // Créer une nouvelle instance de l'objet

                Field[] fields = clazz.getDeclaredFields();
                for (Field field : fields) {
                    ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
                    if (annotation != null) {
                        int index = annotation.index();
                        Cell cell = row.getCell(index);
                        if (cell != null) {
                            field.setAccessible(true);
                            if (cell.getCellType() == CellType.STRING) {
                                field.set(obj, cell.getStringCellValue());
                            } else if (cell.getCellType() == CellType.NUMERIC) {
                                if (DateUtil.isCellDateFormatted(cell)) {
                                    Date date = cell.getDateCellValue();
                                    field.set(obj, date);                              }
                                else {
                                    field.set(obj, (int) cell.getNumericCellValue());
                                }
                            }
                        }
                    }
                }
                dataList.add(obj);
            }

            workbook.close();
        }

        return dataList;
    }
    private <T> void setFieldValue(T obj, String fieldName, String fieldValue) throws IllegalAccessException {
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.getName().equals(fieldName)) {
                field.setAccessible(true);
                if (field.getType() == String.class) {
                    field.set(obj, fieldValue);
                } else if (field.getType() == Integer.class) {
                    field.set(obj, Integer.parseInt(fieldValue));
                } // Ajoutez d'autres types de données au besoin
                break;
            }
        }
    }
}
