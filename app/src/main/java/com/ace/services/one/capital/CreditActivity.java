package com.ace.services.one.capital;

import static com.ace.services.one.capital.DefaultValues.DECIMAL_FORMAT;
import static com.ace.services.one.capital.DefaultValues.USERS_NODE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.ace.services.one.capital.models.CreditModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class CreditActivity extends AppCompatActivity {
    // XML Components
    private TextView approved_credit_limit, used_credit_limit, able_credit_limit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide Status Bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_credit);

        // Set full screen display
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;

        // Firebase initialization
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        databaseReference = databaseReference.child(USERS_NODE).child(userId).child("CreditActivity");

        // Link XML Components
        approved_credit_limit = findViewById(R.id.approved_credit_limit);
        used_credit_limit = findViewById(R.id.used_credit_limit);
        able_credit_limit = findViewById(R.id.able_credit_limit);
        Button bt_apply_now = findViewById(R.id.bt_apply_now);

        // Click listener on "Apply Now" button
        bt_apply_now.setOnClickListener(view -> startActivity(new Intent(CreditActivity.this, RequestLoanActivity.class)));

        // Get Credit details from database and update UI accordingly
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    // get credit details
                    CreditModel creditModel = snapshot.getValue(CreditModel.class);

                    // update UI
                    assert creditModel != null;
                    approved_credit_limit.setText(String.format("INR %s", DECIMAL_FORMAT.format(creditModel.getApprovedLimit())));
                    used_credit_limit.setText(String.format("INR %s", DECIMAL_FORMAT.format(creditModel.getUsedLimit())));
                    able_credit_limit.setText(String.format("INR %s", DECIMAL_FORMAT.format((creditModel.getApprovedLimit() - creditModel.getUsedLimit()))));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}