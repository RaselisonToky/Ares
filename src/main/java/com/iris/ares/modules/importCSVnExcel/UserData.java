package com.iris.ares.modules.importCSVnExcel;

import com.iris.ares.reactGenerator.annotations.ExcelColumn;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
public class UserData {
    @Id
    @CsvBindByName(column = "userId")
    @ExcelColumn(index = 0)
    private int userId;

    @ExcelColumn(index = 1)
    @CsvBindByName(column = "nom")
    private String nom;

    @CsvBindByName(column = "age")
    @ExcelColumn(index = 2)
    private int age;

    @CsvBindByName(column = "dtn")
    @CsvDate("dd/MM/yyyy")
    @ExcelColumn(index = 3)
    Date dtn;
}
