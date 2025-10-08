package com.example.SamvaadProject.pdfpackage;

import com.example.SamvaadProject.coursepackage.CourseMaster;
import com.example.SamvaadProject.coursepackage.CourseRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.List;

@Controller
//@RequestMapping("")
public class PdfController {
    @Autowired
    PdfRepository pdfRepository;

    @Autowired
    CourseRepository courseRepository;

    @GetMapping("/admin/document")
    public String documents(@RequestParam(value = "course", required = false) Long cId,
                            Model model) {
        model.addAttribute("courses", courseRepository.findAll());
        model.addAttribute("pdfs", pdfRepository.findByCourse_CourseId(cId));
        return "document";
    }

    @GetMapping("/admin/view/{id}")
    public ResponseEntity<byte[]> view(@PathVariable Long id) {
        PdfMaster pdf = pdfRepository.findById(id)
                .orElse(null);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + pdf.getDocumentName() + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf.getDocumentPath());

    }


    @PostMapping("/admin/upload/material")
    public String upload(@RequestParam("course") CourseMaster courseId,
                         @RequestParam("document") MultipartFile file,
                         RedirectAttributes redirectAttributes,
                         Model model) {
        try {
            PdfMaster pdf = new PdfMaster();
            pdf.setDocumentName(file.getOriginalFilename());
            pdf.setCourse(courseId);
            pdf.setDocumentPath(file.getBytes());
            pdf.setUploadedAt(new Date());

            pdfRepository.save(pdf);
//            System.out.println("Name : " + file.getOriginalFilename() + "\nCourse Id : " + cId + "\nCourse : " + courseId);

            redirectAttributes.addFlashAttribute("studyMaterialUploaded", true);

            return "redirect:/admin/dashboard#studymaterial";

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error Uploading documet!!");
            return "login";
        }

    }


    @GetMapping("/admin/course/material/{courseId}")
    @ResponseBody
    public List<PdfDTO> getMaterialByCourse(@PathVariable("courseId")Long courseId){
        return pdfRepository.findByCourse_CourseId(courseId)
                .stream()
                .map(pdf->new PdfDTO(pdf.getDocumentName(),pdf.getUploadedAt().toString(),pdf.getPdfId()))
                .toList();
    }




    @GetMapping("/admin/delete/material/{pdfId}")
    public String getDeletePdf(@PathVariable("pdfId")Long pdfId,
                               RedirectAttributes redirectAttributes,
                               HttpSession session){

        pdfRepository.deleteById(pdfId);

        redirectAttributes.addAttribute("notePdfDeleted",true);
        return "redirect:/admin/dashboard";
    }


    @GetMapping("/student/pdf/view/{id}")
    public ResponseEntity<byte[]> viewForFaculty(@PathVariable Long id) {
        PdfMaster pdf = pdfRepository.findById(id)
                .orElse(null);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + pdf.getDocumentName() + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf.getDocumentPath());

    }





}
