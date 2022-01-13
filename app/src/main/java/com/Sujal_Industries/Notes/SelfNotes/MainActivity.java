package com.Sujal_Industries.Notes.SelfNotes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 420;
    private static final String TAG = "SelfNotes";
    private static final String spFileKey = "SelfNotes.SECRET_FILE";
    private SharedPreferences sharedPreferences;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SplashScreen.installSplashScreen(this);
        doInitialSetup();
    }

    private void doInitialSetup() {
        sharedPreferences = getSharedPreferences(spFileKey, MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            if (!sharedPreferences.getBoolean("FCM", false)) {
                FirebaseMessaging.getInstance().subscribeToTopic("Global")
                        .addOnCompleteListener(task -> {
                            String msg = getString(R.string.msg_subscribed);
                            editor.putBoolean("FCM", true);
                            if (!task.isSuccessful()) {
                                msg = getString(R.string.msg_subscribe_failed);
                                editor.putBoolean("FCM", false);
                            }
                            editor.apply();
                            Log.d(TAG, msg);
                        });
            }
            Intent intent = new Intent(getApplicationContext(), App.class);
            startActivity(intent);
            finish();
        }
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.button);
        button.setOnClickListener(v -> {
            // Choose authentication providers
            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.EmailBuilder().build(),
                    new AuthUI.IdpConfig.GoogleBuilder().build());

            // Create and launch sign-in intent
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .build(),
                    RC_SIGN_IN);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        sharedPreferences = getSharedPreferences(spFileKey, MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    if (!sharedPreferences.getBoolean("FCM", false)) {
                        FirebaseMessaging.getInstance().subscribeToTopic("Global")
                                .addOnCompleteListener(task -> {
                                    String msg = getString(R.string.msg_subscribed);
                                    editor.putBoolean("FCM", true);
                                    if (!task.isSuccessful()) {
                                        msg = getString(R.string.msg_subscribe_failed);
                                        editor.putBoolean("FCM", false);
                                    }
                                    editor.apply();
                                    Log.d(TAG, msg);
                                });
                    }
                    // Accessing Cloud FireStore...
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    //Creating New User...
                    String name = user.getDisplayName();
                    String email = user.getEmail();
                    if (email != null && name != null) {
                        Map<String, Object> newUser = new HashMap<>();
                        newUser.put("Name", name);
                        db.collection("users")
                                .document(email)
                                .set(newUser);
                    }
                    Intent intent = new Intent(getApplicationContext(), App.class);
                    startActivity(intent);
                    finish();
                } else {
                    // Sign in failed. If response is null the user canceled the
                    // sign-in flow using the back button. Otherwise check
                    // response.getError().getErrorCode() and handle the error.
                    // ...
                    Log.e(TAG, "Failure!");
                }
            }

        }
    }
}