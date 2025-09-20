package com.example.SamvaadProject.admissionpackage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdmissionRepository extends JpaRepository<AdmissionMaster ,String> {

    @Query("select count(a) from  AdmissionMaster a")
    public Long getCount();

    List<AdmissionMaster> findByUserMaster_UserId(Long userId);

//    @Query("SELECT COUNT(a) FROM AdmissionMaster a")
//    public Long getCount();

}
