package com.ace.services.one.capital;

import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;

public class DefaultValues {
    // Main "Users" Node For Firebase
    public static final String USERS_NODE = "users";

    // Fixed rate for GST
    public static final double GST_RATE = 18.00;

    public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.00");
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd MMMM yyyy");

    // Variables For KYC Status
    public static final int KYC_NOT_VERIFIED = 0;
    public static final int KYC_VERIFIED = 1;
    public static final int KYC_PROCESSING = 2;

    // Variables For Transactions Status
    public static final int TRANSACTION_FAILED = 0;
    public static final int TRANSACTION_SUCCESS = 1;
    public static final int TRANSACTION_PENDING = 2;
}
