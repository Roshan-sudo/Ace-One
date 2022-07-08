package com.ace.services.one.capital.models;

public class TransactionModel {
    private float amount;
    private String date, time, paymentMode;
    private int transactionId, status;

    public TransactionModel() {
        // Required Empty Constructor
    }

    public TransactionModel(float amount, String date, String time, String paymentMode, int transactionId, int status) {
        this.amount = amount;
        this.date = date;
        this.time = time;
        this.paymentMode = paymentMode;
        this.transactionId = transactionId;
        this.status = status;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
