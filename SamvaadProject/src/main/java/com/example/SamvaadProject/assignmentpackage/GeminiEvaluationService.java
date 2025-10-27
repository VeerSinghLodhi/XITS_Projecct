//package com.example.SamvaadProject.assignmentpackage;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import okhttp3.*;
////import org.apache.catalina.connector.Request;
////import org.apache.catalina.connector.Response;
////import org.apache.tomcat.util.http.parser.MediaType;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Service;
//
//import java.io.IOException;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.TimeUnit;
//
//@Service
//public class GeminiEvaluationService {
//
//    private final OkHttpClient httpClient;
//    private final ObjectMapper objectMapper = new ObjectMapper();
//    private final String geminiApiKey;
//
//    @Autowired
//    private SubmitRepository submitAssignmentRepository;
//
//    @Autowired
//    public GeminiEvaluationService(@Value("${gemini.api.key}") String key) {
//        this.geminiApiKey = key;
//        this.httpClient = new OkHttpClient.Builder()
//                .readTimeout(120, TimeUnit.SECONDS)
//                .connectTimeout(60, TimeUnit.SECONDS)
//                .build();
//    }
//    @Async
//    public void evaluateAssignmentAsync(SubmitAssignment submission, AssignmentMaster assignment) throws IOException {
//        System.out.println("=== Gemini evaluation started for submission ID: " + submission.getSubmitId() + " ===");
//        byte[] pdfBytes = submission.getPdf();
//        String text = PdfUtils.extractText(pdfBytes);
//
//        if (text == null || text.trim().isEmpty()) {
////            System.out.println("⚠ No readable text found. Using Tesseract OCR...");
////            text = OcrUtils.extractTextFromPdf(pdfBytes);
//
////            if (text == null || text.trim().isEmpty()) {
//                submission.setGptFeedback("{\"verdict\": \"Error\", \"summary\": \"No readable text found in PDF (even after OCR).\", \"grade\": 0}");
//                submission.setGptScore(0);
//                submission.setStatus("Failed");
//                submission.setGptEvaluatedAt(new Date());
//                submitAssignmentRepository.save(submission);
//                return;
////            }
//        }
//
//        String cleanText = text.replaceAll("[\\x00-\\x1F\\x7F]", "");
//        String truncatedText = cleanText.substring(0, Math.min(cleanText.length(), 100000));
//        String title = assignment.getTitle() != null ? assignment.getTitle().toLowerCase() : "N/A";
//        String submissionTextLower = truncatedText.toLowerCase();
//
//        boolean isPython = title.contains("python");
//        boolean isJava = title.contains("java");
//        boolean isC = title.contains("c ") || title.contains(" c");
//
//        boolean containsPython = submissionTextLower.contains("python") || submissionTextLower.contains("def ");
//        boolean containsJava = submissionTextLower.contains("public class") || submissionTextLower.contains("system.out.print");
//        boolean containsC = submissionTextLower.contains("#include") || submissionTextLower.contains("int main");
//
//        boolean mismatch = (isPython && !containsPython)
//                || (isJava && !containsJava)
//                || (isC && !containsC);
//
//        if (mismatch) {
//            submission.setExtrafeedback("The submitted assignment does not match the expected language/topic (" + title + ").");
//            submission.setGptScore(0);
//            submission.setStatus("Rejected - Topic Mismatch");
//            submission.setGptEvaluatedAt(new Date());
//            submitAssignmentRepository.save(submission);
//
//            submission.setGptFeedback("{\"Status\": \"Rejected\", \"summary\": \"The topic or programming language in the submission does not match the assignment title.\", \"grade\": 0}");
//            return;
//        }
//        String prompt = "You are a university professor grading a student's assignment.\n"
//                + "Generate feedback as if a professor is directly talking to the student.\n"
//                + "Assignment title: " + assignment.getTitle() + "\n\n"
//                + "STUDENT_SUBMISSION_START\n" + truncatedText + "\nSTUDENT_SUBMISSION_END\n\n"
//                + "Return JSON ONLY with keys:\n"
//                + "{\"Status\": \"Accepted/Rejected\", \"summary\": \"brief summary\", \"issues\": [\"weak point 1\"], \"suggestions\": [\"improvement 1\"], \"grade\": integer (0-100)}";
//        try {
//            Map<String, Object> payload = new HashMap<>();
//            payload.put("contents", List.of(Map.of("role", "user", "parts", List.of(Map.of("text", prompt)))));
//            payload.put("generationConfig", Map.of("temperature", 0.2, "candidateCount", 1));
//
//            String jsonBody = objectMapper.writeValueAsString(payload);
//            String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + geminiApiKey;
//
//            MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
//            RequestBody body = RequestBody.create(mediaType, jsonBody);
//            Request request = new Request.Builder().url(url).post(body).build();
//
//            try (Response response = httpClient.newCall(request).execute()) {
//                String responseBody = response.body().string();
//                if (responseBody == null || responseBody.isBlank()) {
//                    throw new IOException("Empty response from Gemini API");
//                }
//                JsonNode candidate = objectMapper.readTree(responseBody)
//                        .path("candidates").path(0)
//                        .path("content").path("parts").path(0)
//                        .path("text");
//
//                String cleaned = candidate.asText("").trim();
//                if (cleaned.startsWith("json")) cleaned = cleaned.substring(7).trim();
//                if (cleaned.endsWith("")) cleaned = cleaned.substring(0, cleaned.length() - 3).trim();
//
//                submission.setGptFeedback(cleaned);
//                submission.setGptEvaluatedAt(new Date());
//                submitAssignmentRepository.save(submission);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            submission.setGptFeedback("{\"error\": \"Evaluation failed: " + e.getMessage() + "\"}");
//            submission.setGptScore(0);
//            submission.setStatus("Failed");
//            submission.setGptEvaluatedAt(new Date());
//            submitAssignmentRepository.save(submission);
//}
//}
//}

