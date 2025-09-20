package com.example.SamvaadProject.feespackage;

import com.example.SamvaadProject.admissionpackage.AdmissionMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeeRepository extends JpaRepository<FeePayment,Long> {


    List<FeePayment> findAllByOrderByFeeIdDesc();  //student list ko dekhne ke liye descending order me ayegi

    List<FeePayment> findByAdmission_AdmissionId(String admissionId);

    List<FeePayment> findByAdmissionIn(List<AdmissionMaster> admissions);
}
