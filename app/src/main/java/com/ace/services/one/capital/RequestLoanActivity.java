package com.ace.services.one.capital;

import static com.ace.services.one.capital.DefaultValues.DECIMAL_FORMAT;
import static com.ace.services.one.capital.DefaultValues.FORMATTER;
import static com.ace.services.one.capital.DefaultValues.GST_RATE;
import static com.ace.services.one.capital.DefaultValues.USERS_NODE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ace.services.one.capital.models.PassbookModel;
import com.ace.services.one.capital.models.RequestLoanModel;
import com.ace.services.one.capital.models.UpcomingEmiModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

public class RequestLoanActivity extends AppCompatActivity {
    // XML Components
    private TextView txt_amount, txtMaxAmount, txt_time, txtMaxTime, processing_fee, gst_applicable, amount_disbursed, emi_amount, txt_processing_fee;
    private SeekBar seekbar_amount, seekbar_tenure;

    private RequestLoanModel requestLoanModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide Status Bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_request_loan);

        // Set full screen display
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;

        // Link XML Components
        txt_amount = findViewById(R.id.txt_amount);
        txtMaxAmount = findViewById(R.id.txtMaxAmount);
        txtMaxTime = findViewById(R.id.txtMaxTime);
        txt_time = findViewById(R.id.txt_time);
        txt_processing_fee = findViewById(R.id.txt_processing_fee);
        processing_fee = findViewById(R.id.processing_fee);
        gst_applicable = findViewById(R.id.gst_applicable);
        amount_disbursed = findViewById(R.id.amount_disbursed);
        emi_amount = findViewById(R.id.emi_amount);
        seekbar_amount = findViewById(R.id.seekbar_amount);
        seekbar_tenure = findViewById(R.id.seekbar_tenure);
        Button btn_proceed = findViewById(R.id.btn_proceed);

        // Database initialization
        String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(USERS_NODE).child(userId);

        // Fetch loan data from database and update UI accordingly
        databaseReference.child("RequestLoanActivity").addValueEventListener(loadDataListener);

        // Update UI accordingly when user slides the "amount" seekbar
        seekbar_amount.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser){
                    if (progress >= 0 && progress <= seekbar_amount.getMax()){
                        // update "amount" textview
                        txt_amount.setText(String.format("INR %s", DECIMAL_FORMAT.format(progress)));

                        // update rest required UI
                        updateUi(progress, requestLoanModel.getProcessingFeeRate(), requestLoanModel.getInterestRate(), seekbar_tenure.getProgress());
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Update UI accordingly when user slides the "tenure" seekbar
        seekbar_tenure.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser){
                    if (progress >= 0 && progress <= seekbar_tenure.getMax()){
                        // update "tenure" textview
                        txt_time.setText(String.format(Locale.ENGLISH, "%d Months", progress));

                        // update rest required UI
                        updateUi(seekbar_amount.getProgress(), requestLoanModel.getProcessingFeeRate(), requestLoanModel.getInterestRate(), progress);
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Click listener on "Proceed" button
        btn_proceed.setOnClickListener(view -> {
            int loanAmount = seekbar_amount.getProgress();
            int loanTenure = seekbar_tenure.getProgress();

            // Update the user selected values (loan amount and tenure) to database
            String loanId = databaseReference.child("HomeActivity").push().getKey();
            String currentDate = FORMATTER.format(LocalDate.now());

            UpcomingEmiModel upcomingEmiModel = new UpcomingEmiModel(loanAmount, getEmiDate(1), loanId);
            PassbookModel passbookModel = new PassbookModel(loanTenure, true, currentDate, loanId);
            assert loanId != null;
            databaseReference.child("HomeActivity").child(loanId).setValue(upcomingEmiModel);
            databaseReference.child("PassbookActivity").child(loanId).setValue(passbookModel);

            // open "approved" activity
            startActivity(new Intent(RequestLoanActivity.this, ApprovedActivity.class));
            RequestLoanActivity.this.finish();
        });
    }

    /**
     * Value Event Listener
     * to fetch "loan data" from database
     * and if data exists - update UI accordingly
     */
    private final ValueEventListener loadDataListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.exists()){
                // get loan details
                requestLoanModel = snapshot.getValue(RequestLoanModel.class);

                // set default loan amount and tenure
                int defaultAmount = 500;
                int defaultTenure = 1;

                // Update required data in UI
                assert requestLoanModel != null;
                txtMaxAmount.setText(String.format("INR %s", DECIMAL_FORMAT.format(requestLoanModel.getMaxLoanAmount())));
                txtMaxTime.setText(String.format(Locale.ENGLISH, "%d Months", requestLoanModel.getMaxTenure()));
                seekbar_amount.setMax((int) requestLoanModel.getMaxLoanAmount());
                seekbar_tenure.setMax(requestLoanModel.getMaxTenure());
                seekbar_amount.setProgress(defaultAmount);
                seekbar_tenure.setProgress(defaultTenure);
                txt_amount.setText(String.format("INR %s", DECIMAL_FORMAT.format(defaultAmount)));
                txt_time.setText(String.format(Locale.ENGLISH, "%d Month", defaultTenure));
                txt_processing_fee.setText(String.format("Processing Fee (%s%%)", DECIMAL_FORMAT.format(requestLoanModel.getProcessingFeeRate())));

                updateUi(defaultAmount, requestLoanModel.getProcessingFeeRate(), requestLoanModel.getInterestRate(), defaultTenure);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    /**
     * Function to update the
     * "processing fee", "gst applicable", "amount disbursed" and "emi amount"
     * in the UI by calculating these values
     * @param principal principal amount
     * @param processingFeeRate processing fee rate in percentage
     * @param interestRate interest rate in years
     * @param tenure duration in months
     */
    private void updateUi(double principal, double processingFeeRate, double interestRate, int tenure){
        // Calculate required data
        double processingFee = principal * processingFeeRate / (100.00);
        double gstApplicable = processingFee * GST_RATE / (100.00);
        double amountDisbursed = principal - (processingFee + gstApplicable);
        double emiAmount = calculateEmi(principal, interestRate, tenure);

        // Set values to textView
        processing_fee.setText(String.format("INR %s", DECIMAL_FORMAT.format(processingFee)));
        gst_applicable.setText(String.format("INR %s", DECIMAL_FORMAT.format(gstApplicable)));
        amount_disbursed.setText(String.format("INR %s", DECIMAL_FORMAT.format(amountDisbursed)));
        emi_amount.setText(String.format("INR %s", DECIMAL_FORMAT.format(emiAmount)));
    }

    /**
     * Function to calculate the emi with given values
     * @param principal principal amount
     * @param rate rate of interest in years
     * @param tenure duration in months
     * @return returns the calculated emi
     */
    private double calculateEmi(double principal, double rate, int tenure){
        rate = rate / (12 * 100); // one month interest
        return  (principal * rate * (float) Math.pow(1 + rate, tenure)) / (float) (Math.pow(1 + rate, tenure) - 1);
    }

    /**
     * Function to add the given months in the current date to get the new date
     * @param emiDuration EMI duration in months
     * @return returns the new calculated emi date
     */
    private String getEmiDate(int emiDuration){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        LocalDate emiDate = LocalDate.now().plusMonths(emiDuration);
        return formatter.format(emiDate);
    }
}