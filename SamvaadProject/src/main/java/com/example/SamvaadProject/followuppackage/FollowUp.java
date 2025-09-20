package com.example.SamvaadProject.followuppackage;

import com.example.SamvaadProject.enquirypackage.EnquiryMaster;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "followup")
public class FollowUp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long followupId;

    @ManyToOne
    @JoinColumn(name = "enquiryId")
    private EnquiryMaster enquiry;

    @Column(length = 500)
    private String comments;

    private String status; // Interested / Not Interested
    private Date nextFollowupDate;
    private Date followUpDate;

    public Date getFollowUpDate() {
        return followUpDate;
    }

    public void setFollowUpDate(Date followUpDate) {
        this.followUpDate = followUpDate;
    }

    public Long getFollowupId() {
        return followupId;
    }

    public void setFollowupId(Long followupId) {
        this.followupId = followupId;
    }

    public EnquiryMaster getEnquiry() {
        return enquiry;
    }

    public void setEnquiry(EnquiryMaster enquiry) {
        this.enquiry = enquiry;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getNextFollowupDate() {
        return nextFollowupDate;
    }

    public void setNextFollowupDate(Date nextFollowupDate) {
        this.nextFollowupDate = nextFollowupDate;
    }
}

