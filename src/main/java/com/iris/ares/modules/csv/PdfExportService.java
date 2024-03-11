package com.iris.ares.modules.csv;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.List;
import java.util.Collections;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;

@Service
public class PdfExportService {

    public byte[] exportObjectToPdf(Map<String, Object> objectMap) throws IOException {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                contentStream.newLineAtOffset(100, 700);
                contentStream.showText("Informations sur l'objet :");
                contentStream.newLine();

                // Parcourir la carte et ajouter les informations dans le PDF
                float y = 680; // Position verticale initiale
                printMap(objectMap, contentStream, y, 0); // Appel récursif pour gérer les structures imbriquées

                contentStream.endText();
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            document.save(outputStream);
            return outputStream.toByteArray();
        }
    }

    private void printMap(Map<String, Object> map, PDPageContentStream contentStream, float y, int level) throws IOException {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            // Ajout d'une indentation en fonction du niveau de profondeur
            String indent = String.join("", Collections.nCopies(level, " "));
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12); // Utilisation d'une police différente pour les clés
            contentStream.newLineAtOffset(0, -20); // Espacement vertical entre les lignes
            contentStream.showText(indent + key + ": ");

            if (value instanceof Map) {
                // Si la valeur est un Map, on affiche le début d'un nouvel objet
                contentStream.newLine();
                printMap((Map<String, Object>) value, contentStream, y - 20, level + 1); // Appel récursif
            } else if (value instanceof List) {
                // Si la valeur est une List, on affiche le début d'une liste
                contentStream.newLine();
                printList((List<Object>) value, contentStream, y - 20, level + 1); // Appel récursif
            } else {
                // Si la valeur est simple, on l'affiche directement
                contentStream.setFont(PDType1Font.HELVETICA, 12); // Retour à la police normale pour les valeurs
                contentStream.showText(value != null ? value.toString() : "null");
            }

            y -= 20; // Met à jour la position verticale pour la nouvelle ligne
        }
    }

    private void printList(List<Object> list, PDPageContentStream contentStream, float y, int level) throws IOException {
        for (Object item : list) {
            if (item instanceof Map) {
                // Si l'élément est un Map, on affiche le début d'un nouvel objet
                contentStream.newLine();
                printMap((Map<String, Object>) item, contentStream, y - 20, level + 1); // Appel récursif
            } else if (item instanceof List) {
                // Si l'élément est une List, on affiche le début d'une liste
                contentStream.newLine();
                printList((List<Object>) item, contentStream, y - 20, level + 1); // Appel récursif
            } else {
                // Si l'élément est simple, on l'affiche directement
                contentStream.setFont(PDType1Font.HELVETICA, 12); // Retour à la police normale pour les valeurs
                contentStream.showText(item != null ? item.toString() : "null");
            }

            y -= 20; // Met à jour la position verticale pour la nouvelle ligne
        }
    }
}
