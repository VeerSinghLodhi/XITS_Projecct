package com.example.SamvaadProject.usermasterpackage;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public class FacultyDTO {
    private Long facultyId;
    private String facultyName;
    private String facultyEmail;
    private String facultyContactNo;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate facultyDOB;
    private byte [] photo;

    private String facultyAddress;
    private String facultyCity;
    private String facultyStatus;

    public FacultyDTO() {
    }

    public FacultyDTO(String facultyName, String facultyEmail, String facultyContactNo, LocalDate facultyDOB, String facultyAddress, String facultyCity, String facultyStatus) {
        this.facultyName = facultyName;
        this.facultyEmail = facultyEmail;
        this.facultyContactNo = facultyContactNo;
        this.facultyDOB = facultyDOB;
        this.facultyAddress = facultyAddress;
        this.facultyCity = facultyCity;
        this.facultyStatus = facultyStatus;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public Long getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(Long facultyId) {
        this.facultyId = facultyId;
    }

    public String getFacultyName() {
        return facultyName;
    }

    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }

    public String getFacultyEmail() {
        return facultyEmail;
    }

    public void setFacultyEmail(String facultyEmail) {
        this.facultyEmail = facultyEmail;
    }

    public String getFacultyContactNo() {
        return facultyContactNo;
    }

    public void setFacultyContactNo(String facultyContactNo) {
        this.facultyContactNo = facultyContactNo;
    }


    public LocalDate getFacultyDOB() {
        return facultyDOB;
    }

    public void setFacultyDOB(LocalDate facultyDOB) {
        this.facultyDOB = facultyDOB;
    }

    public String getFacultyAddress() {
        return facultyAddress;
    }

    public void setFacultyAddress(String facultyAddress) {
        this.facultyAddress = facultyAddress;
    }

    public String getFacultyCity() {
        return facultyCity;
    }

    public void setFacultyCity(String facultyCity) {
        this.facultyCity = facultyCity;
    }

    public String getFacultyStatus() {
        return facultyStatus;
    }

    public void setFacultyStatus(String facultyStatus) {
        this.facultyStatus = facultyStatus;
    }
}
