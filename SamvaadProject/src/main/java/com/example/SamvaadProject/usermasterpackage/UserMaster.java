package com.example.SamvaadProject.usermasterpackage;

import com.example.SamvaadProject.admissionpackage.AdmissionMaster;
import com.example.SamvaadProject.assignmentpackage.AssignmentMaster;
import com.example.SamvaadProject.assignmentpackage.SubmitAssignment;
import com.example.SamvaadProject.batchmasterpackage.BatchMaster;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "user_master")
public class UserMaster implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String fullName;
    private String email;
    private String username;
    private String password;
    private String contactNo;
    private String address;
    private String city;
//    private String fatherContact;


    @Temporal(TemporalType.DATE)
    private LocalDate dob;

    @Temporal(TemporalType.DATE)
    private Date createdOn;

    private byte [] photoPath;
//    private String specialization;
//    private Double fees;

    @Enumerated(EnumType.STRING)
    private Role role;

    private Boolean status=true;// Either Enabled(true) or Disabled(false).

    public enum Role {
        STUDENT, FACULTY, ADMIN
    }

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private List<String> roleList = new ArrayList<>();


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // List of roles{ADMIN,USER}
        Collection<SimpleGrantedAuthority>roles=roleList.stream().map(role->new SimpleGrantedAuthority(role)).collect(Collectors.toList());
        return roles;
    }

    @OneToMany(mappedBy = "userMaster", cascade = CascadeType.ALL)
    private List<AdmissionMaster> admissions;

    @OneToMany(mappedBy = "faculty", cascade = CascadeType.ALL)
    private List<BatchMaster> batchMasters;

    @OneToMany(mappedBy = "professor", cascade = CascadeType.ALL)
    private List<AssignmentMaster> assignmentMasterList;

    @OneToMany(mappedBy = "professor", cascade = CascadeType.ALL)
    private List<SubmitAssignment> assignments;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.status;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<String> roleList) {
        this.roleList = roleList;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public byte[] getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(byte[] photoPath) {
        this.photoPath = photoPath;
    }


    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<AdmissionMaster> getAdmissions() {
        return admissions;
    }

    public void setAdmissions(List<AdmissionMaster> admissions) {
        this.admissions = admissions;
    }

    public List<BatchMaster> getBatchMasters() {
        return batchMasters;
    }

    public void setBatchMasters(List<BatchMaster> batchMasters) {
        this.batchMasters = batchMasters;
    }

    public List<AssignmentMaster> getAssignmentMasterList() {
        return assignmentMasterList;
    }

    public void setAssignmentMasterList(List<AssignmentMaster> assignmentMasterList) {
        this.assignmentMasterList = assignmentMasterList;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<SubmitAssignment> getAssignments() {
        return assignments;
    }

    public void setAssignments(List<SubmitAssignment> assignments) {
        this.assignments = assignments;
    }
}
