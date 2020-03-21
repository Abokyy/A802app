package csapat.app.auth;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import csapat.app.BaseCompat;
import csapat.app.NavMainActivity;
import csapat.app.R;
import csapat.app.supportFiles.SaveSharedPreference;
import csapat.app.teamstructure.model.AppUser;

public class LoginActivity extends BaseCompat {


    private FirebaseAuth mAuth;
    private EditText etEmail;
    private EditText etPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);


        Button registerBtn = findViewById(R.id.btnRegister);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
            }
        });

        Button loginBtn = findViewById(R.id.btnLogin);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateForm()) {

                    signInUserWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString());

                }
            }
        });

        Button guestLogin = findViewById(R.id.guestLoginBtn);
        guestLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appUser = new AppUser();
                appUser.setUsername("802guest");
                SaveSharedPreference.setAppUser(LoginActivity.this, appUser, "guest");
                Intent guestIntent = new Intent(LoginActivity.this, NavMainActivity.class);
                startActivity(guestIntent);
            }
        });

    }

    private boolean validateForm() {
        boolean valid = true;

        String email = etEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Required.");
            valid = false;
        } else {
            etEmail.setError(null);
        }

        String password = etPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Required.");
            valid = false;
        } else {
            etPassword.setError(null);
        }

        return valid;
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
        finish();
    }

    public void signInUserWithEmailAndPassword(String email, String password) {
        if (!validateForm()) {
            return;
        }

        showProgressDialog();


        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            if (user.isEmailVerified())
                                readData();
                            else {
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                hideProgressDialog();
                            }


                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            hideProgressDialog();
                        }

                    }
                });


        ;


    }


    private void readData() {

        db.collection("users")
                .whereEqualTo("email", etEmail.getText().toString())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                appUser = document.toObject(AppUser.class);
                                SaveSharedPreference.setAppUser(LoginActivity.this, appUser, document.getId());
                            }
                        } else {
                            //TODO handle unsuccessful query
                        }


                        hideProgressDialog();
                        Intent loginIntent = new Intent(LoginActivity.this, NavMainActivity.class);
                        loginIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(loginIntent);
                        finish();

                    }


                });

    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }


}
