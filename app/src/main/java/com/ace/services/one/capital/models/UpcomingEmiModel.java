package com.ace.services.one.capital.models;

public class UpcomingEmiModel {
    private float dueAmount;
    private String dueDate, loanId;

    public UpcomingEmiModel() {
        // Required Empty Constructor
    }

    public UpcomingEmiModel(float dueAmount, String dueDate, String loanId) {
        this.dueAmount = dueAmount;
        this.dueDate = dueDate;
        this.loanId = loanId;
    }

    public float getDueAmount() {
        return dueAmount;
    }

    public void setDueAmount(float dueAmount) {
        this.dueAmount = dueAmount;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }
}
