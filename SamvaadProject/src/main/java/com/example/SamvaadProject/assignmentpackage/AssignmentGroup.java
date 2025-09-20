package com.example.SamvaadProject.assignmentpackage;

import java.util.List;

public class AssignmentGroup {
    private String batchName;
    private List<AssignmentMaster> assignments;

    public AssignmentGroup(String batchName, List<AssignmentMaster> assignments) {
        this.batchName = batchName;
        this.assignments = assignments;
    }

    public String getBatchName() {
        return batchName;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }

    public List<AssignmentMaster> getAssignments() {
        return assignments;
    }

    public void setAssignments(List<AssignmentMaster> assignments) {
        this.assignments = assignments;
    }
}
