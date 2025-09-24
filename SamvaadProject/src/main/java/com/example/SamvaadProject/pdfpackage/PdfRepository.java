package com.example.SamvaadProject.pdfpackage;

import com.example.SamvaadProject.coursepackage.CourseMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface PdfRepository extends JpaRepository<PdfMaster, Long> {
    List<PdfMaster> findByCourse_CourseId(Long courseId);

}
