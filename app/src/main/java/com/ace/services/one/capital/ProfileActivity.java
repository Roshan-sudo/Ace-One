package com.ace.services.one.capital;

import static com.ace.services.one.capital.DefaultValues.KYC_NOT_VERIFIED;
import static com.ace.services.one.capital.DefaultValues.KYC_PROCESSING;
import static com.ace.services.one.capital.DefaultValues.KYC_VERIFIED;
import static com.ace.services.one.capital.DefaultValues.USERS_NODE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ace.services.one.capital.models.UserModel;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {
    // XML Components
    private LinearLayout kycBtn;
    private ImageView profilePicImg, kycStatusImg;
    private TextView name, course, institute, phone, txtKycStatus;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide Status Bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_profile);

        // Set full screen display
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;

        // Firebase Initialization
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String userId = Objects.requireNonNull(auth.getCurrentUser()).getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child(USERS_NODE).child(userId).child("ProfileActivity");

        // Link XML Components
        LinearLayout helpBtn = findViewById(R.id.helpBtn);
        kycBtn = findViewById(R.id.kycBtn);
        profilePicImg = findViewById(R.id.profilePicImg);
        kycStatusImg = findViewById(R.id.kycStatusImg);
        name = findViewById(R.id.name);
        course = findViewById(R.id.course);
        institute = findViewById(R.id.institute);
        phone = findViewById(R.id.phone);
        txtKycStatus = findViewById(R.id.txtKycStatus);

        // Get user details from database
        databaseReference.addValueEventListener(getUserDetailsListener);

        // Click listener on "Complete KYC" button
        kycBtn.setOnClickListener(view -> startActivity(new Intent(ProfileActivity.this, KycActivity.class)));

        // Click listener on "Help" button
        helpBtn.setOnClickListener(view -> startActivity(new Intent(ProfileActivity.this, HelpActivity.class)));
    }

    /**
     * Value Event Listener
     * to fetch user details from database
     * and if user details present in database - update UI accordingly
     */
    private final ValueEventListener getUserDetailsListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.exists()){
                // Fetch user details from database
                UserModel userModel = snapshot.getValue(UserModel.class);

                // Update the UI
                assert userModel != null;
                Glide.with(ProfileActivity.this).load(userModel.getProfilePicUrl()).into(profilePicImg);
                name.setText(userModel.getName());
                course.setText(userModel.getCourse());
                institute.setText(userModel.getInstitute());
                phone.setText(userModel.getPhone());
                int kycStatus = userModel.getKycStatus();

                // Update UI according to KYC status
                if (kycStatus == KYC_PROCESSING){
                    txtKycStatus.setText("Processing");
                    kycStatusImg.setImageResource(R.drawable.ic_processing);
                    kycBtn.setClickable(false);
                }else if (kycStatus == KYC_VERIFIED){
                    txtKycStatus.setText("KYC Verified");
                    kycBtn.setClickable(false);
                    kycStatusImg.setImageResource(R.drawable.ic_check);
                }else if (kycStatus == KYC_NOT_VERIFIED){
                    txtKycStatus.setText("Complete KYC");
                    kycBtn.setClickable(true);
                    kycStatusImg.setImageResource(R.drawable.arrowimage_foreground);
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Remove Event listener
        databaseReference.removeEventListener(getUserDetailsListener);
    }
}