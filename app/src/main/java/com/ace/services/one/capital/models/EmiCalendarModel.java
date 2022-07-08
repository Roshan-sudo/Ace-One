package com.ace.services.one.capital.models;

public class EmiCalendarModel {
    private double emiAmount;
    private int EmiNos;
    private boolean isPaid;

    public EmiCalendarModel() {
        // Required Empty Constructor
    }

    public EmiCalendarModel(double emiAmount, int emiNos, boolean isPaid) {
        this.emiAmount = emiAmount;
        EmiNos = emiNos;
        this.isPaid = isPaid;
    }

    public double getEmiAmount() {
        return emiAmount;
    }

    public void setEmiAmount(double emiAmount) {
        this.emiAmount = emiAmount;
    }

    public int getEmiNos() {
        return EmiNos;
    }

    public void setEmiNos(int emiNos) {
        EmiNos = emiNos;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }
}
