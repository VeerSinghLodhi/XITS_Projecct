package com.example.SamvaadProject.assignmentpackage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<AssignmentMaster,Long> {
    List<AssignmentMaster> findByPdfDateBefore(LocalDate date);
}
