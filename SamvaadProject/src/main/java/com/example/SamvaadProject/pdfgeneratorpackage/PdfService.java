package com.example.SamvaadProject.pdfgeneratorpackage;


import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

@Service
public class PdfService {

    private final SpringTemplateEngine templateEngine;

    public PdfService(SpringTemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public byte[] generatePdf(String templateName, Map<String, Object> data) throws IOException {
        Context context = new Context();
        context.setVariables(data);

        // Render Thymeleaf template to HTML string
        String htmlContent = templateEngine.process(templateName, context);

        // Generate PDF from HTML
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.withHtmlContent(htmlContent, "");
            builder.toStream(outputStream);
            builder.run();
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new IOException("Error generating PDF: " + e.getMessage(), e);
        }
    }

    public byte[] generatePdfFile(String templateName, Map<String, Object> data) throws IOException {
        byte[] pdfBytes = generatePdf(templateName, data);
//        Path path = Path.of(outputPath);
//        Files.write(path, pdfBytes);
        return pdfBytes;
    }
}