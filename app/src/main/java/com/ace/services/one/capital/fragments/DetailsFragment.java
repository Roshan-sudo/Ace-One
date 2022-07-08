package com.ace.services.one.capital.fragments;

import static com.ace.services.one.capital.DefaultValues.DECIMAL_FORMAT;
import static com.ace.services.one.capital.DefaultValues.GST_RATE;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ace.services.one.capital.LoanDetailsActivity;
import com.ace.services.one.capital.R;

import java.util.Locale;

public class DetailsFragment extends Fragment {
    public DetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        // Link XML Components
        TextView loan_acc_no_section = view.findViewById(R.id.loan_acc_no_section);
        TextView loan_amount_section = view.findViewById(R.id.loan_amount_section);
        TextView annual_interest_section = view.findViewById(R.id.annual_interest_section);
        TextView processing_fee_section = view.findViewById(R.id.processing_fee_section);
        TextView gst_applicable_section = view.findViewById(R.id.gst_applicable_section);
        TextView amount_disbursed = view.findViewById(R.id.amount_disbursed);
        TextView loan_tenure_section = view.findViewById(R.id.loan_tenure_section);
        TextView emi_amount_section = view.findViewById(R.id.emi_amount_section);
        TextView loan_end_date = view.findViewById(R.id.loan_end_date);

        // update the UI with relevant values
        double loanAmount = LoanDetailsActivity.loanDetailsModel.getLoanAmount();
        loan_acc_no_section.setText(String.valueOf(LoanDetailsActivity.loanDetailsModel.getLoanAccountNo()));
        loan_amount_section.setText(String.format("INR %s", DECIMAL_FORMAT.format(loanAmount)));
        emi_amount_section.setText(String.format("INR %s", DECIMAL_FORMAT.format(LoanDetailsActivity.upcomingEmiModel.getDueAmount())));
        loan_end_date.setText(LoanDetailsActivity.upcomingEmiModel.getDueDate());
        annual_interest_section.setText(String.format("%s%%", LoanDetailsActivity.requestLoanModel.getInterestRate()));
        loan_tenure_section.setText(String.format(Locale.ENGLISH, "%d Months", LoanDetailsActivity.passbookModel.getEmiRemaining()));

        // Calculate the "processingFee, gstApplicable, amountDisbursed"
        double processingFee = loanAmount * LoanDetailsActivity.requestLoanModel.getProcessingFeeRate() / (100.00);
        double gstApplicable = processingFee * GST_RATE / (100.00);
        double amountDisbursed = loanAmount - (processingFee + gstApplicable);

        processing_fee_section.setText(String.format("INR %s", DECIMAL_FORMAT.format(processingFee)));
        gst_applicable_section.setText(String.format("INR %s", DECIMAL_FORMAT.format(gstApplicable)));
        amount_disbursed.setText(String.format("INR %s", DECIMAL_FORMAT.format(amountDisbursed)));

        return view;
    }
}