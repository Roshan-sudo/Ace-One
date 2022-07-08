package com.ace.services.one.capital;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;
import java.util.Objects;

public class KycActivity extends AppCompatActivity {
    // XML Components
    private TextView progressText;
    private ProgressBar progressBar;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide Status Bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_kyc);

        // Set full screen display
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;

        // Link XML Components
        WebView kycWebView = findViewById(R.id.kycWebView);
        progressBar = findViewById(R.id.kycProgressBar);
        progressText = findViewById(R.id.kycProgressText);

        // Firebase initialization
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference = databaseReference.child("Links").child("kycLink");

        // Set up WebView
        kycWebView.getSettings().setAllowFileAccess(true);
        kycWebView.getSettings().setJavaScriptEnabled(true);
        kycWebView.getSettings().setSupportZoom(true);
        kycWebView.getSettings().setBuiltInZoomControls(true);
        kycWebView.getSettings().setDisplayZoomControls(false);
        kycWebView.setWebViewClient(new WebViewClient());

        kycWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);

                // update loading progress text
                progressText.setText(String.format(Locale.ENGLISH, "%d%%", newProgress));

                // Hide progress details when loading completed
                if (newProgress == 100){
                    progressText.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

        // fetch kyc url from database and load in webView
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    // get "kyc" url
                    String link = Objects.requireNonNull(snapshot.getValue()).toString();
                    // load url in webView
                    kycWebView.loadUrl(link);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}