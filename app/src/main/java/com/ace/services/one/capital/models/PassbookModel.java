package com.ace.services.one.capital.models;

public class PassbookModel {
    private int emiRemaining;
    private boolean isActive;
    private String loanSanctionDate, loanId;

    public PassbookModel() {
        // Required Empty Constructor
    }

    public PassbookModel(int emiRemaining, boolean isActive, String loanSanctionDate, String loanId) {
        this.emiRemaining = emiRemaining;
        this.isActive = isActive;
        this.loanSanctionDate = loanSanctionDate;
        this.loanId = loanId;
    }

    public int getEmiRemaining() {
        return emiRemaining;
    }

    public void setEmiRemaining(int emiRemaining) {
        this.emiRemaining = emiRemaining;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getLoanSanctionDate() {
        return loanSanctionDate;
    }

    public void setLoanSanctionDate(String loanSanctionDate) {
        this.loanSanctionDate = loanSanctionDate;
    }

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }
}
