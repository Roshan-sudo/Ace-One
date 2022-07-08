package com.ace.services.one.capital.models;

public class CreditModel {
    private float approvedLimit, usedLimit;

    public CreditModel() {
        // Required Empty Constructor
    }

    public CreditModel(float approvedLimit, float usedLimit) {
        this.approvedLimit = approvedLimit;
        this.usedLimit = usedLimit;
    }

    public float getApprovedLimit() {
        return approvedLimit;
    }

    public void setApprovedLimit(float approvedLimit) {
        this.approvedLimit = approvedLimit;
    }

    public float getUsedLimit() {
        return usedLimit;
    }

    public void setUsedLimit(float usedLimit) {
        this.usedLimit = usedLimit;
    }
}
