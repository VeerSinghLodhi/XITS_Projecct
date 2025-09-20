package com.example.SamvaadProject.batchmasterpackage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BatchMasterRepository extends JpaRepository<BatchMaster, Long> {
}
