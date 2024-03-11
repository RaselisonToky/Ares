package com.iris.ares.modules.csv;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("api/v1/export")
public class PdfExportController {

    PdfExportService pdfExportService;

    @Autowired
    public PdfExportController(PdfExportService pdfExportService) {
        this.pdfExportService = pdfExportService;
    }

    @PostMapping("/pdf")
    public ResponseEntity<byte[]> exportObjectToPdf(@RequestBody Map<String, Object> objectMap) {
        try {
            byte[] pdfBytes = pdfExportService.exportObjectToPdf(objectMap);
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=\"export.pdf\"")
                    .body(pdfBytes);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
