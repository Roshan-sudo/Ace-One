package com.ace.services.one.capital;

import static com.ace.services.one.capital.DefaultValues.DECIMAL_FORMAT;
import static com.ace.services.one.capital.DefaultValues.FORMATTER;
import static com.ace.services.one.capital.DefaultValues.USERS_NODE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.ace.services.one.capital.models.UpcomingEmiModel;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity {
    // XML Components
    private TextView due_date, amount, due_in;
    private ImageView imgAdv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide Status Bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_home);

        // Set full screen display
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;

        // Link XML Components
        ImageView imgProfile = findViewById(R.id.imgProfile);
        ImageView imgCredit = findViewById(R.id.imgCredit);
        ImageView imgPassbook = findViewById(R.id.imgPassbook);
        due_date = findViewById(R.id.due_date);
        amount = findViewById(R.id.amount);
        due_in = findViewById(R.id.due_in);
        imgAdv = findViewById(R.id.imgAdv);

        // Firebase Initialization
        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        String userId = Objects.requireNonNull(auth.getCurrentUser()).getUid();

        // Click listener on "Profile" Button
        imgProfile.setOnClickListener(view -> startActivity(new Intent(HomeActivity.this, ProfileActivity.class)));

        // Click listener on "Credit" Button
        imgCredit.setOnClickListener(view -> startActivity(new Intent(HomeActivity.this, CreditActivity.class)));

        // Click listener on "Passbook" Button
        imgPassbook.setOnClickListener(view -> startActivity(new Intent(HomeActivity.this, PassbookActivity.class)));

        // Load Upcoming EMI details from database
        databaseReference.child(USERS_NODE).child(userId).child("HomeActivity").addValueEventListener(upcomingEmiDetailsListener);

        // Load Advertisement from database
        databaseReference.child("Links").child("homePageAd").addValueEventListener(adListener);
    }

    /**
     * Value Event Listener
     * to fetch upcoming emi details from database
     * and if data exists - update the UI accordingly
     */
    private final ValueEventListener upcomingEmiDetailsListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.exists()){
                // List ot store all the upcoming emi details
                List<UpcomingEmiModel> upcomingEmiModels = new ArrayList<>();

                // get data from database
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    UpcomingEmiModel upcomingEmiModel = dataSnapshot.getValue(UpcomingEmiModel.class);
                    upcomingEmiModels.add(upcomingEmiModel);
                }

                // Sorting the "upcomingEmiModels" list by date in ascending order i.e, the latest upcoming emi should be in first place
                upcomingEmiModels.sort(Comparator.comparing(UpcomingEmiModel::getDueDate));

                // Get the latest upcoming emi from list
                UpcomingEmiModel upcomingEmiModel = upcomingEmiModels.get(0);

                // Update the UI
                due_date.setText(upcomingEmiModel.getDueDate());
                amount.setText(String.format("INR %s", DECIMAL_FORMAT.format(upcomingEmiModel.getDueAmount())));
                String remainingDays = getRemainingDays(upcomingEmiModel.getDueDate());
                due_in.setText(String.format("Due in %s Days", remainingDays));
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    /**
     * Value Event Listener
     * to fetch "advertisement link" from the database
     * and if link exists in database - show it in "imgAdv" imageView
     */
    private final ValueEventListener adListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.exists()){
                // get ad link link from database
                String imgUrl = Objects.requireNonNull(snapshot.getValue()).toString();
                // show ad in imageview
                Glide.with(HomeActivity.this).load(imgUrl).into(imgAdv);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    /**
     * Function to calculate "days left" from the current date to the given date
     * @param dueDate the upcoming due date
     * @return returns the total days left
     */
    private String getRemainingDays(String dueDate){
        LocalDate date = LocalDate.parse(dueDate, FORMATTER);
        LocalDate now = LocalDate.now();
        return String.valueOf(ChronoUnit.DAYS.between(now, date));
    }
}