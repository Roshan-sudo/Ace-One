package com.ace.services.one.capital.models;

public class RequestLoanModel {
    private float processingFeeRate, InterestRate, maxLoanAmount;
    private int maxTenure;

    public RequestLoanModel() {
        // Required Empty Constructor
    }

    public RequestLoanModel(float processingFeeRate, float interestRate, float maxLoanAmount, int maxTenure) {
        this.processingFeeRate = processingFeeRate;
        InterestRate = interestRate;
        this.maxLoanAmount = maxLoanAmount;
        this.maxTenure = maxTenure;
    }

    public float getProcessingFeeRate() {
        return processingFeeRate;
    }

    public void setProcessingFeeRate(float processingFeeRate) {
        this.processingFeeRate = processingFeeRate;
    }

    public float getInterestRate() {
        return InterestRate;
    }

    public void setInterestRate(float interestRate) {
        InterestRate = interestRate;
    }

    public float getMaxLoanAmount() {
        return maxLoanAmount;
    }

    public void setMaxLoanAmount(float maxLoanAmount) {
        this.maxLoanAmount = maxLoanAmount;
    }

    public int getMaxTenure() {
        return maxTenure;
    }

    public void setMaxTenure(int maxTenure) {
        this.maxTenure = maxTenure;
    }
}
