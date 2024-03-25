package com.iris.ares.modules.importCSVnExcel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
public class FileUploadController {

    private final FileProcessor fileProcessor;

    @Autowired
    public FileUploadController(FileProcessor fileProcessor) {
        this.fileProcessor = fileProcessor;
    }
    @PostMapping("/upload/csv")
    public List<UserData> uploadCSV(@RequestParam("file") MultipartFile file) throws Exception {
        List<UserData> userDataList = fileProcessor.processCSV(file, UserData.class);
        for (UserData userData : userDataList) {
            System.out.println(userData.getNom()); // Afficher le nom de chaque objet UserData
        }
        return userDataList;
    }

    @PostMapping("/upload/excel")
    public List<UserData> uploadExcel(@RequestParam("file") MultipartFile file) throws Exception {
        List<UserData> userDataList = fileProcessor.processExcel(file, UserData.class);
        for (UserData userData : userDataList) {
            System.out.println(userData.getNom()); // Afficher le nom de chaque objet UserData
        }
        return userDataList;
    }
}
