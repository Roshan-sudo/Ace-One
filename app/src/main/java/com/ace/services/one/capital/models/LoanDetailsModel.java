package com.ace.services.one.capital.models;

public class LoanDetailsModel {
    private long loanAccountNo;
    private float loanAmount;

    public LoanDetailsModel() {
        // Required Empty Constructor
    }

    public LoanDetailsModel(long loanAccountNo, float loanAmount) {
        this.loanAccountNo = loanAccountNo;
        this.loanAmount = loanAmount;
    }

    public long getLoanAccountNo() {
        return loanAccountNo;
    }

    public void setLoanAccountNo(long loanAccountNo) {
        this.loanAccountNo = loanAccountNo;
    }

    public float getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(float loanAmount) {
        this.loanAmount = loanAmount;
    }
}