package com.example.SamvaadProject.assignmentpackage;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class GeminiEvaluationService {

    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String geminiApiKey;

    @Autowired
    private SubmitRepository submitAssignmentRepository;

    @Autowired
    public GeminiEvaluationService(@Value("${gemini.api.key}") String key) {
        this.geminiApiKey = key;
        this.httpClient = new OkHttpClient.Builder()
                .readTimeout(120, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();
    }

    @Async
    public void evaluateAssignmentAsync(SubmitAssignment submission, AssignmentMaster assignment) throws IOException {
        System.out.println("=== Gemini evaluation started for submission ID: " + submission.getSubmitId() + " ===");

        byte[] pdfBytes = submission.getPdf();
        String text = PdfUtils.extractText(pdfBytes);

        // ✅ Handle empty PDF case
        if (text == null || text.trim().isEmpty()) {
            submission.setGptFeedback("{\"verdict\": \"Error\", \"summary\": \"No readable text found in PDF.\", \"grade\": 0}");
            submission.setGptScore(0);
            submission.setStatus("Failed");
            submission.setGptEvaluatedAt(new Date());
            submitAssignmentRepository.save(submission);
            return;
        }

        // ✅ Clean and truncate long text
        String cleanText = text.replaceAll("[\\x00-\\x1F\\x7F]", "");
        String truncatedText = cleanText.substring(0, Math.min(cleanText.length(), 100000));

        String title = assignment.getTitle() != null ? assignment.getTitle().toLowerCase() : "N/A";
        String submissionTextLower = truncatedText.toLowerCase();

        // ✅ Detect language mismatches
        boolean isPython = title.contains("python");
        boolean isJava = title.contains("java");
        boolean isC = title.contains("c ") || title.contains(" c");

        boolean containsPython = submissionTextLower.contains("python") || submissionTextLower.contains("def ");
        boolean containsJava = submissionTextLower.contains("public class") || submissionTextLower.contains("system.out.print");
        boolean containsC = submissionTextLower.contains("#include") || submissionTextLower.contains("int main");

        boolean mismatch = (isPython && !containsPython)
                || (isJava && !containsJava)
                || (isC && !containsC);

        if (mismatch) {
            submission.setExtrafeedback("The submitted assignment does not match the expected language/topic (" + title + ").");
            submission.setGptFeedback("{\"Status\": \"Rejected\", \"summary\": \"The topic or programming language in the submission does not match the assignment title.\", \"grade\": 0}");
            submission.setGptScore(0);
            submission.setStatus("Rejected - Topic Mismatch");
            submission.setGptEvaluatedAt(new Date());
            submitAssignmentRepository.save(submission);
            return;
        }

        // ✅ Build Gemini prompt
        String prompt = "You are a university professor grading a student's assignment.\n"
                + "Generate feedback as if a professor is directly talking to the student.\n"
                + "Assignment title: " + assignment.getTitle() + "\n\n"
                + "STUDENT_SUBMISSION_START\n" + truncatedText + "\nSTUDENT_SUBMISSION_END\n\n"
                + "Return STRICT JSON ONLY (no explanations, no markdown). Example format:\n"
                + "{\"Status\": \"Accepted\", \"summary\": \"brief summary\", \"issues\": [\"weak point 1\"], \"suggestions\": [\"improvement 1\"], \"grade\": 90}";

        try {
            // ✅ Prepare JSON payload
            Map<String, Object> payload = new HashMap<>();
            payload.put("contents", List.of(Map.of("role", "user", "parts", List.of(Map.of("text", prompt)))));
            payload.put("generationConfig", Map.of("temperature", 0.2, "candidateCount", 1));

            String jsonBody = objectMapper.writeValueAsString(payload);
            String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + geminiApiKey;

            MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(mediaType, jsonBody);
            Request request = new Request.Builder().url(url).post(body).build();

            try (Response response = httpClient.newCall(request).execute()) {
                String responseBody = response.body().string();

                if (responseBody == null || responseBody.isBlank()) {
                    throw new IOException("Empty response from Gemini API");
                }

                JsonNode candidate = objectMapper.readTree(responseBody)
                        .path("candidates").path(0)
                        .path("content").path("parts").path(0)
                        .path("text");

                String cleaned = candidate.asText("").trim();

                // ✅ Remove unwanted wrappers
                cleaned = cleaned.replaceAll("(?i)^json", "").trim();
                cleaned = cleaned.replaceAll("```json", "").replaceAll("```", "").trim();

                // ✅ Validate and sanitize JSON before saving
                try {
                    objectMapper.readTree(cleaned); // Check if valid JSON
                } catch (Exception e) {
                    System.out.println("⚠ Invalid JSON from Gemini, wrapping as text.");
                    cleaned = objectMapper.writeValueAsString(Map.of("raw_feedback", cleaned));
                }

                // ✅ Save safe feedback
                submission.setGptFeedback(cleaned);
                submission.setGptEvaluatedAt(new Date());
                submission.setStatus("Evaluated");
                submitAssignmentRepository.save(submission);
            }

        } catch (Exception e) {
            e.printStackTrace();
            submission.setGptFeedback("{\"error\": \"Evaluation failed: " + e.getMessage() + "\"}");
            submission.setGptScore(0);
            submission.setStatus("Failed");
            submission.setGptEvaluatedAt(new Date());
            submitAssignmentRepository.save(submission);
        }
    }
}
