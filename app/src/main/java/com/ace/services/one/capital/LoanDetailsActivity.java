package com.ace.services.one.capital;

import static com.ace.services.one.capital.DefaultValues.USERS_NODE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;

import com.ace.services.one.capital.fragments.DetailsFragment;
import com.ace.services.one.capital.fragments.EmiCalendarFragment;
import com.ace.services.one.capital.adapters.LoanDetailsFragmentAdapter;
import com.ace.services.one.capital.fragments.TransactionsFragment;
import com.ace.services.one.capital.models.EmiCalendarModel;
import com.ace.services.one.capital.models.LoanDetailsModel;
import com.ace.services.one.capital.models.PassbookModel;
import com.ace.services.one.capital.models.RequestLoanModel;
import com.ace.services.one.capital.models.TransactionModel;
import com.ace.services.one.capital.models.UpcomingEmiModel;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LoanDetailsActivity extends AppCompatActivity {
    // XML Components
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;

    public static UpcomingEmiModel upcomingEmiModel;
    public static RequestLoanModel requestLoanModel;
    public static LoanDetailsModel loanDetailsModel;
    public static PassbookModel passbookModel;

    public static List<EmiCalendarModel> emiCalendarModels = new ArrayList<>();
    public static List<TransactionModel> transactionModels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide Status Bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_loan_details);

        // Set full screen display
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;

        // Link XML Components
        tabLayout = findViewById(R.id.tabLayout);
        viewPager2 = findViewById(R.id.viewPager2);

        // get loan_id from previous activity
        String loanId = getIntent().getStringExtra("loan_id");

        // Initialize Database
        String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(USERS_NODE).child(userId);

        // Add tabs to tabLayout
        tabLayout.addTab(tabLayout.newTab().setText("Details"));
        tabLayout.addTab(tabLayout.newTab().setText("EMI Calendar"));
        tabLayout.addTab(tabLayout.newTab().setText("Transactions"));

        // initialize fragment adapter
        LoanDetailsFragmentAdapter fragmentAdapter = new LoanDetailsFragmentAdapter(getSupportFragmentManager(), getLifecycle());
        fragmentAdapter.addFragment(new DetailsFragment());
        fragmentAdapter.addFragment(new EmiCalendarFragment());
        fragmentAdapter.addFragment(new TransactionsFragment());

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

        // Get data from Database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    upcomingEmiModel = snapshot.child("HomeActivity").child(loanId).getValue(UpcomingEmiModel.class);
                    requestLoanModel = snapshot.child("RequestLoanActivity").getValue(RequestLoanModel.class);
                    loanDetailsModel = snapshot.child("LoanDetailsActivity").child(loanId).getValue(LoanDetailsModel.class);
                    passbookModel = snapshot.child("PassbookActivity").child(loanId).getValue(PassbookModel.class);

                    emiCalendarModels.clear();
                    for (DataSnapshot dataSnapshot : snapshot.child("LoanEmiCalendarActivity").child(loanId).getChildren()){
                        EmiCalendarModel emiCalendarModel = dataSnapshot.getValue(EmiCalendarModel.class);
                        emiCalendarModels.add(emiCalendarModel);
                    }

                    transactionModels.clear();
                    for (DataSnapshot dataSnapshot : snapshot.child("LoanTransactionsActivity").child(loanId).getChildren()){
                        TransactionModel transactionModel = dataSnapshot.getValue(TransactionModel.class);
                        transactionModels.add(transactionModel);
                    }

                    // set adapter to viewPager
                    viewPager2.setAdapter(fragmentAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}