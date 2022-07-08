package com.ace.services.one.capital;

import static com.ace.services.one.capital.DefaultValues.USERS_NODE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import com.ace.services.one.capital.adapters.PassbookAdapter;
import com.ace.services.one.capital.models.PassbookModel;
import com.ace.services.one.capital.models.UpcomingEmiModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class PassbookActivity extends AppCompatActivity {
    // XML Components
    private TextView active_loans;

    private int totalActiveLoans = 0;

    public static final List<PassbookModel> passbookModels = new ArrayList<>();
    public static final List<UpcomingEmiModel> upcomingEmiModels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide Status Bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_passbook);

        // Set full screen display
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;

        // Database Initialization
        String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(USERS_NODE).child(userId);

        // Link XML Components
        RecyclerView recyclerView = findViewById(R.id.passbookRecyclerView);
        active_loans = findViewById(R.id.active_loans);

        // Set up recyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(PassbookActivity.this));
        PassbookAdapter adapter = new PassbookAdapter(PassbookActivity.this);
        recyclerView.setAdapter(adapter);

        // Click listener on Details Button
        adapter.setOnDetailsClickListener(position -> {
            Intent intent = new Intent(PassbookActivity.this, LoanDetailsActivity.class);
            intent.putExtra("loan_id", passbookModels.get(position).getLoanId());
            startActivity(intent);
        });

        // Get required data from Database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    upcomingEmiModels.clear();
                    for (DataSnapshot dataSnapshot : snapshot.child("HomeActivity").getChildren()){
                        UpcomingEmiModel model = dataSnapshot.getValue(UpcomingEmiModel.class);
                        upcomingEmiModels.add(model);
                    }

                    passbookModels.clear();
                    for (DataSnapshot dataSnapshot : snapshot.child("PassbookActivity").getChildren()){
                        PassbookModel model = dataSnapshot.getValue(PassbookModel.class);
                        assert model != null;
                        if (model.isActive()) totalActiveLoans += 1;
                        passbookModels.add(model);
                    }

                    passbookModels.sort(Comparator.comparing(PassbookModel::getLoanSanctionDate));

                    // Set total number of active loans
                    active_loans.setText(String.valueOf(totalActiveLoans));

                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}