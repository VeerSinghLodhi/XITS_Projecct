package com.example.SamvaadProject.coursepackage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<CourseMaster,Long> {


    @Query("select f.lumpSum from CourseMaster f where f.courseId=:cid")
    String getCourseFees(@Param("cid") Long cid);
}
